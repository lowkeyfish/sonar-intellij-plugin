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

package com.yujunyang.intellij.plugin.sonar.core;

import java.util.List;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;

public class AnalyzeScope {
    private ScopeType type;
    private List<VirtualFile> files;
    private Project project;
    private Module module;

    public AnalyzeScope(Project project, ScopeType type) {
        this.project = project;
        this.type = type;
    }

    public AnalyzeScope(Project project, ScopeType type, Module module) {
        this(project, type);
        this.module = module;
    }

    public AnalyzeScope(Project project, ScopeType type, List<VirtualFile> files) {
        this(project, type);
        this.files = files;
    }

    public String getSources() {
        if (ScopeType.PROJECT_FILES.equals(type)) {
            return IdeaUtils.getAllSourceRootPath(project);
        }

        if (ScopeType.MODULE_FILES.equals(type)) {
            return IdeaUtils.getAllSourceRootPath(module);
        }

        if (files != null && files.size() > 0) {
            return IdeaUtils.getAllSourceRootPath(files);
        }

        return IdeaUtils.getAllSourceRootPath(project);
    }

    public String getJavaBinaries() {
        return ApplicationManager.getApplication().runReadAction((Computable<String>)() -> {
            if (ScopeType.PROJECT_FILES.equals(type)) {
                return IdeaUtils.getAllCompilerOutputPath(project);
            }

            if (ScopeType.MODULE_FILES.equals(type)) {
                return IdeaUtils.getAllCompilerOutputPath(module);
            }

            if (files != null && files.size() > 0) {
                return IdeaUtils.getAllCompilerOutputPath(project, files);
            }

            return IdeaUtils.getAllCompilerOutputPath(project);
        });
    }

    public String getScopeDescription() {
        StringBuilder ret = new StringBuilder("Analyze scope: ");
        if (ScopeType.PROJECT_FILES.equals(type)) {
            ret.append("Project files\n");
            return ret.toString();
        }

        if (ScopeType.MODULE_FILES.equals(type)) {
            ret.append("Module[");
            ret.append(module.getName());
            ret.append("] files\n");
            return ret.toString();
        }

        if (ScopeType.PACKAGE_FILES.equals(type)) {
            ret.append("Package files\n");
            ret.append("\t");
            ret.append(files.get(0).getCanonicalPath());
            ret.append("\n");
            return ret.toString();
        }

        if (ScopeType.OPEN_FILES.equals(type)) {
            ret.append("Open files\n");
        } else if (ScopeType.CHANGELIST_FILES.equals(type)) {
            ret.append("Changelist files\n");
        } else if (ScopeType.SELECTED_FILES.equals(type)) {
            ret.append("Selected files\n");
        }

        files.forEach(n -> {
            ret.append("\t");
            ret.append(n.getCanonicalPath());
            ret.append("\n");
        });
        return ret.toString();
    }

    public enum ScopeType {
        PROJECT_FILES,
        MODULE_FILES,
        PACKAGE_FILES,
        CHANGELIST_FILES,
        SELECTED_FILES,
        OPEN_FILES;
    }
}
