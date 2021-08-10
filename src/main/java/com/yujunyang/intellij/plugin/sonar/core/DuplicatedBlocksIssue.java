package com.yujunyang.intellij.plugin.sonar.core;

import java.util.ArrayList;
import java.util.List;

import com.intellij.psi.PsiFile;

public class DuplicatedBlocksIssue extends AbstractIssue {
    private List<Block> blocks;

    public DuplicatedBlocksIssue(
            PsiFile psiFile,
            String ruleRepository,
            String ruleKey,
            String severity,
            String type,
            String name,
            String htmlDesc) {
        super(psiFile, ruleRepository, ruleKey, "", severity, type, name, htmlDesc);
        this.blocks = new ArrayList<>();
    }

    @Override
    public String getMsg() {
        return String.format("%s duplicated blocks of code must be removed.", blocks.size());
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int getBlockCount() {
        return blocks.size();
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public static class Block {
        private int startLine;
        private int endLine;
        private List<Duplicate> duplicates;

        public Block(int startLine, int endLine) {
            this.startLine = startLine;
            this.endLine = endLine;
            this.duplicates = new ArrayList<>();
        }

        public int getStartLine() {
            return startLine;
        }

        public int getEndLine() {
            return endLine;
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
