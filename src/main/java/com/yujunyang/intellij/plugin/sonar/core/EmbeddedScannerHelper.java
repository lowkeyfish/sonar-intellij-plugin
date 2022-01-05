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

package com.yujunyang.intellij.plugin.sonar.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.common.SettingsUtils;
import com.yujunyang.intellij.plugin.sonar.config.SonarQubeSettings;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;
import org.jetbrains.annotations.NotNull;
import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.LogOutput;

public final class EmbeddedScannerHelper {
    public static final List<String> EXCLUDED_PROPERTIES = Arrays.asList(
            "sonar.host.url",
            "sonar.login",
            "sonar.password",
            "sonar.projectBaseDir",
            "sonar.working.directory",
            "sonar.java.source",
            "sonar.tests",
            "sonar.sources",
            "sonar.java.libraries",
            "sonar.java.binaries",
            "sonar.sourceEncoding"
    );

    public static Map<String, String> createTaskProperties(Project project, AnalyzeScope analyzeScope) {
        Map<String, String> props = new HashMap<>();
        {
            SonarQubeSettings connection = SettingsUtils.getSonarQubeConnection(project);
            props.put("sonar.host.url", connection.url);
            props.put("sonar.login", connection.token);
            props.put("sonar.projectKey", "SonarAnalyzer:" + project.getName());
            props.put("sonar.projectBaseDir", project.getBasePath());
            props.put("sonar.working.directory", "./.idea/SonarAnalyzer/.scannerwork");
            props.put("sonar.java.source", IdeaUtils.getProjectSdkVersion(project));
            props.put("sonar.tests", "");
            // props.put("sonar.sources", IdeaUtils.getAllSourceRootPath(project));
            props.put("sonar.sources", analyzeScope.getSources());
            props.put("sonar.java.libraries", IdeaUtils.getFullClassPath(project));
            props.put("sonar.java.binaries", analyzeScope.getJavaBinaries());
            props.put("sonar.sourceEncoding", IdeaUtils.getProjectFileEncoding(project));

            Map<String, String> settingsProperties = SettingsUtils.getSonarProperties(project);
            for (Map.Entry<String, String> item : settingsProperties.entrySet()) {
                String propertyName = item.getKey();
                String propertyValue = item.getValue();
                if (!EXCLUDED_PROPERTIES.contains(propertyName)) {
                    if (propertyName.equals("sonar.projectKey") || propertyName.equals("sonar.projectName")) {
                        propertyValue = propertyValue.replaceAll("<projectName>", project.getName());
                    }
                    props.put(propertyName, propertyValue);
                }
            }
        }

        return props;
    }

    public static void startEmbeddedScanner(@NotNull Project project, @NotNull AnalyzeScope analyzeScope, @NotNull LogOutput logOutput) {
        Map<String, String> taskProperties = createTaskProperties(project, analyzeScope);
        EmbeddedScanner scanner = EmbeddedScanner.create("Intellij Sonar plugin", IdeaUtils.getPluginVersion(), logOutput);
        scanner.addGlobalProperties(taskProperties);
        scanner.start();
        scanner.execute(taskProperties);
    }

}
