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
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.yujunyang.intellij.plugin.sonar.config.ProjectSettings;
import com.yujunyang.intellij.plugin.sonar.config.SonarQubeSettings;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import com.yujunyang.intellij.plugin.sonar.extensions.ProjectSettingsConfigurable;

public final class SettingsUtils {
    private SettingsUtils() {}

    public static void showSettingsDialog(Project project) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, ProjectSettingsConfigurable.class);
    }

    @Nullable
    public static SonarQubeSettings getSonarQubeConnection(@Nonnull Project project) {
        WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        Set<SonarQubeSettings> connections = workspaceSettings.sonarQubeConnections;
        if (connections == null || connections.size() == 0) {
            return null;
        }

        ProjectSettings projectSettings = ProjectSettings.getInstance(project);
        String bindConnectionName = projectSettings.getSonarQubeConnectionName();
        SonarQubeSettings ret = connections.stream().filter(n -> n.name.equals(bindConnectionName)).findFirst().orElse(null);
        if (ret != null) {
            return ret;
        }
        return connections.iterator().next();
    }

    public static Map<String, String> getSonarProperties(@Nonnull Project project) {
        Map<String, String> ret = new HashMap<>();

        ProjectSettings projectSettings = ProjectSettings.getInstance(project);
        Map<String, String> properties = projectSettings.sonarProperties;
        for (Map.Entry<String, String> item : properties.entrySet()) {
            ret.put(item.getKey(), item.getValue());
        }

        if (!projectSettings.inheritedFromApplication) {
            return ret;
        }

        WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        Map<String, String> globalProperties = workspaceSettings.sonarProperties;
        for (Map.Entry<String, String> item : globalProperties.entrySet()) {
            if (!ret.containsKey(item.getKey())) {
                ret.put(item.getKey(), item.getValue());
            }
        }
        return ret;
    }
}
