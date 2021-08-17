package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;

public class SummaryPanel extends JBPanel {
    private Project project;
    private JBPanel vulnerabilityCountPanel;
    private JBLabel vulnerabilityCountLabel;
    private JBPanel bugCountPanel;
    private JBLabel bugCountLabel;
    private JBPanel codeSmellCountPanel;
    private JBLabel codeSmellCountLabel;
    private JBPanel duplicatedBlocksCountPanel;
    private JBLabel duplicatedBlocksCountLabel;

    public SummaryPanel(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        setLayout(new GridLayout(1, 4, 10, 0));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 0, UIUtils.borderColor()),
                JBUI.Borders.empty(5, 5, 15, 5)
        ));

        vulnerabilityCountPanel = createPanel();
        add(vulnerabilityCountPanel);
        vulnerabilityCountLabel = createLabel();
        vulnerabilityCountPanel.add(vulnerabilityCountLabel, BorderLayout.NORTH);
        vulnerabilityCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel("漏洞"), BorderLayout.CENTER);

        bugCountPanel = createPanel();
        add(bugCountPanel);
        bugCountLabel = createLabel();
        bugCountPanel.add(bugCountLabel, BorderLayout.NORTH);
        bugCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel("Bugs"), BorderLayout.CENTER);

        codeSmellCountPanel = createPanel();
        add(codeSmellCountPanel);
        codeSmellCountLabel = createLabel();
        codeSmellCountPanel.add(codeSmellCountLabel, BorderLayout.NORTH);
        codeSmellCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel("异味"), BorderLayout.CENTER);

        duplicatedBlocksCountPanel = createPanel();
        add(duplicatedBlocksCountPanel);
        duplicatedBlocksCountLabel = createLabel();
        duplicatedBlocksCountPanel.add(duplicatedBlocksCountLabel, BorderLayout.NORTH);
        duplicatedBlocksCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel("重复块"), BorderLayout.CENTER);
    }

    public void refresh() {
        ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
        vulnerabilityCountLabel.setText(String.valueOf(problemCacheService.getVulnerabilityCount()));
        bugCountLabel.setText(String.valueOf(problemCacheService.getBugCount()));
        codeSmellCountLabel.setText(String.valueOf(problemCacheService.getCodeSmellCount()));
        duplicatedBlocksCountLabel.setText(String.valueOf(problemCacheService.getDuplicatedBlocksCount()));
    }

    private JBPanel createPanel() {
        JBPanel panel = new JBPanel();
//        panel.setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(5)));
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JBLabel createLabel() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
        JBLabel label = UIUtils.createHorizontalAlignmentCenterLabel("0", font);
        return label;
    }
}
