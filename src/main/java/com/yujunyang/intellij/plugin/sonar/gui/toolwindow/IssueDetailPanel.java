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
