/*
 * Copyright 2021 Yu Junyang
 * https://github.com/lowkeyfish
 *
 * This file is part of Sonar Intellij plugin.
 *
 * Sonar Intellij plugin is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Sonar Intellij plugin is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar Intellij plugin.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.yujunyang.intellij.plugin.sonar.messages.AnalysisStateListener;
import com.yujunyang.intellij.plugin.sonar.messages.LogListener;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sonarsource.scanner.api.LogOutput;

public class LogPanel extends JBPanel implements AnalysisStateListener {
    private Project project;
    private ConsoleView consoleView;

    public LogPanel(Project project) {
        this.project = project;
        setLayout(new BorderLayout());
        init();
        MessageBusManager.subscribeAnalysisState(project, this, this);
        MessageBusManager.subscribe(project, this, LogListener.TOPIC, this::print);
    }

    private void init() {
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        add(consoleView.getComponent(), BorderLayout.CENTER);
    }

    private void print(String message, LogOutput.Level level) {

        ConsoleViewContentType consoleViewContentType = level == LogOutput.Level.ERROR ?
                ConsoleViewContentType.LOG_ERROR_OUTPUT : ConsoleViewContentType.NORMAL_OUTPUT;
        consoleView.print(level.name() + ": " + message + "\n", consoleViewContentType);
    }

    @Override
    public void analysisAborted() {
        consoleView.print("\nSonar analysis aborted\n", ConsoleViewContentType.NORMAL_OUTPUT);
    }

    @Override
    public void analysisAborting() {
        consoleView.print("\nSonar analysis aborting\n", ConsoleViewContentType.NORMAL_OUTPUT);
    }

    @Override
    public void analysisFinished(@NotNull Object result, @Nullable Throwable error) {
        consoleView.print("\nSonar analysis finished\n", ConsoleViewContentType.NORMAL_OUTPUT);
    }

    @Override
    public void analysisStarted() {
        consoleView.clear();
        consoleView.print("Sonar analysis start...\n\n", ConsoleViewContentType.LOG_VERBOSE_OUTPUT);
    }
}
