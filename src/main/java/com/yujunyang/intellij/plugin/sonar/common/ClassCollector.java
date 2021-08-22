
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


import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public final class ClassCollector extends AbstractClassAdder {

    private final Map<String, PsiElement> _classes;


    public ClassCollector(@NotNull final Project project) {
        super(project);
        _classes = new HashMap<>();
    }


    @Override
    void put(@NotNull final String fqp, @NotNull final PsiElement element) {
        _classes.put(fqp + CLASS_FILE_SUFFIX, element);
    }


    @NotNull
    public Map<String, PsiElement> getClasses() {
        return _classes;
    }
}