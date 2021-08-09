package com.yujunyang.intellij.plugin.sonar.core;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;

public class Issue {
    private PsiFile psiFile;
    private String ruleRepository;
    private String ruleKey;
    private String msg;
    private String severity;
    private int lineStart;
    private int lineEnd;
    private TextRange textRange;
    private String type;

    public Issue(
            PsiFile psiFile,
            String ruleRepository,
            String ruleKey,
            String msg,
            String severity,
            int lineStart,
            int lineEnd,
            TextRange textRange,
            String type) {
        this.psiFile = psiFile;
        this.ruleRepository = ruleRepository;
        this.ruleKey = ruleKey;
        this.msg = msg;
        this.severity = severity;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.textRange = textRange;
        this.type = type;
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

    public int getLineStart() {
        return lineStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public TextRange getTextRange() {
        return textRange;
    }

    public String getType() {
        return type;
    }
}
