package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.components.JBPanel;
import com.yujunyang.intellij.plugin.sonar.common.PluginConstants;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;

public class LeftToolbarPanel extends JBPanel {
    private boolean needUpdate = false;

    public LeftToolbarPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIUtils.borderColor()));
        init();
    }

    private void init() {
        ActionGroup actionGroupLeft = (ActionGroup) ActionManager.getInstance().getAction(PluginConstants.ACTION_GROUP_LEFT);
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("left", actionGroupLeft, false);
        add(actionToolbar.getComponent(), BorderLayout.CENTER);
    }

}


