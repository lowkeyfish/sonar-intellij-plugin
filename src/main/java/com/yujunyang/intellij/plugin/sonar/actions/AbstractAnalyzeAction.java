
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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAnalyzeAction extends AbstractAction {

    @Override
    public final void actionPerformedImpl(
            @NotNull final AnActionEvent e,
            @NotNull final Project project,
            @NotNull final ToolWindow toolWindow,
            @NotNull final AnalyzeState state
    ) {

        EventDispatchThreadHelper.checkEDT();

        analyze(
                e,
                project,
                toolWindow,
                state
        );
    }

    public abstract void analyze(
            @NotNull final AnActionEvent e,
            @NotNull final Project project,
            @NotNull final ToolWindow toolWindow,
            @NotNull final AnalyzeState state
    );


}
