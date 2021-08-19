package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBViewport;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;

public class IssuesPanel extends JBPanel {
    private Project project;
    private SummaryPanel summaryPanel;
    private IssueListPanel issueListPanel;
    private JBScrollPane listScrollPane;

    public IssuesPanel(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        summaryPanel = new SummaryPanel(project);
        add(summaryPanel, BorderLayout.NORTH);

        listScrollPane = new JBScrollPane();
        listScrollPane.setBorder(JBUI.Borders.empty());
        listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(listScrollPane, BorderLayout.CENTER);

        issueListPanel = new IssueListPanel(project);

        // 设置Viewport以便内部issueListPanel内容增加时滚动条不要滚动到底部
        listScrollPane.setViewport(new JBViewport() {
            @Override
            public void scrollRectToVisible(Rectangle bounds) {
            }
        });
        listScrollPane.setViewportView(issueListPanel);

        UIUtils.setBackgroundRecursively(this, UIUtils.backgroundColor());
    }

    public void refresh() {
        // 每次加载数据把纵向滚动条回原，再配合设置的Viewport可以使每次加载数据纵向滚动条都在顶部
        listScrollPane.getVerticalScrollBar().setValue(0);
        summaryPanel.refresh();
        issueListPanel.refresh();
        UIUtils.setBackgroundRecursively(this, UIUtils.backgroundColor());
    }
}
