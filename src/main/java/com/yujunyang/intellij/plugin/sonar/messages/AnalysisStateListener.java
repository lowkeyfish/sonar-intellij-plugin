
package com.yujunyang.intellij.plugin.sonar.messages;


public interface AnalysisStateListener extends
		AnalysisStartedListener,
		AnalysisAbortingListener,
		AnalysisAbortedListener,
		AnalysisFinishedListener {
}