
package com.yujunyang.intellij.plugin.sonar.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAnalyzeAction extends AbstractAction {

	@Override
	public final void actionPerformedImpl(
			@NotNull final AnActionEvent e,
			@NotNull final Project project,
			@NotNull final ToolWindow toolWindow,
			@NotNull final AnalyzeState state
	) {

		EventDispatchThreadHelper.checkEDT();

		analyze(
				e,
				project,
				toolWindow,
				state
		);
	}

	public abstract void analyze(
			@NotNull final AnActionEvent e,
			@NotNull final Project project,
			@NotNull final ToolWindow toolWindow,
			@NotNull final AnalyzeState state
	);


}
