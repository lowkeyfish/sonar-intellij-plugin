package com.yujunyang.intellij.plugin.sonar.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
            MessageBusManager.publishLogToEDT(project, "开始复制报告", Level.INFO);
            ReportUtils.copyReportDir(project);
            MessageBusManager.publishLogToEDT(project, "报告复制成功", Level.INFO);

            FutureTask<Report> task = new FutureTask<>(() -> {
                MessageBusManager.publishLogToEDT(project, "开始解析报告", Level.INFO);
                Report report = ReportUtils.createReport(project);
                ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
                problemCacheService.setIssues(report.getIssues());
                problemCacheService.setStats(report.getBugCount(), report.getCodeSmellCount(), report.getVulnerabilityCount(), report.getDuplicatedBlocksCount());
                return report;
            });

            new Thread(task).start();

            try {
                task.get();
                MessageBusManager.publishLogToEDT(project, "报告解析成功", Level.INFO);
            } catch (Exception e) {
                MessageBusManager.publishLogToEDT(project, "报告解析成功", Level.ERROR);
                throw new RuntimeException("报告解析出错: " + e.getMessage());
            }
        }
        EventDispatchThreadHelper.invokeLater(() -> {
            if (level == Level.ERROR) {
                BalloonTipFactory.showToolWindowErrorNotifier(project, SonarScannerStarter.createErrorInfo(formattedMessage).toString());
            }
            MessageBusManager.publishLog(project, formattedMessage, level);
        });



    }

}
