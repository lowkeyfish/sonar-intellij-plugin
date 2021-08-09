package com.yujunyang.intellij.plugin.sonar.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import org.jetbrains.annotations.NotNull;

public class StopAnalyzeAction extends AbstractAction {
    @Override
    public void updateImpl(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ToolWindow toolWindow,
            @NotNull AnalyzeState state) {
        final boolean enable = state.isStarted();

        e.getPresentation().setEnabled(false);
        e.getPresentation().setVisible(false);
    }


    @Override
    public void actionPerformedImpl(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ToolWindow toolWindow,
            @NotNull AnalyzeState state) {

    }
}
