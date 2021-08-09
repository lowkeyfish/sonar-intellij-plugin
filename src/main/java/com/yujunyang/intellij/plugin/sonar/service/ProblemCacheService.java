package com.yujunyang.intellij.plugin.sonar.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.yujunyang.intellij.plugin.sonar.core.ExtendedProblemDescriptor;
import org.jetbrains.annotations.NotNull;

public class ProblemCacheService {
    private Project project;

    private final ConcurrentMap<PsiFile, List<ExtendedProblemDescriptor>> issues;

    public ProblemCacheService(Project project) {
        this.project = project;
        issues = new ConcurrentHashMap<>();
    }



    public static ProblemCacheService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ProblemCacheService.class);
    }
}
