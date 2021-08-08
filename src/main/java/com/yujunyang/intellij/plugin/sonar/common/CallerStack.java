
package com.yujunyang.intellij.plugin.sonar.common;

public class CallerStack extends Throwable {

	private static final long serialVersionUID = 1L;


	public CallerStack() {
		super("called from");
	}


	public CallerStack(final CallerStack cause) {
		super("called from", cause);
	}


	public static void initCallerStack(final Throwable throwable, final CallerStack callerStack) {
		Throwable lastCause = throwable;
		while (lastCause.getCause() != null) {
			lastCause = lastCause.getCause();
		}
		try {
			lastCause.initCause(callerStack);
		} catch (final IllegalStateException ignored) {
			// some exceptions may override getCause(), but not initCause()
			// => getCause() can be null, but cause is alreay set
		}
	}


}
