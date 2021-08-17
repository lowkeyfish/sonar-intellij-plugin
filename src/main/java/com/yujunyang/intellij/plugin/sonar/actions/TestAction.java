package com.yujunyang.intellij.plugin.sonar.actions;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.core.Report;
import com.yujunyang.intellij.plugin.sonar.core.ReportUtils;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;
import org.jetbrains.annotations.NotNull;

public class TestAction extends AbstractAction {
    @Override
    public void updateImpl(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ToolWindow toolWindow,
            @NotNull AnalyzeState state) {
        e.getPresentation().setEnabled(true);
        e.getPresentation().setVisible(true);
    }


    @Override
    public void actionPerformedImpl(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ToolWindow toolWindow,
            @NotNull AnalyzeState state) {
//        System.out.println(project.getBasePath());
//        System.out.println(project.getWorkspaceFile().getPath());
//        String fullClassPath = OrderEnumerator.orderEntries(project).recursively().getPathsList().getPathsString();
//        Arrays.stream(fullClassPath.split(";")).forEach(n -> {
//            System.out.println(n);
//        });
//
//        Module[] modules = ModuleManager.getInstance(project).getModules();
//        for (Module module : modules) {
//            CompilerModuleExtension compilerModuleExtension = CompilerModuleExtension.getInstance(module);
//            VirtualFile compilerOutPath = compilerModuleExtension.getCompilerOutputPath();
//            if (compilerOutPath != null) {
//                System.out.println(module.getName());
//                System.out.println(compilerOutPath.getCanonicalPath());
//            }
//            ModuleRootManager root = ModuleRootManager.getInstance(module);
//            String[] rootUrls = root.getSourceRootUrls(false);
//            for (String rootUrl : rootUrls) {
//                System.out.println(rootUrl);
//            }
//        }

        Report report = ReportUtils.createReport(e.getProject());
        ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
        problemCacheService.setIssues(report.getIssues());
        problemCacheService.setStats(report.getBugCount(), report.getCodeSmellCount(), report.getVulnerabilityCount(), report.getDuplicatedBlocksCount());
        DaemonCodeAnalyzer.getInstance(project).restart();
        MessageBusManager.publishAnalysisFinished(e.getProject(), new Object(), null);
    }
}
