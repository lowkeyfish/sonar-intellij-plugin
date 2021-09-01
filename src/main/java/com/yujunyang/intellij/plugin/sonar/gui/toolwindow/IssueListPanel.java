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

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBPanel;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.gui.layout.SampleVerticalScrollLayout;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;

public class IssueListPanel extends JBPanel {
    private Project project;

    public IssueListPanel(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        setLayout(new SampleVerticalScrollLayout());
    }

    public void refresh() {
        removeAll();
        ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
        ConcurrentMap<PsiFile, List<AbstractIssue>> issues = problemCacheService.getIssues();
        issues.entrySet().stream().
                sorted(Comparator.comparing(o -> IdeaUtils.getPath(o.getKey()))).
                collect(Collectors.toList()).
                forEach(n -> add(new IssueFileGroupPanel(n.getKey(), n.getValue())));
//        for (Map.Entry<PsiFile, List<AbstractIssue>> fileIssues : issues.entrySet()) {
//            add(new IssueFileGroupPanel(fileIssues.getKey(), fileIssues.getValue()));
//        }
    }

    public void reset() {
        removeAll();
    }
}
