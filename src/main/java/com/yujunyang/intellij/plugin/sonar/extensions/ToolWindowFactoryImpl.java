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

package com.yujunyang.intellij.plugin.sonar.extensions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.yujunyang.intellij.plugin.sonar.common.PluginConstants;
import com.yujunyang.intellij.plugin.sonar.gui.toolwindow.LogPanel;
import com.yujunyang.intellij.plugin.sonar.gui.toolwindow.ReportPanel;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolWindowFactoryImpl implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JBPanel reportPanel = new ReportPanel(project);
        JBPanel logPanel = new LogPanel(project);
        toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(reportPanel, ResourcesLoader.getString("toolWindow.tab.report"), false));
        toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(logPanel, ResourcesLoader.getString("toolWindow.tab.log"), false));
    }

    @Nullable
    public static ToolWindow getWindow(@NotNull final Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(PluginConstants.TOOL_WINDOW_ID);
    }

    public static void showWindow(@NotNull final ToolWindow toolWindow) {
        if (!toolWindow.isActive() && toolWindow.isAvailable()) {
            toolWindow.show(null);
        }
    }

    public static void showWindowContent(@NotNull final ToolWindow toolWindow, int contentIndex) {
        toolWindow.show(null);
        Content content = toolWindow.getContentManager().getContent(contentIndex);
        toolWindow.getContentManager().setSelectedContent(content);
    }
}
