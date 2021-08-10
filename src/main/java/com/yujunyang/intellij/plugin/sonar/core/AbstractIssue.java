package com.yujunyang.intellij.plugin.sonar.core;

import com.intellij.psi.PsiFile;

public abstract class AbstractIssue {
    protected PsiFile psiFile;
    protected String ruleRepository;
    protected String ruleKey;
    protected String msg;
    protected String severity;
    protected String type;
    protected String name;
    protected String htmlDesc;

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
}
