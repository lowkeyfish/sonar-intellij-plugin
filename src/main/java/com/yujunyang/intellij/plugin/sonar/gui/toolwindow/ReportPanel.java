package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import com.intellij.openapi.project.Project;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.JBPanel;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.extensions.ToolWindowFactoryImpl;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.messages.AnalysisStateListener;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReportPanel extends JBPanel implements AnalysisStateListener {
    private Project project;

    private LeftToolbarPanel leftToolbarPanel;
    private JBPanel bodyPanel;
    private CardLayout bodyPanelLayout;

    private IssuesPanel issuesPanel;
    private IssueDetailPanel issueDetailPanel;

    public ReportPanel(@NotNull Project project) {
        this.project = project;
        setLayout(new BorderLayout());
        init();
        MessageBusManager.subscribeAnalysisState(project, this, this);
    }

    private void init() {
        leftToolbarPanel = new LeftToolbarPanel();
        add(leftToolbarPanel, BorderLayout.WEST);

        bodyPanel = new JBPanel();
        bodyPanelLayout = new CardLayout();
        bodyPanel.setLayout(bodyPanelLayout);
        add(bodyPanel, BorderLayout.CENTER);

        bodyPanel.add("EMPTY", new MessagePanel("无代码分析结果"));

        OnePixelSplitter listAndCurrentSplitter = new OnePixelSplitter();
        listAndCurrentSplitter.getDivider().setBackground(UIUtils.borderColor());

        issuesPanel = new IssuesPanel(project);
        listAndCurrentSplitter.setFirstComponent(issuesPanel);

        issueDetailPanel = new IssueDetailPanel(project);
        listAndCurrentSplitter.setSecondComponent(issueDetailPanel);

        listAndCurrentSplitter.setProportion(0.25f);
//        add(listAndCurrentSplitter, BorderLayout.CENTER);
        bodyPanel.add("REPORT", listAndCurrentSplitter);
        bodyPanelLayout.show(bodyPanel, "EMPTY");
    }

    public void refresh() {
        issuesPanel.refresh();
    }

    public void reset() {
        issuesPanel.reset();
        issueDetailPanel.reset();
    }

    @Override
    public void analysisAborted() {

    }

    @Override
    public void analysisAborting() {

    }

    @Override
    public void analysisFinished(@NotNull Object result, @Nullable Throwable error) {
        EventDispatchThreadHelper.invokeLater(() -> {
            refresh();
            bodyPanelLayout.show(bodyPanel, "REPORT");
            ToolWindowFactoryImpl.showWindowContent(ToolWindowFactoryImpl.getWindow(project), 0);
        });
    }

    @Override
    public void analysisStarted() {
        bodyPanelLayout.show(bodyPanel, "EMPTY");
        reset();
    }
}
