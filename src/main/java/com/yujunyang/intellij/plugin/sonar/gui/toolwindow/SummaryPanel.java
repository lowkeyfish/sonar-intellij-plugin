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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;

public class SummaryPanel extends JBPanel {
    private Project project;
    private JBPanel vulnerabilityCountPanel;
    private JBLabel vulnerabilityCountLabel;
    private JBPanel bugCountPanel;
    private JBLabel bugCountLabel;
    private JBPanel codeSmellCountPanel;
    private JBLabel codeSmellCountLabel;
    private JBPanel securityHotSpotCountPanel;
    private JBLabel securityHotSpotCountLabel;
    private JBPanel duplicatedBlocksCountPanel;
    private JBLabel duplicatedBlocksCountLabel;

    public SummaryPanel(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        setLayout(new GridLayout(1, 4, 5, 0));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 0, UIUtils.borderColor()),
                JBUI.Borders.empty(5, 5, 15, 5)
        ));

        bugCountPanel = createPanel();
        add(bugCountPanel);
        bugCountLabel = createLabel();
        bugCountPanel.add(bugCountLabel, BorderLayout.NORTH);
        bugCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel(ResourcesLoader.getString("issueType.bug")), BorderLayout.CENTER);

        codeSmellCountPanel = createPanel();
        add(codeSmellCountPanel);
        codeSmellCountLabel = createLabel();
        codeSmellCountPanel.add(codeSmellCountLabel, BorderLayout.NORTH);
        codeSmellCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel(ResourcesLoader.getString("issueType.codeSmell")), BorderLayout.CENTER);

        vulnerabilityCountPanel = createPanel();
        add(vulnerabilityCountPanel);
        vulnerabilityCountLabel = createLabel();
        vulnerabilityCountPanel.add(vulnerabilityCountLabel, BorderLayout.NORTH);
        vulnerabilityCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel(ResourcesLoader.getString("issueType.vulnerability")), BorderLayout.CENTER);

        securityHotSpotCountPanel = createPanel();
        add(securityHotSpotCountPanel);
        securityHotSpotCountLabel = createLabel();
        securityHotSpotCountPanel.add(securityHotSpotCountLabel, BorderLayout.NORTH);
        securityHotSpotCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel(ResourcesLoader.getString("issueType.securityHotspot")), BorderLayout.CENTER);

        duplicatedBlocksCountPanel = createPanel();
        add(duplicatedBlocksCountPanel);
        duplicatedBlocksCountLabel = createLabel();
        duplicatedBlocksCountPanel.add(duplicatedBlocksCountLabel, BorderLayout.NORTH);
        duplicatedBlocksCountPanel.add(UIUtils.createHorizontalAlignmentCenterLabel(ResourcesLoader.getString("issueType.duplication")), BorderLayout.CENTER);
    }

    public void refresh() {
        ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
        vulnerabilityCountLabel.setText(String.valueOf(problemCacheService.getVulnerabilityCount()));
        bugCountLabel.setText(String.valueOf(problemCacheService.getBugCount()));
        codeSmellCountLabel.setText(String.valueOf(problemCacheService.getCodeSmellCount()));
        duplicatedBlocksCountLabel.setText(String.valueOf(problemCacheService.getDuplicatedBlocksCount()));
        securityHotSpotCountLabel.setText(String.valueOf(problemCacheService.getSecurityHotSpotCount()));
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

    public void reset() {
        vulnerabilityCountLabel.setText("0");
        bugCountLabel.setText("0");
        codeSmellCountLabel.setText("0");
        duplicatedBlocksCountLabel.setText("0");
        securityHotSpotCountLabel.setText("0");
    }
}
