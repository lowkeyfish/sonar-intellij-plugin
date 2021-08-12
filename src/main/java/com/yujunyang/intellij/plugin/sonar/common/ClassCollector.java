
package com.yujunyang.intellij.plugin.sonar.common;


import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public final class ClassCollector extends AbstractClassAdder {

	private final Map<String, PsiElement> _classes;


	public ClassCollector(@NotNull final Project project) {
		super(project);
		_classes = new HashMap<>();
	}


	@Override
	void put(@NotNull final String fqp, @NotNull final PsiElement element) {
		_classes.put(fqp + CLASS_FILE_SUFFIX, element);
	}


	@NotNull
	public Map<String, PsiElement> getClasses() {
		return _classes;
	}
}