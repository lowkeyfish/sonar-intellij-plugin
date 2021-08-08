package com.yujunyang.intellij.plugin.sonar.core;

import com.intellij.openapi.project.Project;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.gui.common.BalloonTipFactory;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import org.sonarsource.scanner.api.LogOutput;

public class LogOutputImpl implements LogOutput {
    private Project project;


    public LogOutputImpl(Project project) {
        this.project = project;
    }

    @Override
    public void log(String formattedMessage, Level level) {
        if (formattedMessage.startsWith("Analysis report generated in")) {
            ReportAnalyzer.copyReportDir(project);
        }
        EventDispatchThreadHelper.invokeLater(() -> {
            if (level == Level.ERROR) {
                BalloonTipFactory.showToolWindowErrorNotifier(project, SonarScannerStarter.createErrorInfo(formattedMessage).toString());
            }
            MessageBusManager.publishLog(project, formattedMessage, level);
        });

    }

}
