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

package com.yujunyang.intellij.plugin.sonar.gui.common;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;

import com.intellij.ide.plugins.PluginManagerConfigurable;
import com.intellij.ui.JBColor;

public class FixButton extends JButton {
    private static final Color GreenColor = new JBColor(0x5D9B47, 0x2B7B50);
    private static final Color WhiteForeground = new JBColor(Color.white, new Color(0xBBBBBB));

    private static final Color FillForegroundColor = JBColor.namedColor("Plugins.Button.installFillForeground", WhiteForeground);
    private static final Color FillBackgroundColor = JBColor.namedColor("Plugins.Button.installFillBackground", GreenColor);

    private static final Color ForegroundColor = JBColor.namedColor("Plugins.Button.installForeground", GreenColor);
    private static final Color BackgroundColor =
            JBColor.namedColor("Plugins.Button.installBackground", PluginManagerConfigurable.MAIN_BG_COLOR);

    @SuppressWarnings("UseJBColor")
    private static final Color FocusedBackground = JBColor.namedColor("Plugins.Button.installFocusedBackground", new Color(0xE1F6DA));

    private static final Color BorderColor = JBColor.namedColor("Plugins.Button.installBorderColor", GreenColor);

    public FixButton() {
        putClientProperty("JButton.textColor", ForegroundColor);
        putClientProperty("JButton.focusedTextColor", ForegroundColor);
        putClientProperty("JButton.backgroundColor", BackgroundColor);
        putClientProperty("JButton.borderColor", BorderColor);
        putClientProperty("JButton.focusedBorderColor", BorderColor);
        setPreferredSize(new Dimension(70, 18));
    }
}
