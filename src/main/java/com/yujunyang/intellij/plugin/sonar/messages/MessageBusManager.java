
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

package com.yujunyang.intellij.plugin.sonar.messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.core.Issue;
import com.yujunyang.intellij.plugin.sonar.core.ReportUtils;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sonarsource.scanner.api.LogOutput;

public final class MessageBusManager {

    private static final Map<Project, MessageBus> _busByProject = new HashMap<>();

    private MessageBusManager() {
    }

    @NotNull
    private static MessageBus of(@NotNull final Project project) {
        MessageBus ret = _busByProject.get(project);
        if (ret == null) {
            ret = new MessageBus(project);
            _busByProject.put(project, ret);
        }
        return ret;
    }

    public static <L extends AnalysisStateListener> void subscribeAnalysisState(@NotNull final Project project, @NotNull final Object subscriber, @NotNull final L handler) {
        subscribe(project, subscriber, AnalysisStartedListener.TOPIC, handler);
        subscribe(project, subscriber, AnalysisAbortingListener.TOPIC, handler);
        subscribe(project, subscriber, AnalysisAbortedListener.TOPIC, handler);
        subscribe(project, subscriber, AnalysisFinishedListener.TOPIC, handler);
    }

    /**
     * Note that you can only subscribe *one* {@code handler} per {@code topic}.
     * If {@code subscriber} has subscribed the topic already, nothing is done.
     *
     * @param project    ..
     * @param subscriber ..
     * @param topic      ..
     * @param handler    ..
     * @param <L>.
     */
    public static <L> void subscribe(@NotNull final Project project, @NotNull final Object subscriber, @NotNull final Topic<L> topic, @NotNull final L handler) {
        EventDispatchThreadHelper.checkEDT();
        of(project).subscribe(subscriber, topic, handler);
    }

    public static void publishClear(@NotNull final Project project) {
        EventDispatchThreadHelper.checkEDT();
        AnalyzeState.set(project, AnalyzeState.Cleared);
        publish(project, ClearListener.TOPIC).clear();
    }

    public static void publishLog(@NotNull final Project project, @NotNull final String formattedMessage, LogOutput.Level level) {
        EventDispatchThreadHelper.checkEDT();
        publish(project, LogListener.TOPIC).log(formattedMessage, level);
    }

    public static void publishLogToEDT(@NotNull final Project project, @NotNull final String formattedMessage, LogOutput.Level level) {
        EventDispatchThreadHelper.checkNotEDT();
        EventDispatchThreadHelper.invokeLater(() -> {
            publish(project, LogListener.TOPIC).log(formattedMessage, level);
        });
    }

    public static void publishAnalysisStarted(@NotNull final Project project) {
        EventDispatchThreadHelper.checkEDT();
        (new Thread(() -> ReportUtils.deleteReportDir(project))).start();
        AnalyzeState.set(project, AnalyzeState.Started);
        ProblemCacheService.getInstance(project).reset();
        publish(project, AnalysisStartedListener.TOPIC).analysisStarted();
    }

    public static void publishAnalysisStartedToEDT(@NotNull final Project project) {
        EventDispatchThreadHelper.checkNotEDT();
        EventDispatchThreadHelper.invokeLater(() -> {
            ReportUtils.deleteReportDir(project);
            AnalyzeState.set(project, AnalyzeState.Started);
            publish(project, AnalysisStartedListener.TOPIC).analysisStarted();
        });
    }

    public static void publishAnalysisAborting(@NotNull final Project project) {
        EventDispatchThreadHelper.checkEDT();
        AnalyzeState.set(project, AnalyzeState.Aborting);
        publish(project, AnalysisAbortingListener.TOPIC).analysisAborting();
    }

    public static void publishAnalysisAbortedToEDT(@NotNull final Project project) {
        EventDispatchThreadHelper.checkNotEDT();
        EventDispatchThreadHelper.invokeLater(() -> {
            AnalyzeState.set(project, AnalyzeState.Aborted);
            publish(project, AnalysisAbortedListener.TOPIC).analysisAborted();
        });
    }

    public static void publishAnalysisFinishedToEDT(@NotNull final Project project, @NotNull final Object result, @Nullable final Throwable error) {
        EventDispatchThreadHelper.checkNotEDT();
        /*
         * Guarantee thread visibility *one* time.
         */
        final AtomicReference<Object> resultRef = new AtomicReference<>(result);
        final AtomicReference<Throwable> errorRef = new AtomicReference<>(error);
        EventDispatchThreadHelper.invokeLater(() -> {
            AnalyzeState.set(project, AnalyzeState.Finished);
            publish(project, AnalysisFinishedListener.TOPIC).analysisFinished(resultRef.get(), errorRef.get());
        });
    }

    public static void publishAnalysisFinished(@NotNull final Project project, @NotNull final Object result, @Nullable final Throwable error) {
        EventDispatchThreadHelper.checkEDT();
        /*
         * Guarantee thread visibility *one* time.
         */
        final AtomicReference<Object> resultRef = new AtomicReference<>(result);
        final AtomicReference<Throwable> errorRef = new AtomicReference<>(error);
        AnalyzeState.set(project, AnalyzeState.Finished);
        publish(project, AnalysisFinishedListener.TOPIC).analysisFinished(resultRef.get(), errorRef.get());
    }

    public static void publishIssueClick(@NotNull final Project project, @NotNull final Issue issue) {
        EventDispatchThreadHelper.checkEDT();
        publish(project, IssueClickListener.TOPIC).click(issue);
    }

    public static void publishDuplicatedBlocksIssueClick(@NotNull final Project project, @NotNull final List<DuplicatedBlocksIssue> issues) {
        EventDispatchThreadHelper.checkEDT();
        publish(project, DuplicatedBlocksIssueClickListener.TOPIC).click(issues);
    }

    @NotNull
    private static <L> L publish(@NotNull final Project project, @NotNull final Topic<L> topic) {
        EventDispatchThreadHelper.checkEDT();
        return of(project).publisher(topic);
    }

    public static void dispose(@NotNull final Project project) {
        EventDispatchThreadHelper.checkEDT();
        _busByProject.remove(project);
    }
}
