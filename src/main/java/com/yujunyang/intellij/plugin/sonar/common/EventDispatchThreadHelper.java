
package com.yujunyang.intellij.plugin.sonar.common;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

public final class EventDispatchThreadHelper {

	private static final Logger LOGGER = Logger.getInstance(EventDispatchThreadHelper.class.getName());
	private static final boolean DEBUG_CHECK_EDT = true; // FIXME

	public static void invokeAndWait(@NotNull final Operation operation) {
		assert operation != null : "Operation must not be null!";
		if (EventQueue.isDispatchThread()) {
			operation.run();
			operation.onSuccess();
		} else {
			try {
				EventQueue.invokeAndWait(operation);
				operation.onSuccess();
			} catch (final Exception e) {
				operation.onFailure(e);
			}
		}
	}


	public static void checkNotEDT() {
		if (DEBUG_CHECK_EDT && EventQueue.isDispatchThread()) {
			throw new IllegalStateException();
		}
	}


	public static void checkEDT() {
		if (DEBUG_CHECK_EDT && !EventQueue.isDispatchThread()) {
			throw new IllegalStateException();
		}
	}


	public static void assertInEDTorADT() {
		if (!EventQueue.isDispatchThread() && !ApplicationManager.getApplication().isDispatchThread()) {
			final CallerStack caller = new CallerStack();
			final Throwable e = new NotInEDTViolation("Should run in EventDispatchThread or ApplcationtDispatchThread.");
			CallerStack.initCallerStack(e, caller);
			LOGGER.debug(e);
		}
	}


	public static void invokeLater(final Runnable runnable) {
		if (!EventQueue.isDispatchThread()) {
			@SuppressWarnings({"ThrowableInstanceNeverThrown"})
			final CallerStack caller = new CallerStack();
			final Runnable wrapper = () -> {
				try {
					runnable.run();
				} catch (final RuntimeException | Error e) {
					CallerStack.initCallerStack(e, caller);
					throw e;
				}
			};
			EventQueue.invokeLater(wrapper);
		} else {
			// we are already in EventDispatcherThread.. Runnable can be called inline
			runnable.run();
		}
	}


	public interface Operation extends Runnable {

		void onFailure(Throwable failure);

		void onSuccess();

	}


	public abstract static class OperationAdapter implements Operation {

		public void onFailure(final Throwable failure) {
			if (failure instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			} else if (failure instanceof InvocationTargetException) {
				final Throwable target = ((InvocationTargetException) failure).getTargetException();
				final StackTraceElement[] stackTrace = target.getStackTrace();
				final StringBuilder message = new StringBuilder("Operation failed. ");
				if (stackTrace.length > 2) {
					final StackTraceElement stackTraceElement = stackTrace[1];
					message.append(stackTraceElement.getClassName()).append('.').append(stackTraceElement.getMethodName()).append("() - ");
				}
				message.append(target);
				LOGGER.error(message.toString(), target);
			}
		}

		public void onSuccess() {
			// stub
		}

	}


	private EventDispatchThreadHelper() {
		// utility
	}


	private static class NotInEDTViolation extends Exception {

		private static final long serialVersionUID = 1L;


		NotInEDTViolation(final String message) {
			super(message);
		}
	}


}
