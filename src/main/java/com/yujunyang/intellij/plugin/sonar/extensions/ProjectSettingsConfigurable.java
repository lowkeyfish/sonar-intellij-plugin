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

package com.yujunyang.intellij.plugin.sonar.extensions;

import java.util.Map;
import java.util.Objects;
import javax.swing.JComponent;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.yujunyang.intellij.plugin.sonar.config.ProjectSettings;
import com.yujunyang.intellij.plugin.sonar.gui.settings.ProjectSettingsPanel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class ProjectSettingsConfigurable implements Configurable {
    Project project;
    ProjectSettingsPanel projectSettingsPanel;

    public ProjectSettingsConfigurable(Project project) {
        this.project = project;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Project Settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        projectSettingsPanel = new ProjectSettingsPanel(project);
        return projectSettingsPanel;
    }

    @Override
    public boolean isModified() {
        ProjectSettings projectSettings = ProjectSettings.getInstance(project);

        if (!Objects.equals(projectSettings.getSonarQubeConnectionName(), projectSettingsPanel.getConnectionName())) {
            return true;
        }

        if (projectSettings.inheritedFromApplication != projectSettingsPanel.isInheritedFromApplication()) {
            return true;
        }

        Map<String, String> existProperties = projectSettings.sonarProperties;
        Map<String, String> properties = projectSettingsPanel.getProperties();
        if (existProperties.size() != properties.size()) {
            return true;
        }
        for (Map.Entry<String, String> n : existProperties.entrySet()) {
            if (!(properties.containsKey(n.getKey()) && properties.get(n.getKey()).equals(n.getValue()))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        ProjectSettings projectSettings = ProjectSettings.getInstance(project);
        projectSettings.sonarQubeConnectionName = projectSettingsPanel.getConnectionName();
        projectSettings.inheritedFromApplication = projectSettingsPanel.isInheritedFromApplication();
        projectSettings.sonarProperties = projectSettingsPanel.getProperties();
    }

    @Override
    public void reset() {
        projectSettingsPanel.reset();
    }


}
