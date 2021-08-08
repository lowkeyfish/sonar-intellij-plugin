package com.yujunyang.intellij.plugin.sonar.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.impl.ToolWindowImpl;
import com.intellij.util.Consumer;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.common.PluginConstants;
import com.yujunyang.intellij.plugin.sonar.extensions.ToolWindowFactoryImpl;
import com.yujunyang.intellij.plugin.sonar.gui.common.BalloonTipFactory;
import com.yujunyang.intellij.plugin.sonar.messages.AnalysisAbortingListener;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import org.jetbrains.annotations.NotNull;
import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.LogOutput;
import org.sonarsource.scanner.api.StdOutLogOutput;
import org.sonarsource.scanner.api.internal.ScannerException;

public abstract class SonarScannerStarter implements AnalysisAbortingListener {

    private static final Logger LOGGER = Logger.getInstance(SonarScannerStarter.class);

    @NotNull
    private final Project project;

    @NotNull
    private final String title;

    private final boolean startProgressInBackground;

    private final boolean startProgressModal;

    private final AtomicBoolean cancellingByUser;



    public SonarScannerStarter(
            @NotNull final Project project,
            @NotNull final String title) {
        this(project, title, ProgressStartType.RunInBackgroundFromSettings);
    }

    public SonarScannerStarter(
            @NotNull final Project project,
            @NotNull final String title,
            @NotNull final ProgressStartType progressStartType) {
        this.project = project;
        this.title = title;
        switch (progressStartType) {
            case RunInBackgroundFromSettings:
            case RunInBackground:
                this.startProgressInBackground = false;
                this.startProgressModal = false;
                break;
            case Modal:
                this.startProgressInBackground = false;
                this.startProgressModal = true;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported " + progressStartType);
        }
        this.cancellingByUser = new AtomicBoolean();
        MessageBusManager.subscribe(project, this, AnalysisAbortingListener.TOPIC, this);
    }

    public final void start() {
        EventDispatchThreadHelper.checkEDT();

        final CompilerManager compilerManager = CompilerManager.getInstance(project);
        createCompileScope(compilerManager, compileScope -> {
            if (compileScope != null) {
                finalizeCompileScope(compileScope);
                compilerManager.make(compileScope, (aborted, errors, warnings, compileContext) -> {
                    // TODO: 这里原先是要判断了只有设置的是编译后不启动检测才会执行
                    // 猜测原因是有其他地方设置了编译后自动执行检测，因此防止触发两次
                    if (!aborted && errors == 0) {
                        EventDispatchThreadHelper.checkEDT(); // see javadoc of CompileStatusNotification
                        // Compiler can cause dumb mode, and finished() is invoked inside.
                        // We need to continue outside dumb mode to make activateToolWindow work f. e.
                        DumbService.getInstance(project).runWhenSmart(() -> {
                            EventDispatchThreadHelper.checkEDT();
                            startImpl(true);
                        });
                    }
                });
            }
        });
    }

    @Override
    public void analysisAborting() {
        this.cancellingByUser.set(true);
    }

    protected abstract void createCompileScope(
            @NotNull final CompilerManager compilerManager,
            @NotNull final Consumer<CompileScope> consumer);


//    protected final void showWarning(@NotNull final String message) {
//        EventDispatchThreadHelper.invokeLater(() -> BalloonTipFactory.showToolWindowWarnNotifier(
//                project, message + " " + ResourcesLoader.getString("analysis.aborted")));
//    }

    /**
     * Depend on selected configuration is creepy but no other way to make it work with Android, see
     * https://intellij-support.jetbrains.com/hc/en-us/community/posts/207521525-CompilerManager-make-does-not-all-tasks-in-case-of-a-Android-module-project
     * <p>
     * Maybe, sometime, we should make this more clever and search for the best configuration instead of just use the selected.
     */
    private void finalizeCompileScope(@NotNull final CompileScope compileScope) {
        final RunnerAndConfigurationSettings runnerAndConfigurationSettings = RunManager.getInstance(project).getSelectedConfiguration();
        if (runnerAndConfigurationSettings != null) {
            final RunConfiguration runConfiguration = runnerAndConfigurationSettings.getConfiguration();
            compileScope.putUserData(CompilerManager.RUN_CONFIGURATION_KEY, runConfiguration);
            compileScope.putUserData(CompilerManager.RUN_CONFIGURATION_TYPE_ID_KEY, runConfiguration.getType().getId());
        }
    }

    private void startImpl(final boolean justCompiled) {
        MessageBusManager.publishAnalysisStarted(project);
        if (!ApplicationManager.getApplication().isUnitTestMode()) {
            final ToolWindow toolWindow = ToolWindowFactoryImpl.getWindow(project);
            if (toolWindow == null) {
                throw new IllegalStateException("No SonarAnalyzer ToolWindow");
            }
            /*
             * Important: Make sure the tool window is initialized.
             * This call is to important to make it just in case of false = toolWindowToFront
             * because we have no guarantee that activateToolWindow works.
             */
            ((ToolWindowImpl) toolWindow).ensureContentInitialized();
            ToolWindowFactoryImpl.showWindowContent(toolWindow, 1);
        }

        final Task task;
        if (startProgressModal) {
            task = new Task.Modal(project, title, true) {
                @Override
                public void run(@NotNull final ProgressIndicator indicator) {
                    asyncStart(indicator, justCompiled);
                }
            };
        } else {
            task = new Task.Backgroundable(project, title, true) {
                @Override
                public void run(@NotNull final ProgressIndicator indicator) {
                    asyncStart(indicator, justCompiled);
                }

                @Override
                public boolean shouldStartInBackground() {
                    return startProgressInBackground;
                }
            };
        }
        task.queue();
    }

    private void asyncStart(@NotNull final ProgressIndicator indicator, final boolean justCompiled) {
        indicator.setIndeterminate(true);
        indicator.setText("Start Sonar analysis of " + project.getName());
        try {
            asyncStartImpl(indicator, justCompiled);
            MessageBusManager.publishAnalysisFinishedToEDT(project, new Object(), null);
        } catch (final ProcessCanceledException ignore) {
            MessageBusManager.publishAnalysisAbortedToEDT(project);
        } catch (Exception exc) {
            if (!(exc instanceof ScannerException)) {
                EventDispatchThreadHelper.invokeLater(() -> BalloonTipFactory.showToolWindowErrorNotifier(project, createErrorInfo(exc.getMessage()).toString()));
            }
            MessageBusManager.publishAnalysisFinishedToEDT(project, "", exc);
        }
    }

    private void asyncStartImpl(@NotNull final ProgressIndicator indicator, final boolean justCompiled) {
        LogOutput logOutput = new LogOutputImpl(project);
        EmbeddedScannerHelper.startEmbeddedScanner(project, logOutput);
    }

    public static StringBuilder createErrorInfo(String errorMessage) {
        final StringBuilder ret = new StringBuilder();
        ret.append("<h2>").append("Sonar analysis failed").append("</h2>");
        ret.append("<p>").append(errorMessage).append("</p>");
        ret.append("<br>");
        ret.append("Go to Log for more details");
        ret.append("<br>");
        return ret;
    }

}
