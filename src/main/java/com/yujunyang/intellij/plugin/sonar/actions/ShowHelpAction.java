package com.yujunyang.intellij.plugin.sonar.actions;

import javax.swing.event.HyperlinkEvent;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.common.PluginConstants;
import com.yujunyang.intellij.plugin.sonar.gui.common.BalloonTipFactory;
import org.jetbrains.annotations.NotNull;

public class ShowHelpAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BalloonTipFactory.showToolWindowInfoNotifier(
                e.getProject(),
                createHelpInfo().toString(),
                evt -> {
                    if (HyperlinkEvent.EventType.ACTIVATED.equals(evt.getEventType())) {
                        BrowserUtil.browse(evt.getURL());
                    }
                }
        );
    }

    private StringBuilder createHelpInfo() {
        final StringBuilder ret = new StringBuilder();
        ret.append("<h2>").append(PluginConstants.PLUGIN_NAME + " " + IdeaUtils.getPluginVersion()).append("</h2>");
        ret.append("Website: <a href='").append(PluginConstants.PLUGIN_WEBSITE).append("'>").append(PluginConstants.PLUGIN_WEBSITE).append("</a>");
        ret.append("<br>");
        ret.append("Download: <a href='").append(PluginConstants.PLUGIN_DOWNLOAD_WEBSITE).append("'>").append(PluginConstants.PLUGIN_DOWNLOAD_WEBSITE).append("</a>");
        ret.append("<br>");
        ret.append("Issue tracker: <a href='").append(PluginConstants.PLUGIN_ISSUE_TRACKER).append("'>").append(PluginConstants.PLUGIN_ISSUE_TRACKER).append("</a>");
        ret.append("<br><br>");
        return ret;
    }


}
