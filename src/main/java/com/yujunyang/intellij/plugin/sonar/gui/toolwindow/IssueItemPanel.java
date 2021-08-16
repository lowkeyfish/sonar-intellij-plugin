package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.SwingConstants;

import com.intellij.openapi.util.Pair;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue2;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;


public class IssueItemPanel extends JBPanel {
    private boolean selected = false;
    private boolean isDuplicatedBlockIssue;
    private Issue issue;
    private List<DuplicatedBlocksIssue2> duplicatedBlocksIssues;
    private Consumer<String> selectHandler;

    public IssueItemPanel(Issue issue, Consumer<String> selectHandler) {
        this.issue = issue;
        this.selectHandler = selectHandler;
        init();
    }

    public IssueItemPanel(List<DuplicatedBlocksIssue2> duplicatedBlocksIssues, Consumer<String> selectHandler) {
        this.duplicatedBlocksIssues = duplicatedBlocksIssues;
        this.isDuplicatedBlockIssue = true;
        this.selectHandler = selectHandler;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(5)));

        String msg = !isDuplicatedBlockIssue ? issue.getMsg() : String.format("%s duplicated blocks of code must be removed.", duplicatedBlocksIssues.size());
        JBLabel msgLabel = new JBLabel(msg);
        msgLabel.setAllowAutoWrapping(true);
        add(msgLabel, BorderLayout.NORTH);

        JBPanel infoPanel = UIUtils.createBoxLayoutPanel(BoxLayout.X_AXIS);
        infoPanel.setBorder(JBUI.Borders.empty(5, 0, 0, 0));
        add(infoPanel, isDuplicatedBlockIssue ? BorderLayout.SOUTH : BorderLayout.CENTER);

        String type = !isDuplicatedBlockIssue ? issue.getType() : duplicatedBlocksIssues.get(0).getType();
        Pair<String, Icon> typeInfo = UIUtils.typeInfo(type);
        JBLabel typeLabel = new JBLabel(typeInfo.first, typeInfo.second, SwingConstants.LEFT);
        infoPanel.add(typeLabel);

        infoPanel.add(Box.createHorizontalStrut(10));

        String severity = !isDuplicatedBlockIssue ? issue.getSeverity() : duplicatedBlocksIssues.get(0).getSeverity();
        Pair<String, Icon> severityInfo = UIUtils.severityInfo(severity);
        JBLabel severityLabel = new JBLabel(severityInfo.first, severityInfo.second, SwingConstants.LEFT);
        infoPanel.add(severityLabel);

        if (isDuplicatedBlockIssue) {
            JBPanel duplicateInfoPanel = UIUtils.createBoxLayoutPanel(BoxLayout.X_AXIS);
            duplicateInfoPanel.setBorder(JBUI.Borders.empty(5, 0, 0, 0));
            add(duplicateInfoPanel, BorderLayout.CENTER);

            duplicatedBlocksIssues.forEach(duplicatedBlocksIssue -> {
                JBLabel label = new JBLabel(String.format("[%s-%s]", duplicatedBlocksIssue.getLineStart(), duplicatedBlocksIssue.getLineEnd()));
                duplicateInfoPanel.add(label);
                duplicateInfoPanel.add(Box.createHorizontalStrut(10));
            });
        }
    }



}
