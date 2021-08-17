package com.yujunyang.intellij.plugin.sonar.messages;

import com.intellij.util.messages.Topic;
import com.yujunyang.intellij.plugin.sonar.core.Issue;

public interface IssueClickListener {
    Topic<IssueClickListener> TOPIC = Topic.create("Sonar Analysis Report Issue Click", IssueClickListener.class);

    /**
     * Invoked by EDT.
     */
    void click(Issue issue);
}
