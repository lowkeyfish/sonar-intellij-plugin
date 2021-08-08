
package com.yujunyang.intellij.plugin.sonar.messages;

import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AnalysisFinishedListener {
	Topic<AnalysisFinishedListener> TOPIC = Topic.create("Sonar Analysis Finished", AnalysisFinishedListener.class);

	/**
	 * Invoked by EDT.
	 */
	void analysisFinished(@NotNull final Object result, @Nullable final Throwable error);

}
