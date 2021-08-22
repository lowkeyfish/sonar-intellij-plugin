/*
 * Copyright 2021 Yu Junyang
 * https://github.com/lowkeyfish
 *
 * This file is part of Sonar Intellij plugin.
 *
 * Sonar Intellij plugin is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Sonar Intellij plugin is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar Intellij plugin.
 * If not, see <http://www.gnu.org/licenses/>.
 */

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
//        BalloonTipFactory.showToolWindowInfoNotifier(
//                e.getProject(),
//                createHelpInfo().toString(),
//                evt -> {
//                    if (HyperlinkEvent.EventType.ACTIVATED.equals(evt.getEventType())) {
//                        BrowserUtil.browse(evt.getURL());
//                    }
//                }
//        );
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
