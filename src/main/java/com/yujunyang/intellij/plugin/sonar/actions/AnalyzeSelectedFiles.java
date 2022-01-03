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

import java.util.List;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.Consumer;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeScope;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.core.SonarScannerStarter;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;
import org.jetbrains.annotations.NotNull;

public class AnalyzeSelectedFiles extends AbstractAnalyzeAction {
    @Override
    public void analyze(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ToolWindow toolWindow,
            @NotNull AnalyzeState state) {
        new SonarScannerStarter(project, ResourcesLoader.getString("task.analysis.title", project.getName())) {
            @Override
            protected void createCompileScope(@NotNull CompilerManager compilerManager, @NotNull Consumer<CompileScope> consumer) {
                consumer.consume(compilerManager.createProjectCompileScope(project));
            }

            @Override
            protected AnalyzeScope createAnalyzeScope() {
                return ApplicationManager.getApplication().runReadAction((Computable<AnalyzeScope>) () ->
                        new AnalyzeScope(project, AnalyzeScope.ScopeType.SELECTED_FILES, IdeaUtils.getValidSelectedFiles(project, e.getDataContext())));
            }
        }.start();
    }

    @Override
    public void updateImpl(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ToolWindow toolWindow,
            @NotNull AnalyzeState state) {
        final List<VirtualFile> selectedFiles = IdeaUtils.getValidSelectedFiles(project, e.getDataContext());

        boolean enable = false;
        if (state.isIdle()) {
            enable = selectedFiles != null &&
                    selectedFiles.size() > 0;
        }

        e.getPresentation().setEnabled(enable);
        e.getPresentation().setVisible(true);
        setText(e);
    }

    private void setText(AnActionEvent e) {
        if (PlatformDataKeys.EDITOR.getData(e.getDataContext()) == null) {
            e.getPresentation().setText(ResourcesLoader.getString("action.analyze.selectedFiles"));
        } else {
            e.getPresentation().setText(ResourcesLoader.getString("action.analyze.currentFile"));
        }
    }


}
