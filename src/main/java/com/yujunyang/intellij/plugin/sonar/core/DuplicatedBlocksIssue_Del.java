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

import java.util.ArrayList;
import java.util.List;

import com.intellij.psi.PsiFile;

public class DuplicatedBlocksIssue_Del extends AbstractIssue {
    private List<Block> blocks;

    public DuplicatedBlocksIssue_Del(
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
        private int lineStart;
        private int lineEnd;
        private List<Duplicate> duplicates;

        public Block(int lineStart, int lineEnd) {
            this.lineStart = lineStart;
            this.lineEnd = lineEnd;
            this.duplicates = new ArrayList<>();
        }

        public int getLineStart() {
            return lineStart;
        }

        public int getLineEnd() {
            return lineEnd;
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
