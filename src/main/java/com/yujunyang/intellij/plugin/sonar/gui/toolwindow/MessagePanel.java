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

import java.awt.Color;
import javax.swing.Box;
import javax.swing.BoxLayout;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;

public class MessagePanel extends JBPanel {
    private String message;

    public MessagePanel(String message) {
        this.message = message;
        init();
    }

    private void init() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        JBLabel emptyLabel = new JBLabel(message);
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(emptyLabel);
        add(Box.createVerticalGlue());
    }
}
