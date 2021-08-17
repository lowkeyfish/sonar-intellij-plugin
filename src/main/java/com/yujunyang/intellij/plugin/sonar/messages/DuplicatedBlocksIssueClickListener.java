package com.yujunyang.intellij.plugin.sonar.messages;

import java.util.List;

import com.intellij.util.messages.Topic;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;

public interface DuplicatedBlocksIssueClickListener {
    Topic<DuplicatedBlocksIssueClickListener> TOPIC = Topic.create("Sonar Analysis Report DuplicatedBlocksIssue Click", DuplicatedBlocksIssueClickListener.class);

    /**
     * Invoked by EDT.
     */
    void click(List<DuplicatedBlocksIssue> issues);
}
