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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import org.jetbrains.annotations.NotNull;

public class ProblemCacheService {
    private Project project;

    private boolean initialized = false;
    private ConcurrentMap<PsiFile, List<AbstractIssue>> issues;
    private int bugCount;
    private int codeSmellCount;
    private int vulnerabilityCount;
    private int duplicatedBlocksCount;
    private int securityHotSpotCount;

    private CopyOnWriteArraySet<String> profileLanguages;
    private CopyOnWriteArraySet<String> ignoreRules;
    private int ignoreIssueCount;

    public ProblemCacheService(Project project) {
        this.project = project;
        issues = new ConcurrentHashMap<>();
        bugCount = 0;
        codeSmellCount = 0;
        vulnerabilityCount = 0;
        duplicatedBlocksCount = 0;
        securityHotSpotCount = 0;

        profileLanguages = new CopyOnWriteArraySet<>();
        ignoreRules = new CopyOnWriteArraySet<>();
        ignoreIssueCount = 0;
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

    public int getSecurityHotSpotCount() {
        return securityHotSpotCount;
    }

    public CopyOnWriteArraySet<String> getProfileLanguages() {
        return profileLanguages;
    }

    public CopyOnWriteArraySet<String> getIgnoreRules() {
        return ignoreRules;
    }

    public int getIgnoreIssueCount() {
        return ignoreIssueCount;
    }

    public void setStats(int bugCount, int codeSmellCount, int vulnerabilityCount, int duplicatedBlocksCount, int securityHotSpotCount) {
        initialized = true;
        this.bugCount = bugCount;
        this.codeSmellCount = codeSmellCount;
        this.vulnerabilityCount = vulnerabilityCount;
        this.duplicatedBlocksCount = duplicatedBlocksCount;
        this.securityHotSpotCount = securityHotSpotCount;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void reset() {
        initialized = false;
        issues.clear();
        bugCount = 0;
        codeSmellCount = 0;
        vulnerabilityCount = 0;
        duplicatedBlocksCount = 0;
        securityHotSpotCount = 0;

        profileLanguages.clear();
        ignoreRules.clear();
        ignoreIssueCount = 0;
    }

    public int getUpdatedFilesIssueCount() {
        List<PsiFile> changedFiles = GitService.getInstance(project).getChangedFiles();
        int count = 0;
        for (Map.Entry<PsiFile, List<AbstractIssue>> entry : issues.entrySet()) {
            PsiFile psiFile = entry.getKey();
            List<AbstractIssue> issueList = entry.getValue();
            if (changedFiles.contains(psiFile)) {
                count += issueList.size();
            }
        }
        return count;
    }

    public int getNotUpdatedFilesIssueCount() {
        List<PsiFile> changedFiles = GitService.getInstance(project).getChangedFiles();
        int count = 0;
        for (Map.Entry<PsiFile, List<AbstractIssue>> entry : issues.entrySet()) {
            PsiFile psiFile = entry.getKey();
            List<AbstractIssue> issueList = entry.getValue();
            if (!changedFiles.contains(psiFile)) {
                count += issueList.size();
            }
        }
        return count;
    }

    public int getFixedIssueCount() {
        int count = 0;
        for (Map.Entry<PsiFile, List<AbstractIssue>> entry : issues.entrySet()) {
            List<AbstractIssue> issueList = entry.getValue();
            for (AbstractIssue n : issueList) {
                if (n.isFixed()) {
                    count += 1;
                }
            }
        }
        return count;
    }

    public int getUnresolvedIssueCount() {
        return bugCount + codeSmellCount + vulnerabilityCount + securityHotSpotCount + duplicatedBlocksCount - getFixedIssueCount();
    }

    public static ProblemCacheService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ProblemCacheService.class);
    }
}
