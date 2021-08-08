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
