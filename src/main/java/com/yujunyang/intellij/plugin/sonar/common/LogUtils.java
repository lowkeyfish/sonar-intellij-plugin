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

package com.yujunyang.intellij.plugin.sonar.common;

import java.util.ArrayList;
import java.util.List;

public final class LogUtils {

    public static String formatStackTrace(StackTraceElement[] stackTraceElements) {
        StringBuilder builder = new StringBuilder();

        for (StackTraceElement item : stackTraceElements) {
            builder.append("\n\t");
            builder.append("at ");
            builder.append(item.getClassName());
            builder.append(".");
            builder.append(item.getMethodName());
            builder.append("(");
            builder.append(item.getFileName());
            builder.append(":");
            builder.append(item.getLineNumber());
            builder.append(")");
        }

        return builder.toString();
    }

    public static String formatException(Exception exc) {
        StringBuilder builder = new StringBuilder();

        builder.append(exc.getClass().getName());
        builder.append(": ");
        builder.append(exc.getMessage());
        builder.append(formatStackTrace(exc.getStackTrace()));

        List<Throwable> causes = new ArrayList<>();
        Throwable cause = exc.getCause();
        int level = 0;
        while (cause != null && level < 3) {
            causes.add(cause);
            cause = cause.getCause();
            level++;
        }

        causes.forEach(n -> {
            builder.append("\n\t\n\t");
            builder.append("Caused by: ");
            builder.append(n.getClass().getName());
            builder.append(": ");
            builder.append(n.getMessage());
            builder.append(formatStackTrace(n.getStackTrace()));
        });

        return builder.toString();
    }
}
