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

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;

public class Issue extends AbstractIssue {
    public Issue(
            PsiFile psiFile,
            String ruleRepository,
            String ruleKey,
            String msg,
            String severity,
            int lineStart,
            int lineEnd,
            TextRange textRange,
            String type,
            String name,
            String htmlDesc) {
        super(psiFile, ruleRepository, ruleKey, msg, severity, type, name, htmlDesc);
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.textRange = textRange;
    }
}
