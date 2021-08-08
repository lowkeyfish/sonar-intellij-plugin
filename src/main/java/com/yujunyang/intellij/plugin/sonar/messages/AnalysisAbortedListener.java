
package com.yujunyang.intellij.plugin.sonar.messages;

import com.intellij.util.messages.Topic;

public interface AnalysisAbortedListener {
	Topic<AnalysisAbortedListener> TOPIC = Topic.create("Sonar Analysis Aborted", AnalysisAbortedListener.class);

	/**
	 * Invoked by EDT.
	 */
	void analysisAborted();
}
