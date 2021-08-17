package com.yujunyang.intellij.plugin.sonar.core;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;

public class DuplicatedBlocksIssue extends AbstractIssue {
    private List<Duplicate> duplicates;

    public DuplicatedBlocksIssue(
            PsiFile psiFile,
            String ruleRepository,
            String ruleKey,
            String severity,
            String type,
            String name,
            String htmlDesc,
            int lineStart,
            int lineEnd) {
        super(psiFile, ruleRepository, ruleKey, "", severity, type, name, htmlDesc);
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.textRange = new TextRange(0, 0);
        this.duplicates = new ArrayList<>();
    }

    public List<Duplicate> getDuplicates() {
        return duplicates;
    }

    public int getDuplicateCount() {
        return duplicates.size();
    }

    public void addDuplicate(Duplicate duplicate) {
        duplicates.add(duplicate);
    }

    public void addDuplicates(List<Duplicate> duplicates) {
        this.duplicates.addAll(duplicates);
    }


    public static class Duplicate {
        private String path;
        private int startLine;
        private int endLine;

        public Duplicate(String path, int startLine, int endLine) {
            this.path = path;
            this.startLine = startLine;
            this.endLine = endLine;
        }

        public String getPath() {
            return path;
        }

        public int getStartLine() {
            return startLine;
        }

        public int getEndLine() {
            return endLine;
        }
    }
}
