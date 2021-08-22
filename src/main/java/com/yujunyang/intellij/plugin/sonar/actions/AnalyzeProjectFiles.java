
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
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.Consumer;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.core.SonarScannerStarter;
import org.jetbrains.annotations.NotNull;

public abstract class AnalyzeProjectFiles extends AbstractAnalyzeAction {
    private final boolean includeTests;

    AnalyzeProjectFiles(boolean includeTests) {
        this.includeTests = includeTests;
    }

    @Override
    public void updateImpl(
            @NotNull final AnActionEvent e,
            @NotNull final Project project,
            @NotNull final ToolWindow toolWindow,
            @NotNull final AnalyzeState state
    ) {

        final boolean enable = state.isIdle();

        e.getPresentation().setEnabled(enable);
        e.getPresentation().setVisible(true);
    }


    @Override
    public void analyze(
            @NotNull final AnActionEvent e,
            @NotNull final Project project,
            @NotNull final ToolWindow toolWindow,
            @NotNull final AnalyzeState state
    ) {
        new SonarScannerStarter(project, "对项目[" + project.getName() + "]执行Sonar代码检测") {
            @Override
            protected void createCompileScope(@NotNull CompilerManager compilerManager, @NotNull Consumer<CompileScope> consumer) {
                consumer.consume(compilerManager.createProjectCompileScope(project));
            }
        }.start();
    }
}
