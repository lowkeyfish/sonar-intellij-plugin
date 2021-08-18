package com.yujunyang.intellij.plugin.sonar.gui.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;

public class LineMarkerProviderPopupPanel extends JBPanel {
    private Project project;
    private List<AbstractIssue> issues;

    public LineMarkerProviderPopupPanel(Project project, List<AbstractIssue> issues) {
        this.project = project;
        this.issues = issues;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.empty(5));

        JBLabel title = new JBLabel("问题: " + issues.size() + "个");
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
            ret.add(new IssueItemPanel(issue));
        }

        return ret;
    }

    private void whenDuplicatedBlocksIssue(JBPanel ret, DuplicatedBlocksIssue issue) {
        ret.add(Box.createVerticalStrut(5));
        JBLabel msgLabel = new JBLabel(String.format("[%s-%s]行代码与%s个代码块重复", issue.getLineStart(), issue.getLineEnd(), issue.getDuplicateCount()));
        ret.add(msgLabel);
        String info = String.format("%s, %s", issue.getType(), issue.getSeverity());
        JBLabel infoLabel = new JBLabel(info);
        ret.add(infoLabel);
        for (DuplicatedBlocksIssue.Duplicate duplicate : issue.getDuplicates()) {
            String duplicateInfo = String.format("[%s-%s] %s", duplicate.getStartLine(), duplicate.getEndLine(), duplicate.getPath());
            JBLabel duplicateInfoLabel = new JBLabel(duplicateInfo);
            ret.add(duplicateInfoLabel);
        }
    }
}
