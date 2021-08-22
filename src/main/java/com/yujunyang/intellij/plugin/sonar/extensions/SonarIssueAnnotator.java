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
