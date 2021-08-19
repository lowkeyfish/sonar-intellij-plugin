package com.yujunyang.intellij.plugin.sonar.gui.popup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.ui.UI;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;


public class IssueItemPanel extends JBPanel {
    private AbstractIssue issue;

    public IssueItemPanel(AbstractIssue issue) {
        this.issue = issue;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(5)));

//        JBTextArea msgTextArea = UIUtils.createWrapLabelLikedTextArea(issue.getMsg());
//        // JBTextArea不管用
//        msgTextArea.setSize(400, 1);
//        add(msgTextArea, BorderLayout.NORTH);

        // 必须使用JTextArea才能控制固定宽度
        JTextArea textArea = new JTextArea();
        textArea.setFont(UIUtil.getLabelFont());
        textArea.setText(issue.getMsg());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setSize(400, 1);
        add(textArea, BorderLayout.NORTH);

        Pair<String, Icon> typeInfo = UIUtils.typeInfo(issue.getType());
        Pair<String, Icon> severityInfo = UIUtils.severityInfo(issue.getSeverity());

        JBPanel infoPanel = UIUtils.createBoxLayoutPanel(BoxLayout.X_AXIS);
        infoPanel.setBorder(JBUI.Borders.empty(5, 0, 0, 0));
        add(infoPanel, issue instanceof DuplicatedBlocksIssue ? BorderLayout.SOUTH : BorderLayout.CENTER);
        JBLabel typeLabel = new JBLabel(typeInfo.first, typeInfo.second, SwingConstants.LEFT);
        infoPanel.add(typeLabel);
        infoPanel.add(Box.createHorizontalStrut(10));
        JBLabel severityLabel = new JBLabel(severityInfo.first, severityInfo.second, SwingConstants.LEFT);
        infoPanel.add(severityLabel);

        if (issue instanceof DuplicatedBlocksIssue) {
            JBPanel duplicatesPanel = new JBPanel();
            BoxLayout boxLayout = new BoxLayout(duplicatesPanel, BoxLayout.Y_AXIS);
            duplicatesPanel.setLayout(boxLayout);

            ((DuplicatedBlocksIssue)issue).getDuplicates().forEach(n -> {
                duplicatesPanel.add(Box.createVerticalStrut(5));
                duplicatesPanel.add(createDuplicatePanel(n));
            });

            add(duplicatesPanel, BorderLayout.CENTER);
        }
    }

    private JBPanel createDuplicatePanel(DuplicatedBlocksIssue.Duplicate duplicate) {
        JBPanel panel = new JBPanel(new BorderLayout());

        JBLabel rowRangeLabel = new JBLabel(String.format("[%s-%s]", duplicate.getStartLine(), duplicate.getEndLine()));
        rowRangeLabel.setBorder(JBUI.Borders.empty(0, 0, 0, 5));
        panel.add(rowRangeLabel, BorderLayout.WEST);
        rowRangeLabel.setForeground(Color.BLUE);
        rowRangeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JBPanel that = this;
        rowRangeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PsiFile psiFile = StringUtil.isEmpty(duplicate.getPath()) ? issue.getPsiFile() : IdeaUtils.getPsiFile(issue.getPsiFile().getProject(), duplicate.getPath());
                UIUtils.navigateToLine(psiFile, duplicate.getStartLine() - 1);
                ((Supplier<JBPopup>)(that.getClientProperty("IssueItemPanel.getOwnerPopupFunction"))).get().cancel();
            }
        });

        String duplicateFileName = "";
        if (!StringUtil.isEmpty(duplicate.getPath())) {
            duplicateFileName = IdeaUtils.getPsiFile(issue.getPsiFile().getProject(), duplicate.getPath()).getName();
        }
        JBLabel filePathLabel = new JBLabel(duplicateFileName);
        panel.add(filePathLabel, BorderLayout.CENTER);

        return panel;
    }

}
