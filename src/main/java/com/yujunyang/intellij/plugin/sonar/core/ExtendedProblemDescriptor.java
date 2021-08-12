package com.yujunyang.intellij.plugin.sonar.core;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.QuickFix;
import com.intellij.lang.annotation.ProblemGroup;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedProblemDescriptor implements ProblemDescriptor, ProblemGroup {
    @NotNull
    private final PsiFile psiFile;
    private PsiElement psiElement;
    private final int lineStart;
    private final int lineEnd;
    private final AbstractIssue issue;
    private final DuplicatedBlocksIssue.Block block;


    public ExtendedProblemDescriptor(
            @NotNull final Issue issue) {
        this.issue = issue;
        this.block = null;
        this.psiFile = issue.getPsiFile();
        this.lineStart = issue.getLineStart();
        this.lineEnd = issue.getLineEnd();
    }

    public ExtendedProblemDescriptor(
            @NotNull final DuplicatedBlocksIssue issue,
            @NotNull final DuplicatedBlocksIssue.Block block) {
        this.issue = issue;
        this.block = block;
        this.psiFile = issue.getPsiFile();
        this.lineStart = block.getLineStart();
        this.lineEnd = block.getLineEnd();

    }

    @Override
    public PsiElement getPsiElement() {
//        if (psiElement != null) {
//            return psiElement;
//        }
//        if (lineStart < 0 || lineStart == 0 && lineEnd == 1) {
//            psiElement = IdeaUtils.findPsiElement(psiFile, bug.getInstance(), psiFile.getProject());
//        } else {
//            psiElement = IdeaUtilImpl.getElementAtLine(psiFile, lineStart);
//        }
//        final MethodAnnotation primaryMethod = BugInstanceUtil.getPrimaryMethod(bug.getInstance());
//        if (primaryMethod != null && DebuggerUtilsEx.isLambdaName(primaryMethod.getMethodName())) {
//            psiElement = IdeaUtilImpl.findOnlyLambdaExpressionOrPsiElement(psiElement);
//        }
//        return psiElement;
        return null;
    }

    @Override
    public PsiElement getStartElement() {
        return null;
    }

    @Override
    public PsiElement getEndElement() {
        return null;
    }

    @Override
    public TextRange getTextRangeInElement() {
        return null;
    }

    @Override
    public int getLineNumber() {
        return 0;
    }

    @Override
    public @NotNull ProblemHighlightType getHighlightType() {
        return null;
    }

    @Override
    public boolean isAfterEndOfLine() {
        return false;
    }

    @Override
    public void setTextAttributes(TextAttributesKey key) {

    }

    @Override
    public @Nullable ProblemGroup getProblemGroup() {
        return this;
    }

    @Override
    public void setProblemGroup(@Nullable ProblemGroup problemGroup) {

    }

    @Override
    public boolean showTooltip() {
        return true;
    }

    @Override
    public @NotNull String getDescriptionTemplate() {
        return null;
    }

    @Override
    public @Nullable QuickFix[] getFixes() {
        return new QuickFix[0];
    }

    @Override
    public @Nullable String getProblemName() {
        return "SonarAnalyzer";
    }
}
