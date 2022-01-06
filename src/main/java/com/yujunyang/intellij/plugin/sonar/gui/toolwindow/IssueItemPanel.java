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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.intellij.ide.plugins.newui.InstallButton;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;
import icons.PluginIcons;


public class IssueItemPanel extends JBPanel {
    private Project project;
    private boolean selected = false;
    private boolean isDuplicatedBlockIssue;
//    private Issue issue;
    private List<DuplicatedBlocksIssue> duplicatedBlocksIssues;
    private AbstractIssue issue;

    public IssueItemPanel(Issue issue) {
        this.project = issue.getPsiFile().getProject();
        this.issue = issue;
        init();
    }

    public IssueItemPanel(List<DuplicatedBlocksIssue> duplicatedBlocksIssues) {
        this.project = duplicatedBlocksIssues.get(0).getPsiFile().getProject();
        this.duplicatedBlocksIssues = duplicatedBlocksIssues;
        this.isDuplicatedBlockIssue = true;
        this.issue = duplicatedBlocksIssues.get(0);
        init();
    }

    public IssueItemPanel(AbstractIssue issue) {
        this.project = issue.getPsiFile().getProject();
        this.issue = issue;
        init();
    }

    public boolean isSelected() {
        return selected;
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(5)));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton fixButton = new InstallButton(false);
        fixButton.setPreferredSize(new Dimension(70, 18));
        fixButton.setVisible(false);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                highlight();
                if (!issue.isFixed()) {
                    fixButton.setVisible(true);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                boolean dblclick = false;
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    dblclick = true;
                }

                click(dblclick);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cancelHighlight();
                if (!issue.isFixed()) {
                    fixButton.setVisible(false);
                }
            }
        };
        addMouseListener(mouseAdapter);

        String msg = !isDuplicatedBlockIssue ? issue.getMsg() : String.format("%s duplicated blocks of code must be removed.", duplicatedBlocksIssues.size());
        JBTextArea msgTextArea = UIUtils.createWrapLabelLikedTextArea(msg);
        msgTextArea.addMouseListener(mouseAdapter);
        add(msgTextArea, BorderLayout.NORTH);

        JBPanel infoPanelParent = new JBPanel(new BorderLayout());
        JBPanel infoPanel = UIUtils.createBoxLayoutPanel(BoxLayout.X_AXIS);
        infoPanel.setBorder(JBUI.Borders.empty(5, 0, 0, 0));
        add(infoPanelParent, isDuplicatedBlockIssue ? BorderLayout.SOUTH : BorderLayout.CENTER);
        infoPanelParent.add(infoPanel, BorderLayout.CENTER);


        if (issue.isFixed()) {
            addResolvedLabel(infoPanelParent);
        } else {
            IssueItemPanel that = this;
            MouseAdapter fixButtonClickMouseAdapter = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    highlight();
                    fixButton.setVisible(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    cancelHighlight();
                    fixButton.setVisible(false);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isDuplicatedBlockIssue) {
                        duplicatedBlocksIssues.forEach(n -> n.setFixed(true));
                    } else {
                        issue.setFixed(true);
                    }
                    infoPanelParent.remove(fixButton);
                    addResolvedLabel(infoPanelParent);
                    MessageBusManager.publishIssueResolved(project);
                    that.revalidate();
                    ((Consumer<IssueItemPanel>)getClientProperty("RESOLVE_CALLBACK")).accept(that);
                }
            };
            fixButton.addMouseListener(fixButtonClickMouseAdapter);
            fixButton.setText(ResourcesLoader.getString("toolWindow.report.issue.fixButtonText"));

            infoPanelParent.add(fixButton, BorderLayout.EAST);
        }

//        String type = !isDuplicatedBlockIssue ? issue.getType() : duplicatedBlocksIssues.get(0).getType();
        String type = issue.getType();
        Pair<String, Icon> typeInfo = UIUtils.typeInfo(type);
        JBLabel typeLabel = new JBLabel(typeInfo.first, typeInfo.second, SwingConstants.LEFT);
        infoPanel.add(typeLabel);

        infoPanel.add(Box.createHorizontalStrut(10));

//        String severity = !isDuplicatedBlockIssue ? issue.getSeverity() : duplicatedBlocksIssues.get(0).getSeverity();
        String severity = issue.getSeverity();
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

    private void addResolvedLabel(JBPanel parent) {
        JBLabel issueResolvedLabel = new JBLabel(ResourcesLoader.getString("toolWindow.report.issue.fixButtonText"), PluginIcons.ISSUE_RESOLVED, SwingConstants.LEFT);
        issueResolvedLabel.setForeground(JBColor.namedColor("Plugins.Button.installForeground", new JBColor(0x5D9B47, 0x2B7B50)));
        parent.add(issueResolvedLabel, BorderLayout.EAST);
    }

    private void highlight() {
        UIUtils.setBackgroundRecursively(this, UIUtils.highlightBackgroundColor());
        this.setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.highlightBorderColor()), JBUI.Borders.empty(5)));
    }

    private void cancelHighlight() {
        if (selected) {
            return;
        }
        UIUtils.setBackgroundRecursively(this);
        this.setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(5)));
    }

    public void unSelect() {
        selected = false;
        cancelHighlight();
    }

    private void select() {
        highlight();
        selected = true;
        if (issue instanceof DuplicatedBlocksIssue) {
            MessageBusManager.publishDuplicatedBlocksIssueClick(issue.getPsiFile().getProject(), Arrays.asList((DuplicatedBlocksIssue)issue));
        } else {
            MessageBusManager.publishIssueClick(issue.getPsiFile().getProject(), (Issue)issue);
        }
    }

    private void click(boolean dblclick) {
        boolean autoScrollToSource = WorkspaceSettings.getInstance().autoScrollToSource;
        if (dblclick || autoScrollToSource) {
            AbstractIssue targetIssue = isDuplicatedBlockIssue ? duplicatedBlocksIssues.get(0) : issue;
            // 打开源文件并定位到问题代码
            UIUtils.navigateToOffset(targetIssue.getPsiFile(), targetIssue.getTextRange().getStartOffset());
        }

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
