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

import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.openapi.project.Project;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.common.LogUtils;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;
import org.sonarsource.scanner.api.LogOutput;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class LogOutputImpl implements LogOutput {
    private static final String PROFILE_LANGUAGE_REGEX = "Quality\\s*profile\\s*for\\s*(.+?):";
    private static final Pattern PROFILE_LANGUAGE_PATTERN = Pattern.compile(PROFILE_LANGUAGE_REGEX, CASE_INSENSITIVE);

    private Project project;


    public LogOutputImpl(Project project) {
        this.project = project;
    }

    @Override
    public void log(String formattedMessage, Level level) {
         if (formattedMessage.startsWith("Quality profile for ")) {
             Matcher matcher = PROFILE_LANGUAGE_PATTERN.matcher(formattedMessage);
             if (matcher.find()) {
                 String profileLanguage = matcher.group(1);
                 ProblemCacheService.getInstance(project).getProfileLanguages().add(profileLanguage);
             }
         }

        if (formattedMessage.startsWith("Analysis report generated in")) {
            MessageBusManager.publishLogToEDT(project, ResourcesLoader.getString("analysis.report.copy.start"), Level.INFO);
            ReportUtils.copyReportDir(project);
            MessageBusManager.publishLogToEDT(project, ResourcesLoader.getString("analysis.report.copy.success"), Level.INFO);

            FutureTask<Report> task = new FutureTask<>(() -> {
                MessageBusManager.publishLogToEDT(project, ResourcesLoader.getString("analysis.report.parse.start"), Level.INFO);
                Report report = ReportUtils.createReport(project);
                ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
                problemCacheService.setIssues(report.getIssues());
                problemCacheService.setStats(
                        report.getBugCount(),
                        report.getCodeSmellCount(),
                        report.getVulnerabilityCount(),
                        report.getDuplicatedBlocksCount(),
                        report.getSecurityHotSpotCount());
                return report;
            });

            new Thread(task).start();

            try {
                task.get();
                MessageBusManager.publishLogToEDT(project, ResourcesLoader.getString("analysis.report.parse.success"), Level.INFO);
            } catch (Exception e) {
                // TODO:log中抛出的异常并不会被外层捕获
                MessageBusManager.publishLogToEDT(project, ResourcesLoader.getString("analysis.report.parse.failed", e.getMessage() + LogUtils.formatStackTrace(e.getStackTrace())), Level.ERROR);
                // throw new RuntimeException("报告解析出错: " + e.getMessage());
            }
        }
        EventDispatchThreadHelper.invokeLater(() -> {
            // 并非所有的error，SonarScanner都会停止整个分析，所以这里不再针对error做提示，因为已经和原先设想的不同
//            if (level == Level.ERROR) {
//                BalloonTipFactory.showToolWindowErrorNotifier(project, SonarScannerStarter.createErrorInfo(formattedMessage).toString());
//            }
            MessageBusManager.publishLog(project, formattedMessage, level);
        });



    }

}
