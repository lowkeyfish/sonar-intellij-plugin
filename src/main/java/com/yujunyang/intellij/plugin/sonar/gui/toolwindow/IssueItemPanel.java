package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.SwingConstants;

import com.intellij.openapi.util.Pair;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;


public class IssueItemPanel extends JBPanel {
    private boolean selected = false;
    private boolean isDuplicatedBlockIssue;
    private Issue issue;
    private List<DuplicatedBlocksIssue> duplicatedBlocksIssues;

    public IssueItemPanel(Issue issue) {
        this.issue = issue;
        init();
    }

    public IssueItemPanel(List<DuplicatedBlocksIssue> duplicatedBlocksIssues) {
        this.duplicatedBlocksIssues = duplicatedBlocksIssues;
        this.isDuplicatedBlockIssue = true;
        init();
    }

    public boolean isSelected() {
        return selected;
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(5)));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                click();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                highlight();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cancelHighlight();
            }
        });

        String msg = !isDuplicatedBlockIssue ? issue.getMsg() : String.format("%s duplicated blocks of code must be removed.", duplicatedBlocksIssues.size());
        JBTextArea msgTextArea = UIUtils.createWrapLabelLikedTextArea(msg);
        msgTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                highlight();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                click();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cancelHighlight();
            }
        });
        add(msgTextArea, BorderLayout.NORTH);

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

    private void highlight() {
        UIUtils.setBackgroundRecursively(this, UIUtils.highlightBackgroundColor());
    }

    private void cancelHighlight() {
        if (selected) {
            return;
        }
        UIUtils.setBackgroundRecursively(this);
    }

    public void unSelect() {
        selected = false;
        cancelHighlight();
    }

    private void select() {
        highlight();
        selected = true;
        if (isDuplicatedBlockIssue) {
            MessageBusManager.publishDuplicatedBlocksIssueClick(duplicatedBlocksIssues.get(0).getPsiFile().getProject(), duplicatedBlocksIssues);
        } else {
            MessageBusManager.publishIssueClick(issue.getPsiFile().getProject(), issue);
        }
    }

    private void click() {
        if (selected) {
            return;
        }
        Component parent = this.getParent();
        Component[] parentSiblings = parent.getParent().getComponents();
        for (Component parentSibling : parentSiblings) {
            boolean done = false;
            if (parentSibling instanceof IssueFileGroupPanel) {
                Component[] components = ((IssueFileGroupPanel) parentSibling).getComponents();
                for (Component component : components) {
                    if (component instanceof IssueItemPanel && ((IssueItemPanel) component).isSelected()) {
                        ((IssueItemPanel)component).unSelect();
                        done = true;
                        break;
                    }
                }
            }
            if (done) {
                break;
            }
        }

        select();
    }

}
