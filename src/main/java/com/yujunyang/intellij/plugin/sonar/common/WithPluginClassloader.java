
package com.yujunyang.intellij.plugin.sonar.common;

import com.intellij.openapi.util.NotNullComputable;
import com.intellij.openapi.util.Throwable2Computable;
import com.yujunyang.intellij.plugin.sonar.extensions.ToolWindowFactoryImpl;
import org.jetbrains.annotations.NotNull;

/**
 * This must be used when FindBugs will create a SAXReader because this use the default DocumentFactory instance
 * (org.dom4j.DocumentFactory#getInstance) which is created by createSingleton which uses Class.forName.
 * <p>
 * Note that all our classes (incl transitive) must be loaded by the PluginClassLoader instance of SpotBugs-IDEA plugin.
 * Check: DebugUtil.dumpClasses(TheClass.class);
 */
public final class WithPluginClassloader {
	@NotNull
	public static final ClassLoader PLUGIN_CLASS_LOADER = ToolWindowFactoryImpl.class.getClassLoader();

	private WithPluginClassloader() {
	}

	public static <V, E extends Throwable, E2 extends Throwable> V compute(@NotNull final Throwable2Computable<V, E, E2> computable) throws E, E2 {
		final Thread currentThread = Thread.currentThread();
		final ClassLoader cl = currentThread.getContextClassLoader();
		try {
			currentThread.setContextClassLoader(PLUGIN_CLASS_LOADER);
			return computable.compute();
		} finally {
			currentThread.setContextClassLoader(cl);
		}
	}

	@NotNull
	public static <V> V notNull(@NotNull final NotNullComputable<V> computable) {
		final Thread currentThread = Thread.currentThread();
		final ClassLoader cl = currentThread.getContextClassLoader();
		try {
			currentThread.setContextClassLoader(PLUGIN_CLASS_LOADER);
			return computable.compute();
		} finally {
			currentThread.setContextClassLoader(cl);
		}
	}
}
