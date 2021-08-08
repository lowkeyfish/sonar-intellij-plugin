
package com.yujunyang.intellij.plugin.sonar.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.extensions.ToolWindowFactoryImpl;
import org.jetbrains.annotations.NotNull;


public abstract class AbstractAction extends AnAction {

	@Override
	public final void update(@NotNull final AnActionEvent e) {
		final Project project = IdeaUtils.getProject(e.getDataContext());
		if (project == null || !project.isInitialized() || !project.isOpen()) {
			e.getPresentation().setEnabled(false);
			e.getPresentation().setVisible(false);
			return;
		}
		final ToolWindow toolWindow = ToolWindowFactoryImpl.getWindow(project);
		if (toolWindow == null || !toolWindow.isAvailable()) {
			e.getPresentation().setEnabled(false);
			e.getPresentation().setVisible(false);
			return;
		}
		updateImpl(
				e,
				project,
				toolWindow,
				AnalyzeState.get(project)
		);
	}

	public abstract void updateImpl(
			@NotNull final AnActionEvent e,
			@NotNull final Project project,
			@NotNull final ToolWindow toolWindow,
			@NotNull final AnalyzeState state
	);

	@Override
	public final void actionPerformed(@NotNull final AnActionEvent e) {
		final Project project = IdeaUtils.getProject(e.getDataContext());
		if (project == null) {
			e.getPresentation().setEnabled(false);
			e.getPresentation().setVisible(false);
			return;
		}
		final ToolWindow toolWindow = ToolWindowFactoryImpl.getWindow(project);
		if (toolWindow == null || !toolWindow.isAvailable()) {
			e.getPresentation().setEnabled(false);
			e.getPresentation().setVisible(false);
			return;
		}
		actionPerformedImpl(
				e,
				project,
				toolWindow,
				AnalyzeState.get(project)
		);
	}

	public abstract void actionPerformedImpl(
			@NotNull final AnActionEvent e,
			@NotNull final Project project,
			@NotNull final ToolWindow toolWindow,
			@NotNull final AnalyzeState state
	);
}
