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

package com.yujunyang.intellij.plugin.sonar.common;

import java.io.File;
import java.util.Set;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class ClassAdder extends AbstractClassAdder {
    private final Set<String> classFilePaths;

    public ClassAdder(@NotNull Project project, Set<String> classFilePaths) {
        super(project);
        this.classFilePaths = classFilePaths;
    }

    @Override
    void put(@NotNull String fqp, @NotNull PsiElement element) {
        final String fqn = fqp + CLASS_FILE_SUFFIX;
        if (new File(fqn).exists()) {
            classFilePaths.add(fqn);
        }
    }
}
