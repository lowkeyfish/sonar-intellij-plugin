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


