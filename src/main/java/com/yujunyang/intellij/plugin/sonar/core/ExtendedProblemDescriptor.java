package com.yujunyang.intellij.plugin.sonar.core;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.QuickFix;
import com.intellij.lang.annotation.ProblemGroup;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedProblemDescriptor implements ProblemDescriptor, ProblemGroup {
    @Override
    public PsiElement getPsiElement() {
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
        return null;
    }

    @Override
    public void setProblemGroup(@Nullable ProblemGroup problemGroup) {

    }

    @Override
    public boolean showTooltip() {
        return false;
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
        return null;
    }
}
