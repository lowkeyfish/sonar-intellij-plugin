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
import java.awt.CardLayout;
import java.util.Arrays;
import java.util.List;

import com.intellij.openapi.project.Project;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.JBPanel;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.messages.DuplicatedBlocksIssueClickListener;
import com.yujunyang.intellij.plugin.sonar.messages.IssueClickListener;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;

public class IssueDetailPanel extends JBPanel implements IssueClickListener, DuplicatedBlocksIssueClickListener {
    private Project project;
    private CardLayout layout;
    private IssueCodePanel codePanel;
    private IssueDescriptionPanel descriptionPanel;

    public IssueDetailPanel(Project project) {
        this.project = project;
        init();
        MessageBusManager.subscribe(project, this, IssueClickListener.TOPIC, this::click);
        MessageBusManager.subscribe(project, this, DuplicatedBlocksIssueClickListener.TOPIC, this::click);
    }

    @Override
    public void click(List<DuplicatedBlocksIssue> issues) {
        layout.show(this, "DETAIL");
        codePanel.show(issues);
        descriptionPanel.show(issues);
        revalidate();
        repaint();
    }

    @Override
    public void click(Issue issue) {
        layout.show(this, "DETAIL");
        codePanel.show(Arrays.asList(issue));
        descriptionPanel.show(Arrays.asList(issue));
        revalidate();
        repaint();
    }

    private void init() {
        layout = new CardLayout();
        setLayout(layout);

        add("EMPTY", new MessagePanel("选择一个问题查看"));

        JBPanel detailPanel = new JBPanel(new BorderLayout());
        add("DETAIL", detailPanel);

        OnePixelSplitter listAndCurrentSplitter = new OnePixelSplitter();
        listAndCurrentSplitter.getDivider().setBackground(UIUtils.borderColor());
        detailPanel.add(listAndCurrentSplitter, BorderLayout.CENTER);

        codePanel = new IssueCodePanel(project);
        listAndCurrentSplitter.setFirstComponent(codePanel);

        descriptionPanel = new IssueDescriptionPanel(project);
        listAndCurrentSplitter.setSecondComponent(descriptionPanel);

        listAndCurrentSplitter.setProportion(0.6f);

        layout.show(this, "EMPTY");
    }

    public void reset() {
        layout.show(this, "EMPTY");
    }
}
