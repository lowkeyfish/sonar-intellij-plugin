package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;

import javax.swing.ScrollPaneConstants;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;

public class IssuesPanel extends JBPanel {
    private Project project;
    private SummaryPanel summaryPanel;
    private IssueListPanel issueListPanel;

    public IssuesPanel(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        summaryPanel = new SummaryPanel(project);
        add(summaryPanel, BorderLayout.NORTH);

        JBScrollPane listScrollPane = new JBScrollPane();
        listScrollPane.setBorder(JBUI.Borders.empty());
        listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(listScrollPane, BorderLayout.CENTER);

        issueListPanel = new IssueListPanel(project);
        listScrollPane.setViewportView(issueListPanel);

        UIUtils.setBackgroundRecursively(this, UIUtils.backgroundColor());
    }

    public void refresh() {
        summaryPanel.refresh();
        issueListPanel.refresh();
        UIUtils.setBackgroundRecursively(this, UIUtils.backgroundColor());
    }
}
