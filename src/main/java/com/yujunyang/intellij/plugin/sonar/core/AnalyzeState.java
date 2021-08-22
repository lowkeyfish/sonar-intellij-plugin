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

package com.yujunyang.intellij.plugin.sonar.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public enum AnalyzeState {
    Cleared,
    Started,
    Aborting,
    Aborted,
    Finished;

    private static final Map<Project, AnalyzeState> _stateByProject = new ConcurrentHashMap<>();


    public boolean isStarted() {
        return Started.equals(this);
    }


    public boolean isAborting() {
        return Aborting.equals(this);
    }


    public boolean isAborted() {
        return Aborted.equals(this);
    }


    public boolean isFinished() {
        return Finished.equals(this);
    }


    public boolean isIdle() {
        return !isStarted() && !isAborting();
    }


    public static void set(@NotNull final Project project, @NotNull final AnalyzeState state) {
        _stateByProject.put(project, state);
    }


    @NotNull
    public static AnalyzeState get(@NotNull final Project project) {
        AnalyzeState ret = _stateByProject.get(project);
        if (ret == null) {
            ret = Cleared;
        }
        return ret;
    }


    public static void dispose(@NotNull final Project project) {
        _stateByProject.remove(project);
    }
}
