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

package com.yujunyang.intellij.plugin.sonar.core;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiWhiteSpace;
import com.siyeh.ig.ui.UiUtils;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;

public abstract class AbstractIssue {
    protected PsiFile psiFile;
    protected String ruleRepository;
    protected String ruleKey;
    protected String msg;
    protected String severity;
    protected String type;
    protected String name;
    protected String htmlDesc;

    protected PsiElement psiElement;
    protected int lineStart;
    protected int lineEnd;
    protected TextRange textRange;

    public AbstractIssue(
            PsiFile psiFile,
            String ruleRepository,
            String ruleKey,
            String msg,
            String severity,
            String type,
            String name,
            String htmlDesc) {
        this.psiFile = psiFile;
        this.ruleRepository = ruleRepository;
        this.ruleKey = ruleKey;
        this.msg = msg;
        this.severity = severity;
        this.type = type;
        this.name = name;
        this.htmlDesc = htmlDesc;
    }

    public PsiFile getPsiFile() {
        return psiFile;
    }

    public String getRuleRepository() {
        return ruleRepository;
    }

    public String getRuleKey() {
        return ruleKey;
    }

    public String getMsg() {
        return msg;
    }

    public String getSeverity() {
        return severity;
    }

    public String getType() {
        return type;
    }

    public String getTypeDesc() {
        return UIUtils.typeInfo(type).first;
    }

    public String getName() {
        return name;
    }

    public String getHtmlDesc() {
        return htmlDesc;
    }

    public int getLineStart() {
        return lineStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public TextRange getTextRange() {
        Document document = PsiDocumentManager.getInstance(psiFile.getProject()).getDocument(psiFile);

        PsiElement element = getPsiElement();
        TextRange textRange = element.getTextRange();
        int elementLineStart = document.getLineNumber(textRange.getStartOffset());
        int elementLineEnd = document.getLineNumber(textRange.getEndOffset());

        int elementStartOffset = textRange.getStartOffset();
        int startOffset = elementStartOffset;
        int lineStartOffset = document.getLineStartOffset(elementLineStart);
        PsiElement lineFirstElement = getPsiElement(lineStartOffset);
        int lineFirstElementStartOffset = lineFirstElement.getTextRange().getStartOffset();
        if (lineFirstElementStartOffset < elementStartOffset) {
            startOffset = lineFirstElementStartOffset;
        }

        int elementEndOffset = textRange.getEndOffset();
        int endOffset = elementEndOffset;
        int lineEndOffset = document.getLineEndOffset(elementLineStart);
        if (lineEndOffset > elementEndOffset) {
            endOffset = lineEndOffset;
        } else if (!(element instanceof PsiDeclarationStatement)) {
            endOffset = lineEndOffset;
        }

        return new TextRange(startOffset, endOffset);
    }


    public PsiElement getTextRangePsiElement() {
        TextRange textRange = getTextRange();
        return getPsiElement(textRange.getStartOffset());
    }

    protected PsiElement getPsiElement() {
        if (psiElement != null) {
            return psiElement;
        }

        Document document = PsiDocumentManager.getInstance(psiFile.getProject()).getDocument(psiFile);

        int line = lineStart - 1;
        // Sonar可能会对整个文件提示问题，这种情况下lineStart=0，需要额外处理
        if (line < 0) {
            line = 0;
        }
        int offset = textRange.getStartOffset();

        int psiElementOffset = document.getLineStartOffset(line) + offset;
        PsiElement findPsiElement = psiFile.findElementAt(psiElementOffset);

        if (findPsiElement instanceof PsiWhiteSpace) {
            psiElement = findPsiElement.getNextSibling();
        } else {
            psiElement = findPsiElement;
        }

        return psiElement;
    }

    protected PsiElement getPsiElement(int offset) {
        PsiElement findPsiElement = psiFile.findElementAt(offset);
        if (findPsiElement instanceof PsiWhiteSpace) {
            PsiElement nextSibling = findPsiElement.getNextSibling();
            if (nextSibling != null) {
                return nextSibling;
            }
            return findPsiElement.getParent().getNextSibling();
        } else {
            return findPsiElement;
        }
    }
}
