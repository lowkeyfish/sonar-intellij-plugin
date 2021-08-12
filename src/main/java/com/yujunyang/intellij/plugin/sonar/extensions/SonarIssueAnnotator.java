package com.yujunyang.intellij.plugin.sonar.extensions;

import java.util.List;
import java.util.Map;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;
import org.jetbrains.annotations.NotNull;

public class SonarIssueAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
//        final Project project = element.getProject();
//
//        if (!AnalyzeState.get(project).isIdle()) {
//            return;
//        }
//
//        final ProblemCacheService cacheService = ProblemCacheService.getInstance(project);
//        final Map<PsiFile, List<AbstractIssue>> issues = cacheService.getIssues();
//
//        final PsiFile psiFile = element.getContainingFile();
//        if (issues.containsKey(psiFile)) {
//            addAnnotation(element, issues.get(psiFile), holder);
//        }
    }


    private static void addAnnotation(
            @NotNull PsiElement element,
            List<AbstractIssue> abstractIssues,
            @NotNull AnnotationHolder holder) {
        Document document = PsiDocumentManager.getInstance(element.getProject()).getDocument(element.getContainingFile());



    }

}
