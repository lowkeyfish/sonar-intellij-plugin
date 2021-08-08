package com.yujunyang.intellij.plugin.sonar.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yujunyang.intellij.plugin.sonar.gui.dialog.SettingsDialog;
import org.jetbrains.annotations.NotNull;

public class ShowSettingDialogAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new SettingsDialog(e.getProject()).show();
    }
}
