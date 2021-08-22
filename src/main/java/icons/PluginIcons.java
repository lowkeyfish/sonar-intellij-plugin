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

package icons;

import javax.swing.Icon;

import com.intellij.icons.AllIcons;
import com.intellij.ui.LayeredIcon;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;

public interface PluginIcons {
    Icon LOGO = ResourcesLoader.loadIcon("logo.svg");
    Icon LOGO_13X13 = ResourcesLoader.loadIcon("logo13x13.svg");

    Icon SONAR_LOGO = ResourcesLoader.loadIcon("sonar.svg");
    Icon SONAR_LOGO_13X13 = ResourcesLoader.loadIcon("sonar13x13.svg");
    Icon SONAR_QUBE = ResourcesLoader.loadIcon("SonarQube.svg");


    Icon ANALYZE_PROJECT_FILES_NOT_INCLUDING_TESTS_ICON = LayeredIcon.create(AllIcons.Nodes.Project, AllIcons.Nodes.RunnableMark);

    Icon ANALYZE_EXECUTE = AllIcons.Actions.Execute;
    Icon ANALYZE_SUSPEND = AllIcons.Actions.Suspend;
    Icon PLUGIN_SETTING = AllIcons.General.GearPlain;
    Icon PLUGIN_HELP = AllIcons.Actions.Help;

    Icon SMILE = ResourcesLoader.loadIcon("smile.svg");

    Icon ISSUE = ResourcesLoader.loadIcon("issue.svg");

    Icon BUGS = ResourcesLoader.loadIcon("bugs.svg");
    Icon VULNERABILITY = ResourcesLoader.loadIcon("vulnerability.svg");
    Icon CODE_SMELL = ResourcesLoader.loadIcon("codeSmell.svg");
    Icon BLOCKER = ResourcesLoader.loadIcon("blocker.svg");
    Icon CRITICAL = ResourcesLoader.loadIcon("critical.svg");
    Icon MAJOR = ResourcesLoader.loadIcon("major.svg");
    Icon MINOR = ResourcesLoader.loadIcon("minor.svg");
    Icon INFO = ResourcesLoader.loadIcon("info.svg");
}
