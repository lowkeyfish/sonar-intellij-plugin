package com.yujunyang.intellij.plugin.sonar.core;

import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.project.Project;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.LogOutput;

public final class EmbeddedScannerHelper {
    public static Map<String, String> createTaskProperties(Project project) {
        Map<String, String> props = new HashMap<>();
        {
            props.put("sonar.host.url", WorkspaceSettings.getInstance().sonarHostUrl);
            props.put("sonar.projectKey", "com.yujunyang.intellij.plugin.sonar");
            props.put("sonar.projectName", "com.yujunyang.intellij.plugin.sonar:" + project.getName());
            props.put("sonar.projectVersion", "1.0.0");
            props.put("sonar.projectBaseDir", project.getBasePath());
            props.put("sonar.working.directory", "./target/.scannerwork");
            props.put("sonar.java.source", "8");
//            props.put("sonar.tests", "./src/test");
            props.put("sonar.tests", "");
//            props.put("sonar.sources", "./src/main");
            props.put("sonar.sources", IdeaUtils.getAllSourceRootPath(project));
            props.put("sonar.java.libraries", IdeaUtils.getFullClassPath(project));
//            props.put("sonar.java.binaries", "./target/classes");
            props.put("sonar.java.binaries", IdeaUtils.getAllCompilerOutPath(project));
        }

        return props;
    }

    public static void startEmbeddedScanner(Project project, LogOutput logOutput) {
        Map<String, String> taskProperties = createTaskProperties(project);
        EmbeddedScanner scanner = EmbeddedScanner.create("Intellij Sonar plugin", "1.0.0", logOutput);
        scanner.addGlobalProperties(taskProperties);
        scanner.start();
        scanner.execute(taskProperties);
    }

}
