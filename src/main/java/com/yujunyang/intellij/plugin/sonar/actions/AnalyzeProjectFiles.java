
package com.yujunyang.intellij.plugin.sonar.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.Consumer;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.core.SonarScannerStarter;
import org.jetbrains.annotations.NotNull;

public abstract class AnalyzeProjectFiles extends AbstractAnalyzeAction {
	private final boolean includeTests;

	AnalyzeProjectFiles(boolean includeTests) {
		this.includeTests = includeTests;
	}

	@Override
	public void updateImpl(
			@NotNull final AnActionEvent e,
			@NotNull final Project project,
			@NotNull final ToolWindow toolWindow,
			@NotNull final AnalyzeState state
	) {

		final boolean enable = state.isIdle();

		e.getPresentation().setEnabled(enable);
		e.getPresentation().setVisible(true);
	}


	@Override
	public void analyze(
			@NotNull final AnActionEvent e,
			@NotNull final Project project,
			@NotNull final ToolWindow toolWindow,
			@NotNull final AnalyzeState state
	) {
		new SonarScannerStarter(project, "对项目[" + project.getName() + "]执行Sonar代码检测") {
			@Override
			protected void createCompileScope(@NotNull CompilerManager compilerManager, @NotNull Consumer<CompileScope> consumer) {
				consumer.consume(compilerManager.createProjectCompileScope(project));
			}
		}.start();
	}
}
