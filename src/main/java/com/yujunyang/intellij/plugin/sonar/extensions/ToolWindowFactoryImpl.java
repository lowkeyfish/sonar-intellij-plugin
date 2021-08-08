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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolWindowFactoryImpl implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JBPanel reportPanel = new ReportPanel(project);
        JBPanel logPanel = new LogPanel(project);
        toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(reportPanel, "Report", false));
        toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(logPanel, "Log", false));
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
