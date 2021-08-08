package com.yujunyang.intellij.plugin.sonar.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SettingService {
    private Project project;

    public SettingService(Project project) {
        this.project = project;
    }



    public static SettingService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, SettingService.class);
    }
}
