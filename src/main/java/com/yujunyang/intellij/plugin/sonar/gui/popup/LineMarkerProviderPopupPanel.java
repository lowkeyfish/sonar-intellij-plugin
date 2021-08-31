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

package com.yujunyang.intellij.plugin.sonar.gui.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;

public class LineMarkerProviderPopupPanel extends JBPanel {
    private Project project;
    private List<AbstractIssue> issues;
    private JBPopup popup;

    public LineMarkerProviderPopupPanel(Project project, List<AbstractIssue> issues) {
        this.project = project;
        this.issues = issues;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.empty(5));

        JBLabel title = new JBLabel(ResourcesLoader.getString("lineMarker.lineSummary",issues.size()));
        add(title, BorderLayout.NORTH);

        add(createIssues(), BorderLayout.CENTER);

        UIUtils.setBackgroundRecursively(this, UIUtils.backgroundColor());
    }

    private JBPanel createIssues() {
        JBPanel ret = new JBPanel();
        BoxLayout layout = new BoxLayout(ret, BoxLayout.Y_AXIS);
        ret.setLayout(layout);
        for (AbstractIssue issue : issues) {
            ret.add(Box.createVerticalStrut(5));
            IssueItemPanel issueItemPanel = new IssueItemPanel(issue);
            Supplier<JBPopup> getOwnerPopupFunction = () -> this.popup;
            issueItemPanel.putClientProperty("IssueItemPanel.getOwnerPopupFunction",  getOwnerPopupFunction);
            ret.add(issueItemPanel);
        }

        return ret;
    }

    public void setPopup(JBPopup popup) {
        this.popup = popup;
    }
}
