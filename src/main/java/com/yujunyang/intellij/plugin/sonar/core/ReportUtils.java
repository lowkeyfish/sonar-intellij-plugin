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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import com.intellij.openapi.util.Computable;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import org.apache.commons.io.FileUtils;

public final class ReportUtils {
    private static final String ORIGINAL_REPORT_DIR = ".idea/SonarAnalyzer/.scannerwork/scanner-report";
    private static final String TARGET_REPORT_DIR = ".idea/SonarAnalyzer/.scannerwork/scanner-report-copy";

    public static void copyReportDir(Project project) {
        String projectPath = project.getBasePath();
        Path originalPath = Paths.get(projectPath, ORIGINAL_REPORT_DIR);
        Path targetPath = Paths.get(projectPath, TARGET_REPORT_DIR);
        try {
            FileUtils.copyDirectory(new File(originalPath.toUri()), new File(targetPath.toUri()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteReportDir(Project project) {
        String projectPath = IdeaUtils.getProjectPath(project).getAbsolutePath();
        Path targetPath = Paths.get(projectPath, TARGET_REPORT_DIR);
        if (Files.exists(targetPath)) {
            try {
                FileUtils.deleteDirectory(targetPath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Report createReport(Project project) {
        return ApplicationManager.getApplication().runReadAction((Computable<Report>) () -> {
            String projectPath = project.getBasePath();
            Path reportDirPath = Paths.get(projectPath, TARGET_REPORT_DIR);
            return new Report(project, reportDirPath.toFile());
        });
    }
}
