package com.yujunyang.intellij.plugin.sonar.gui.popup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.intellij.openapi.util.Pair;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.intellij.vcs.log.ui.frame.WrappedFlowLayout;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.gui.common.YTextArea;
import com.yujunyang.intellij.plugin.sonar.gui.toolwindow.IssueFileGroupPanel;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;


public class IssueItemPanel extends JBPanel {
    private AbstractIssue issue;

    public IssueItemPanel(AbstractIssue issue) {
        this.issue = issue;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(5)));
//        setMaximumSize(new Dimension(400, getPreferredSize().height));

//        JBPanel p = new JBPanel();
//        p.setLayout(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//        c.gridx = 0;
//        c.gridy = 0;
//        c.gridwidth = 1;

//        JBScrollPane sp = new JBScrollPane();
//        JBTextArea msgTextArea = UIUtils.createWrapLabelLikedTextArea(issue.getMsg());
        JBTextArea msgTextArea = new JBTextArea(issue.getMsg());
        msgTextArea.setColumns(50);
//        msgTextArea.setRows(4);
        msgTextArea.setEnabled(false);
        msgTextArea.setWrapStyleWord(true);
        msgTextArea.setLineWrap(true);
        msgTextArea.setFont(UIUtil.getLabelFont());
//        sp.setViewportView(msgTextArea);
//        add(sp, BorderLayout.NORTH);
        add(msgTextArea, BorderLayout.NORTH);
//
//        p.add(msgTextArea, c);
//        add(p, BorderLayout.NORTH);

//        JTextField tf = new JTextField(issue.getMsg(), 100);
//        tf.setAutoscrolls(true);
//
//        add(tf, BorderLayout.NORTH);

//        YTextArea msgTextArea = new YTextArea(issue.getMsg());
//        msgTextArea.setEnabled(false);
//        msgTextArea.setWrapStyleWord(true);
//        msgTextArea.setLineWrap(true);
//        msgTextArea.setFont(UIUtil.getLabelFont());
//        add(msgTextArea, BorderLayout.NORTH);

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


    }



}
