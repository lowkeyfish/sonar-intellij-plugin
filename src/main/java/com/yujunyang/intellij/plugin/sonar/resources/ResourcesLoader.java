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

package com.yujunyang.intellij.plugin.sonar.resources;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

public final class ResourcesLoader {
    private static final String ICON_RESOURCES_PATH_PREFIX = "/icons/";
    private static volatile ResourceBundle BUNDLE;
    private static final String BUNDLE_PATH = "i18n.Messages";
    private static final String MESSAGE_KEY_PREFIX = "com.yujunyang.intellij.plugin.sonar.";


    private ResourcesLoader() {}

    @NotNull
    public static Icon loadIcon(final String fileName) {
        return IconLoader.getIcon(ICON_RESOURCES_PATH_PREFIX + fileName);
    }

    @NotNull
    public static ResourceBundle getResourceBundle() {
        if (BUNDLE != null) {
            return BUNDLE;
        }

        WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        String uiLanguage = workspaceSettings.uiLanguageLocale;
        try {
            Locale locale = new Locale(uiLanguage);
            BUNDLE = ResourceBundle.getBundle(BUNDLE_PATH, locale);
        } catch (final MissingResourceException e) {
            throw new MissingResourceException("Missing Resource bundle: " + uiLanguage + ' ',  BUNDLE_PATH, "");
        }

        return BUNDLE;
    }

    @Nls
    public static String getString(@NotNull String key, @Nullable Object... params) {
        key = MESSAGE_KEY_PREFIX + key;
        try {
            if (BUNDLE == null) {
                getResourceBundle();
            }
            String ret = BUNDLE.getString(key);
            if (params != null && params.length > 0 && ret.indexOf('{') >= 0) {
                return MessageFormat.format(ret, params);
            }
            return ret;
        } catch (final MissingResourceException e) {
            throw new MissingResourceException("Missing Resource: " + "key: " + key + "  - resources: " + BUNDLE_PATH, BUNDLE_PATH, key);
        }
    }
}
