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

package com.yujunyang.intellij.plugin.sonar.config;

import java.util.Objects;

import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.NotNull;

@Tag(value = "sonarQubeConnection")
public class SonarQubeSettings implements Comparable<SonarQubeSettings> { // 必须实现Comparable接口否则无法存储
    @Tag
    public String name;

    @Tag
    public String url;

    @Tag
    public String token;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SonarQubeSettings that = (SonarQubeSettings) o;

        return Objects.equals(name, that.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        return result;
    }

    @Override
    public int compareTo(@NotNull SonarQubeSettings o) {
        return name.compareTo(o.name);
    }
}
