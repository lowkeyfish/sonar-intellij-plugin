package com.yujunyang.intellij.plugin.sonar.config;

import java.util.Arrays;
import java.util.List;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "SonarAnalyzer-Workspace", storages = { @Storage("intellij-sonar-plugin.xml") })
public final class WorkspaceSettings implements PersistentStateComponent<WorkspaceSettings> {

    @Tag
    public String sonarHostUrl = "";

    @Tag
    public String sonarToken = "";

    @Tag
    public List<String> languages = Arrays.asList("java", "xml");

    @Tag
    public boolean annotationGutterIcon = true;

    @Nullable
    @Override
    public WorkspaceSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull WorkspaceSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isSonarConfigured() {
        return StringUtil.isNotEmpty(sonarHostUrl);
    }

    public String getSonarHostUrl() {
        if (sonarHostUrl == null) {
            return "";
        }
        return sonarHostUrl;
    }

    public String getSonarToken() {
        if (sonarToken == null) {
            return "";
        }
        return sonarToken;
    }

    public static WorkspaceSettings getInstance() {
        return ServiceManager.getService(WorkspaceSettings.class);
    }
}
