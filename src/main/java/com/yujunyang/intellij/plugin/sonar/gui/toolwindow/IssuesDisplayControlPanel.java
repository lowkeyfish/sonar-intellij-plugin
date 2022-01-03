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
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.Box;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.gui.layout.SampleVerticalScrollLayout;
import com.yujunyang.intellij.plugin.sonar.messages.IssueResolvedListener;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;

public class IssuesDisplayControlPanel extends JBPanel {
    private Project project;
    private JBLabel vulnerabilityCountLabel;
    private JBLabel bugCountLabel;
    private JBLabel codeSmellCountLabel;
    private JBLabel securityHotSpotCountLabel;
    private JBLabel duplicatedBlocksCountLabel;
    private JBLabel resolvedCountLabel;
    private JBLabel unresolvedCountLabel;
    private JBLabel updatedFilesCountLabel;
    private JBLabel notUpdatedFilesCountLabel;

    public IssuesDisplayControlPanel(Project project) {
        this.project = project;
        init();
        MessageBusManager.subscribe(project, this, IssueResolvedListener.TOPIC, this::refresh);
    }

    private void init() {
        setLayout(new SampleVerticalScrollLayout());

        addTitleLabel(ResourcesLoader.getString("toolWindow.report.displayControl.issueTypeTitle"));
        bugCountLabel = createCountLabel("0");
        addControlItemPanel("issueType.bug", bugCountLabel, "BUG");
        codeSmellCountLabel = createCountLabel("0");
        addControlItemPanel("issueType.codeSmell", codeSmellCountLabel, "CODE_SMELL");
        vulnerabilityCountLabel = createCountLabel("0");
        addControlItemPanel("issueType.vulnerability", vulnerabilityCountLabel, "VULNERABILITY");
        securityHotSpotCountLabel = createCountLabel("0");
        addControlItemPanel("issueType.securityHotspot", securityHotSpotCountLabel, "SECURITY_HOTSPOT");
        duplicatedBlocksCountLabel = createCountLabel("0");
        addControlItemPanel("issueType.duplication", duplicatedBlocksCountLabel, "DUPLICATION");

        add(Box.createVerticalStrut(5));
        addTitleLabel(ResourcesLoader.getString("toolWindow.report.displayControl.scopeTitle"));
        updatedFilesCountLabel = createCountLabel("0");
        addControlItemPanel("toolWindow.report.displayControl.scope.updatedFiles", updatedFilesCountLabel, "UPDATED_FILES");
        notUpdatedFilesCountLabel = createCountLabel("0");
        addControlItemPanel("toolWindow.report.displayControl.scope.notUpdatedFiles", notUpdatedFilesCountLabel, "NOT_UPDATED_FILES");

        add(Box.createVerticalStrut(5));
        addTitleLabel(ResourcesLoader.getString("toolWindow.report.displayControl.statusTitle"));
        resolvedCountLabel = createCountLabel("0");
        addControlItemPanel("toolWindow.report.displayControl.status.resolved", resolvedCountLabel, "RESOLVED");
        unresolvedCountLabel = createCountLabel("0");
        addControlItemPanel("toolWindow.report.displayControl.status.unresolved", unresolvedCountLabel, "UNRESOLVED");

        UIUtils.setBackgroundRecursively(this, UIUtils.backgroundColor());
    }

    public void refresh() {
        ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
        vulnerabilityCountLabel.setText(String.valueOf(problemCacheService.getVulnerabilityCount()));
        bugCountLabel.setText(String.valueOf(problemCacheService.getBugCount()));
        codeSmellCountLabel.setText(String.valueOf(problemCacheService.getCodeSmellCount()));
        duplicatedBlocksCountLabel.setText(String.valueOf(problemCacheService.getDuplicatedBlocksCount()));
        securityHotSpotCountLabel.setText(String.valueOf(problemCacheService.getSecurityHotSpotCount()));
        updatedFilesCountLabel.setText(String.valueOf(problemCacheService.getUpdatedFilesIssueCount()));
        notUpdatedFilesCountLabel.setText(String.valueOf(problemCacheService.getNotUpdatedFilesIssueCount()));
        resolvedCountLabel.setText(String.valueOf(problemCacheService.getFixedIssueCount()));
        unresolvedCountLabel.setText(String.valueOf(problemCacheService.getUnresolvedIssueCount()));
    }

    public void reset() {
        Arrays.asList(
                vulnerabilityCountLabel,
                bugCountLabel,
                codeSmellCountLabel,
                duplicatedBlocksCountLabel,
                securityHotSpotCountLabel,
                updatedFilesCountLabel,
                notUpdatedFilesCountLabel,
                resolvedCountLabel,
                unresolvedCountLabel).forEach(label -> {
            label.setText("0");
            cancelHighlight((JBPanel)(label.getParent()));
        });
    }

    private MouseAdapter createMouseAdapter(JBPanel target, String filter) {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Set<String> filters = ProblemCacheService.getInstance(project).getFilters();
                if (!filters.contains(filter)) {
                    filters.add(filter);
                    highlight(target);
                } else {
                    cancelHighlight(target);
                    filters.remove(filter);
                }
                MessageBusManager.publishIssueFilter(project);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                highlight(target);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Set<String> filters = ProblemCacheService.getInstance(project).getFilters();
                if (filters.contains(filter)) {
                    return;
                }
                cancelHighlight(target);
            }
        };
        return mouseAdapter;
    }

    private void highlight(JBPanel target) {
        UIUtils.setBackgroundRecursively(target, UIUtils.highlightBackgroundColor());
        target.setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.highlightBorderColor()), JBUI.Borders.empty(3, 5)));
    }

    private void cancelHighlight(JBPanel target) {
        UIUtils.setBackgroundRecursively(target);
        target.setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(3, 5)));
    }

    private void addControlItemPanel(String resourceKey, JBLabel countLabel, String filter) {
        JBPanel panel = new JBPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(JBUI.Borders.customLine(UIUtils.borderColor()), JBUI.Borders.empty(3, 5)));

        panel.add(new JBLabel(ResourcesLoader.getString(resourceKey)), BorderLayout.CENTER);
        panel.add(countLabel, BorderLayout.EAST);

        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(createMouseAdapter(panel, filter));

        add(panel);

        add(Box.createVerticalStrut(5));
    }

    private void addTitleLabel(String title) {
        JBLabel label = new JBLabel(title);
        Font font = new Font(label.getFont().getFontName(), Font.BOLD, 13);
        label.setFont(font);
        add(label);
        add(Box.createVerticalStrut(5));
    }

    private JBLabel createCountLabel(String text) {
        JBLabel label = new JBLabel(text);
        label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 12));
        return label;
    }
}
