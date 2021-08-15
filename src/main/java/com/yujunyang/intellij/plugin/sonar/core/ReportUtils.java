package com.yujunyang.intellij.plugin.sonar.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.intellij.openapi.project.Project;

import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import org.apache.commons.io.FileUtils;

public final class ReportUtils {
    private static final String ORIGINAL_REPORT_DIR = "target/.scannerwork/scanner-report";
    private static final String TARGET_REPORT_DIR = "target/.scannerwork/scanner-report-copy";

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

    public static Report2 createReport(Project project) {
        String projectPath = project.getBasePath();
        Path reportDirPath = Paths.get(projectPath, TARGET_REPORT_DIR);
        return new Report2(project, reportDirPath.toFile());
    }
}
