package com.yujunyang.intellij.plugin.sonar.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class LineIssuesGroup {
    private PsiFile psiFile;
    private Map<PsiElement, List<AbstractIssue>> errorAnnotations;
    private List<AbstractIssue> issues;

    public LineIssuesGroup(
            @NotNull PsiFile psiFile) {
        this.psiFile = psiFile;
        this.errorAnnotations = new HashMap<>();
        this.issues = new ArrayList<>();
    }

    public void addIssue(AbstractIssue issue) {
        if (issue instanceof Issue) {
            addIssue((Issue)issue);
        } else if (issue instanceof DuplicatedBlocksIssue) {
            addIssue((DuplicatedBlocksIssue)issue);
        }
    }

    private void addIssue(Issue issue) {

    }

    private void addIssue(DuplicatedBlocksIssue issue) {

    }

}
