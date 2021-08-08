package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.NotNull;

public class ReportPanel extends JBPanel {

    private LeftToolbarPanel leftToolbarPanel;
    private Project project;

    public ReportPanel(@NotNull Project project) {
        this.project = project;
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        leftToolbarPanel = new LeftToolbarPanel();
        add(leftToolbarPanel, BorderLayout.WEST);
    }
}
