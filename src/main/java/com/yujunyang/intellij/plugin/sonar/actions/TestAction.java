package com.yujunyang.intellij.plugin.sonar.actions;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.intellij.ide.macro.ModuleSdkPathMacro;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.core.ReportUtils;
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

        ReportUtils.createReport(e.getProject());
    }
}
