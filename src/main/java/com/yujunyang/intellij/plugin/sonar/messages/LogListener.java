package com.yujunyang.intellij.plugin.sonar.messages;

import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.sonarsource.scanner.api.LogOutput;

public interface LogListener {
    Topic<LogListener> TOPIC = Topic.create("Sonar Analysis Log", LogListener.class);

    /**
     * Invoked by EDT.
     */
    void log(@NotNull final String formattedMessage, @NotNull LogOutput.Level level);
}
