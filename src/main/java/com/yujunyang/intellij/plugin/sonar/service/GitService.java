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

package com.yujunyang.intellij.plugin.sonar.service;

import java.util.List;
import java.util.stream.Collectors;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.ChangeListManagerEx;
import com.intellij.psi.PsiFile;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import git4idea.GitUtil;
import org.jetbrains.annotations.NotNull;

public class GitService {
    private Project project;

    public GitService(Project project) {
        this.project = project;
    }

    public List<PsiFile> getChangedFiles() {
        ChangeListManagerEx changeListManager = (ChangeListManagerEx) ChangeListManager.getInstance(project);
        return changeListManager.getAffectedPaths().stream().
                map(n -> IdeaUtils.getPsiFile(project, n)).
                filter(n -> n != null).
                collect(Collectors.toList());
    }

    public static GitService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, GitService.class);
    }
}
