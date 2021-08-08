
package com.yujunyang.intellij.plugin.sonar.gui.common;

import javax.annotation.Nullable;
import javax.swing.event.HyperlinkListener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.wm.ToolWindowManager;
import com.yujunyang.intellij.plugin.sonar.common.PluginConstants;
import org.jetbrains.annotations.NotNull;

public final class BalloonTipFactory {

	public static void showToolWindowInfoNotifier(@NotNull final Project project, final String html) {
		showToolWindowInfoNotifier(project, html, null);
	}

	public static void showToolWindowInfoNotifier(@NotNull final Project project, final String html, @Nullable final HyperlinkListener hyperlinkListener) {
		final ToolWindowManager manager = ToolWindowManager.getInstance(project);
		if (manager == null) { // this should never happen.
			return;
		}
		manager.notifyByBalloon(PluginConstants.TOOL_WINDOW_ID, MessageType.INFO, html, null, hyperlinkListener);
	}

	public static void showToolWindowWarnNotifier(@NotNull final Project project, final String html) {
		showToolWindowWarnNotifier(project, html, null);
	}

	public static void showToolWindowWarnNotifier(@NotNull final Project project, final String html, @Nullable final HyperlinkListener hyperlinkListener) {
		final ToolWindowManager manager = ToolWindowManager.getInstance(project);
		if (manager == null) { // this should never happen.
			return;
		}
		manager.notifyByBalloon(PluginConstants.TOOL_WINDOW_ID, MessageType.WARNING, html, null, hyperlinkListener);
	}

	public static void showToolWindowErrorNotifier(@NotNull final Project project, final String html) {
		showToolWindowErrorNotifier(project, html, null);
	}

	public static void showToolWindowErrorNotifier(@NotNull final Project project, final String html, @Nullable final HyperlinkListener hyperlinkListener) {
		final ToolWindowManager manager = ToolWindowManager.getInstance(project);
		if (manager == null) { // this should never happen.
			return;
		}
		manager.notifyByBalloon(PluginConstants.TOOL_WINDOW_ID, MessageType.ERROR, html, null, hyperlinkListener);
	}

	private BalloonTipFactory() {
	}
}
