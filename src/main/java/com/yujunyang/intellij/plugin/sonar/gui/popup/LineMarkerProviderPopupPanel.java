package com.yujunyang.intellij.plugin.sonar.gui.popup;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.Issue;

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

        JBLabel title = new JBLabel("问题: " + issues.size() + "个");
        add(title, BorderLayout.NORTH);

        add(createIssues(), BorderLayout.CENTER);
    }

    private JBPanel createIssues() {
        JBPanel ret = new JBPanel();
        BoxLayout layout = new BoxLayout(ret, BoxLayout.Y_AXIS);
        ret.setLayout(layout);
        for (AbstractIssue issue : issues) {
            if (issue instanceof Issue) {
                whenIssue(ret, (Issue)issue);
            } else if (issue instanceof DuplicatedBlocksIssue) {
                whenDuplicatedBlocksIssue(ret, (DuplicatedBlocksIssue)issue);
            }
        }

        return ret;
    }

    private void whenIssue(JBPanel ret, Issue issue) {
        ret.add(Box.createVerticalStrut(5));
        JBLabel msgLabel = new JBLabel(issue.getMsg());
        ret.add(msgLabel);
        String info = String.format("%s, %s", issue.getType(), issue.getSeverity());
        JBLabel infoLabel = new JBLabel(info);
        ret.add(infoLabel);

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
