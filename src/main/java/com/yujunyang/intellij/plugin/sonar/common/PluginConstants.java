
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

public final class PluginConstants {
    public static final String PLUGIN_WEBSITE = "https://github.com/lowkeyfish/sonar-intellij-plugin";
//    public static final String PLUGIN_DOWNLOAD_WEBSITE = "https://plugins.jetbrains.com/plugin/index?xmlId=com.yujunyang.intellij.plugin.sonar";
public static final String PLUGIN_DOWNLOAD_WEBSITE = "https://plugins.jetbrains.com/plugin/17542-sonaranalyzer";
    public static final String PLUGIN_ISSUE_TRACKER = "https://github.com/lowkeyfish/sonar-intellij-plugin/issues";
    public static final String PLUGIN_ID = "com.yujunyang.intellij.plugin.sonar";
    public static final String PLUGIN_NAME = "Sonar Intellij Plugin";

    public static final String TOOL_WINDOW_ID = "SonarAnalyzer";
    public static final String ACTION_GROUP_LEFT = "SonarAnalyzer.ToolBarActions.left";

    public static final String FILE_SEPARATOR = File.separator;

    private PluginConstants() {
    }
}
