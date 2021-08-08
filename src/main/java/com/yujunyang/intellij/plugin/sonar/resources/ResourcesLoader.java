package com.yujunyang.intellij.plugin.sonar.resources;

import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

public final class ResourcesLoader {
    private static final String ICON_RESOURCES_PATH_PREFIX = "/icons/";

    private ResourcesLoader() {}

    @NotNull
    public static Icon loadIcon(final String fileName) {
        return IconLoader.getIcon(ICON_RESOURCES_PATH_PREFIX + fileName);
    }
}
