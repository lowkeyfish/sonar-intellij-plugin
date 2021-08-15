package com.yujunyang.intellij.plugin.sonar.extensions;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue2;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;
import org.jetbrains.annotations.NotNull;

public class SonarIssueExternalAnnotator extends ExternalAnnotator<SonarIssueExternalAnnotator.AnnotationContext, SonarIssueExternalAnnotator.AnnotationContext> {

    @Override
    public void apply(@NotNull PsiFile file, AnnotationContext annotationResult, @NotNull AnnotationHolder holder) {
        final Project project = file.getProject();

        if (!AnalyzeState.get(project).isIdle()) {
            return;
        }

        final ProblemCacheService cacheService = ProblemCacheService.getInstance(project);
        final Map<PsiFile, List<AbstractIssue>> issues = cacheService.getIssues();

        if (issues.containsKey(file)) {
            addAnnotation(project, file, issues.get(file), holder);
        }

    }

    @Override
    public AnnotationContext collectInformation(@NotNull PsiFile file, @NotNull Editor editor, boolean hasErrors) {
        return collectInformation(file);
    }

    @Override
    public AnnotationContext collectInformation(@NotNull PsiFile file) {
        return new AnnotationContext();
    }

    @Override
    @Nullable
    public AnnotationContext doAnnotate(AnnotationContext collectedInfo) {
        return collectedInfo;
    }

    private static void addAnnotation(Project project, PsiFile psiFile, List<AbstractIssue> issues, AnnotationHolder holder) {
        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
        issues.forEach(n -> {
            if (n instanceof Issue) {
                Issue issue = (Issue)n;
                Annotation annotation = holder.createErrorAnnotation(issue.getTextRange(), msg(issue));
            } else if (n instanceof DuplicatedBlocksIssue2) {
                DuplicatedBlocksIssue2 issue = (DuplicatedBlocksIssue2)n;
                Annotation annotation = holder.createErrorAnnotation(issue.getTextRange(), msg(issue));
            }
        });

    }

    private static String msg(Issue issue) {
        return String.format("SonarAnalyzer: %s", issue.getMsg());
    }

    private static String msg(DuplicatedBlocksIssue2 issue) {
        return String.format("SonarAnalyzer: 行[%s-%s]与其他[%s]个代码块重复", issue.getLineStart(), issue.getLineEnd(), issue.getDuplicateCount());
    }



    public static class AnnotationContext {
    }
}
