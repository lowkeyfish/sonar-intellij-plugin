package com.yujunyang.intellij.plugin.sonar.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.ExtendedProblemDescriptor;
import org.jetbrains.annotations.NotNull;

public class ProblemCacheService {
    private Project project;

    private ConcurrentMap<PsiFile, List<AbstractIssue>> issues;
    private int bugCount;
    private int codeSmellCount;
    private int vulnerabilityCount;
    private int duplicatedBlocksCount;

    public ProblemCacheService(Project project) {
        this.project = project;
        issues = new ConcurrentHashMap<>();
        bugCount = 0;
        codeSmellCount = 0;
        vulnerabilityCount = 0;
        duplicatedBlocksCount = 0;
    }

    public ConcurrentMap<PsiFile, List<AbstractIssue>> getIssues() {
        return issues;
    }

    public void setIssues(ConcurrentMap<PsiFile, List<AbstractIssue>> issues) {
        this.issues = issues;
    }

    public int getBugCount() {
        return bugCount;
    }

    public int getCodeSmellCount() {
        return codeSmellCount;
    }

    public int getVulnerabilityCount() {
        return vulnerabilityCount;
    }

    public int getDuplicatedBlocksCount() {
        return duplicatedBlocksCount;
    }

    public void setStats(int bugCount, int codeSmellCount, int vulnerabilityCount, int duplicatedBlocksCount) {
        this.bugCount = bugCount;
        this.codeSmellCount = codeSmellCount;
        this.vulnerabilityCount = vulnerabilityCount;
        this.duplicatedBlocksCount = duplicatedBlocksCount;
    }

    public static ProblemCacheService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ProblemCacheService.class);
    }
}
