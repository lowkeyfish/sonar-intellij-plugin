package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBPanel;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.gui.layout.SampleVerticalScrollLayout;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;

public class IssueListPanel extends JBPanel {
    private Project project;

    public IssueListPanel(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        setLayout(new SampleVerticalScrollLayout());
    }

    public void refresh() {
        removeAll();
        ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
        ConcurrentMap<PsiFile, List<AbstractIssue>> issues = problemCacheService.getIssues();
        for (Map.Entry<PsiFile, List<AbstractIssue>> fileIssues : issues.entrySet()) {
            add(new IssueFileGroupPanel(fileIssues.getKey(), fileIssues.getValue()));
        }
    }

}
