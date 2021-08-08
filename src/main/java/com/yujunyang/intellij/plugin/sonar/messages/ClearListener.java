
package com.yujunyang.intellij.plugin.sonar.messages;


import com.intellij.util.messages.Topic;


public interface ClearListener {
	Topic<ClearListener> TOPIC = Topic.create("Sonar ToolWindow Clear", ClearListener.class);

	/**
	 * Invoked by EDT.
	 */
	void clear();

}