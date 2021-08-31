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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.Constants;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.AbstractCollection;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.XCollection;
import org.apache.commons.collections.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "SonarAnalyzer-Workspace", storages = { @Storage("intellij-sonar-plugin.xml") })
public final class WorkspaceSettings implements PersistentStateComponent<WorkspaceSettings> {

    @Tag
    public String sonarHostUrl = "";

    @Tag
    public String sonarToken = "";

    @Tag
    public List<String> languages = Arrays.asList("java", "xml");

    @Tag
    public boolean autoScrollToSource = true;

    @Tag
    public String uiLanguageLocale = "zh";


    @Tag("sonarQubeConnections")
    @AbstractCollection(surroundWithTag = false, elementTag = Constants.SET)
    public Set<SonarQubeSettings> sonarQubeConnections = new HashSet<>();

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

    public WorkspaceSettings() {
    }

    @Nullable
    @Override
    public WorkspaceSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull WorkspaceSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isSonarConfigured() {
        return StringUtil.isNotEmpty(sonarHostUrl);
    }

    public String getSonarHostUrl() {
        if (sonarHostUrl == null) {
            return "";
        }
        return sonarHostUrl;
    }

    public String getSonarToken() {
        if (sonarToken == null) {
            return "";
        }
        return sonarToken;
    }

    public static WorkspaceSettings getInstance() {
        return ServiceManager.getService(WorkspaceSettings.class);
    }
}
