package com.yujunyang.intellij.plugin.sonar.core;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.project.Project;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.gui.common.BalloonTipFactory;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;
import org.sonarsource.scanner.api.LogOutput;

public class LogOutputImpl implements LogOutput {
    private Project project;


    public LogOutputImpl(Project project) {
        this.project = project;
    }

    @Override
    public void log(String formattedMessage, Level level) {
        if (formattedMessage.startsWith("Analysis report generated in")) {
            ReportUtils.copyReportDir(project);
            EventDispatchThreadHelper.invokeLater(() -> {
                Report report = ReportUtils.createReport(project);
                ProblemCacheService.getInstance(project).setIssues(report.getIssues());
                MessageBusManager.publishAnalysisFinished(project, report, null);
                DaemonCodeAnalyzer.getInstance(project).restart();
            });
        }
        EventDispatchThreadHelper.invokeLater(() -> {
            if (level == Level.ERROR) {
                BalloonTipFactory.showToolWindowErrorNotifier(project, SonarScannerStarter.createErrorInfo(formattedMessage).toString());
            }
            MessageBusManager.publishLog(project, formattedMessage, level);
        });

    }

}
