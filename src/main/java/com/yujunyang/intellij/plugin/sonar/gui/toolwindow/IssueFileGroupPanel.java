package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;

import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue2;
import com.yujunyang.intellij.plugin.sonar.core.Issue;

public class IssueFileGroupPanel extends JBPanel {
    private PsiFile psiFile;
    private List<AbstractIssue> issues;

    public IssueFileGroupPanel(PsiFile psiFile, List<AbstractIssue> issues) {
        this.psiFile = psiFile;
        this.issues = issues;
        init();
    }

    private void init() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        setBorder(JBUI.Borders.empty(0, 5));

        addPathLabel();

        List<DuplicatedBlocksIssue2> duplicatedBlocksIssues = issues.stream()
                .filter(n -> n instanceof DuplicatedBlocksIssue2).map(n -> (DuplicatedBlocksIssue2)n).collect(Collectors.toList());
        if (duplicatedBlocksIssues.size() > 0) {
            addIssue(duplicatedBlocksIssues);
        }
        issues.stream().filter(n -> n instanceof Issue).forEach(n -> addIssue((Issue)n));
        add(Box.createVerticalStrut(5));
    }


    private void addPathLabel() {
        JBLabel label = new JBLabel(IdeaUtils.getPath(psiFile));
        label.setBorder(JBUI.Borders.empty(5, 0));
        label.setForeground(Color.GRAY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        add(label);
    }

    private void addIssue(List<DuplicatedBlocksIssue2> issues) {
        IssueItemPanel panel = new IssueItemPanel(issues, null);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        add(panel);
        add(Box.createVerticalStrut(5));
    }

    private void addIssue(Issue issue) {
        IssueItemPanel panel = new IssueItemPanel(issue, null);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        add(panel);
        add(Box.createVerticalStrut(5));
    }
}
