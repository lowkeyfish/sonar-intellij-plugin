
package com.yujunyang.intellij.plugin.sonar.messages;


import com.intellij.util.messages.Topic;

public interface AnalysisStartedListener {
	Topic<AnalysisStartedListener> TOPIC = Topic.create("Sonar Analysis Started", AnalysisStartedListener.class);

	/**
	 * Invoked by EDT.
	 */
	void analysisStarted();

}