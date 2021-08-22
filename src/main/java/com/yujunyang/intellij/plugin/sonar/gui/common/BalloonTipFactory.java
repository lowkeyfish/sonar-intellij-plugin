
/*
 * Copyright 2021 Yu Junyang
 * https://github.com/lowkeyfish
 *
 * This file is part of Sonar Intellij plugin.
 *
 * Sonar Intellij plugin is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Sonar Intellij plugin is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar Intellij plugin.
 * If not, see <http://www.gnu.org/licenses/>.
 */

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
