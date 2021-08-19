package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;

public class IssueDescriptionPanel extends JBPanel {
    private Project project;
    private JTextArea nameTextArea;
    private JBPanel infoPanel;
    private JEditorPane descriptionEditorPane;
    private JBScrollPane descriptionScrollPane;

    public IssueDescriptionPanel(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        JBPanel headerPanel = new JBPanel(new BorderLayout());
        headerPanel.setBorder(JBUI.Borders.empty(10));
        add(headerPanel, BorderLayout.NORTH);

        // 必须使用ScrollPane包裹TextArea，否则Splitter不能往右拖动，相当于TextArea的宽度只能增大不能缩小了
        nameTextArea = UIUtils.createWrapLabelLikedTextArea("");
        JBScrollPane sp = new JBScrollPane(nameTextArea);
        sp.setBorder(JBUI.Borders.empty());
        sp.setViewportView(nameTextArea);
        Font font = new Font(UIUtil.getLabelFont().getFontName(), Font.PLAIN, 20);
        nameTextArea.setFont(font);
        headerPanel.add(sp, BorderLayout.NORTH);
        infoPanel = UIUtils.createBoxLayoutPanel(BoxLayout.X_AXIS);
        infoPanel.setBorder(JBUI.Borders.empty(10, 0, 0, 0));
        headerPanel.add(infoPanel, BorderLayout.CENTER);

        descriptionEditorPane = new JEditorPane();
        descriptionEditorPane.setBorder(JBUI.Borders.empty(10));
        descriptionEditorPane.setContentType("text/html");
        descriptionEditorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        descriptionEditorPane.setFont(UIUtil.getLabelFont());
        descriptionEditorPane.setEditable(false);
        // 防止每次更新内容后滚动条发生滚动，期望据顶据左
        ((DefaultCaret)descriptionEditorPane.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        JBScrollPane descriptionScrollPane = new JBScrollPane(descriptionEditorPane);
        descriptionScrollPane.setBorder(JBUI.Borders.empty(0));
        descriptionScrollPane.setViewportView(descriptionEditorPane);
        descriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        descriptionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(descriptionScrollPane, BorderLayout.CENTER);

        UIUtils.setBackgroundRecursively(this);
    }

    public void show(List<? extends AbstractIssue> issues) {
        AbstractIssue issue = issues.get(0);
        nameTextArea.setText(issue.getName());

        infoPanel.removeAll();
        Pair<String, Icon> typeInfo = UIUtils.typeInfo(issue.getType());
        infoPanel.add(new JBLabel(typeInfo.first, typeInfo.second, SwingConstants.LEFT));
        infoPanel.add(Box.createHorizontalStrut(10));
        Pair<String, Icon> severityInfo = UIUtils.severityInfo(issue.getSeverity());
        infoPanel.add(new JBLabel(severityInfo.first, severityInfo.second, SwingConstants.LEFT));

        descriptionEditorPane.setText(String.format("<html><head><style>body{overflow:auto;}</style></head><body>%s</body></body>", issue.getHtmlDesc()));
    }
}
