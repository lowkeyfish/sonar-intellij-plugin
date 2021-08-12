
package com.yujunyang.intellij.plugin.sonar.common;

import java.io.File;

public final class PluginConstants {
	public static final String PLUGIN_WEBSITE = "https://github.com/lowkeyfish/sonar-intellij-plugin";
	public static final String PLUGIN_DOWNLOAD_WEBSITE = "https://plugins.jetbrains.com/plugin/";
	public static final String PLUGIN_ISSUE_TRACKER = "https://github.com/lowkeyfish/sonar-intellij-plugin/issues";
	public static final String PLUGIN_ID = "com.yujunyang.intellij.plugin.sonar";
	public static final String PLUGIN_NAME = "Intellij Sonar plugin";

	public static final String TOOL_WINDOW_ID = "SonarAnalyzer";
	public static final String ACTION_GROUP_LEFT = "SonarAnalyzer.ToolBarActions.left";

	public static final String FILE_SEPARATOR = File.separator;

	private PluginConstants() {
	}
}
