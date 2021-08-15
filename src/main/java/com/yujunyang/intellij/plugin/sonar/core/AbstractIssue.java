package com.yujunyang.intellij.plugin.sonar.core;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiWhiteSpace;

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
        } else if (element instanceof PsiMethod) {
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
            return findPsiElement.getNextSibling();
        } else {
            return findPsiElement;
        }
    }
}
