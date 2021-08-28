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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JComponent;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.yujunyang.intellij.plugin.sonar.config.SonarQubeSettings;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import com.yujunyang.intellij.plugin.sonar.gui.settings.ApplicationSettingsPanel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class ApplicationSettingsConfigurable implements Configurable {
    ApplicationSettingsPanel applicationSettingsPanel;

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "SonarAnalyzer";
    }

    @Override
    public @Nullable JComponent createComponent() {
        applicationSettingsPanel = new ApplicationSettingsPanel();
        return applicationSettingsPanel;
    }

    @Override
    public boolean isModified() {
        WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        Set<SonarQubeSettings> existConnections = workspaceSettings.sonarQubeConnections;
        Map<String, String> existProperties = workspaceSettings.sonarProperties;
        List<SonarQubeSettings> connections = applicationSettingsPanel.getConnections();
        Map<String, String> properties = applicationSettingsPanel.getProperties();

        if (existConnections.size() != connections.size()) {
            return true;
        }

        for (SonarQubeSettings n : existConnections) {
            if (!connections.stream().anyMatch(m -> m.name.equals(n.name) && m.url.equals(n.url) && m.token.equals(n.token))) {
                return true;
            }
        }

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
        WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        List<SonarQubeSettings> connections = applicationSettingsPanel.getConnections();
        Map<String, String> properties = applicationSettingsPanel.getProperties();
        workspaceSettings.sonarQubeConnections = connections.stream().collect(Collectors.toSet());
        workspaceSettings.sonarProperties = properties;
    }

    @Override
    public void reset() {
        applicationSettingsPanel.reset();
    }
}
