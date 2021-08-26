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

import javax.swing.JComponent;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
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
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
