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

package com.yujunyang.intellij.plugin.sonar.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import org.jetbrains.annotations.NotNull;

public class AnalyzePackageFiles extends AbstractAnalyzeAction {
    @Override
    public void analyze(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ToolWindow toolWindow,
            @NotNull AnalyzeState state) {

    }

    @Override
    public void updateImpl(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ToolWindow toolWindow,
            @NotNull AnalyzeState state) {
        VirtualFile directory = getDirectory(e, project);
        boolean enable = state.isIdle() && directory != null && ModuleUtilCore.findModuleForFile(directory, project) != null;
        e.getPresentation().setEnabled(enable);
        e.getPresentation().setVisible(true);
    }

    private static VirtualFile getDirectory(AnActionEvent e, Project project) {
        final VirtualFile[] selectedFiles = IdeaUtils.getVirtualFiles(e.getDataContext());
        if (selectedFiles == null || selectedFiles.length != 1) {
            return null;
        }
        VirtualFile directory = selectedFiles[0];
        if (!directory.isDirectory()) {
            directory = directory.getParent();
            if (directory == null) {
                return null;
            }
            if (!directory.isDirectory()) {
                return null;
            }
        }
        final PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(directory);
        if (psiDirectory == null) {
            return null;
        }
        if (!PsiDirectoryFactory.getInstance(project).isPackage(psiDirectory)) {
            return null;
        }
        return directory;
    }
}
