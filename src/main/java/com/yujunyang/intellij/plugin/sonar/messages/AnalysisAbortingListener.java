
package com.yujunyang.intellij.plugin.sonar.messages;

import com.intellij.util.messages.Topic;

public interface AnalysisAbortingListener {
	Topic<AnalysisAbortingListener> TOPIC = Topic.create("Sonar Analysis Aborting", AnalysisAbortingListener.class);

	/**
	 * Invoked by EDT.
	 */
	void analysisAborting();

}