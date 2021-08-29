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

import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "SonarAnalyzer-Project", storages = { @Storage("intellij-sonar-plugin-project.xml") })
public class ProjectSettings implements PersistentStateComponent<ProjectSettings>  {

    @Tag
    public String sonarQubeConnectionName;

    @Tag
    public boolean inheritedFromApplication = true;

    @Tag("sonarProperties")
    @MapAnnotation(
            surroundWithTag = false,
            surroundValueWithTag = false,
            surroundKeyWithTag = false,
            entryTagName = "property",
            keyAttributeName = "name",
            valueAttributeName = "value"
    )
    public Map<String, String> sonarProperties = new HashMap<>();


    @Nullable
    @Override
    public ProjectSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static ProjectSettings getInstance(Project project) {
        return ServiceManager.getService(project, ProjectSettings.class);
    }
}
