package com.yujunyang.intellij.plugin.sonar.common;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiLambdaExpression;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IdeaUtils {

	private IdeaUtils() {
	}


	/**
	 * Get the base path of the project.
	 *
	 * @param project the open idea project
	 * @return the base path of the project.
	 */
	@Nullable
	public static File getProjectPath(@Nullable final Project project) {
		if (project == null) {
			return null;
		}

		final VirtualFile baseDir = project.getBaseDir();
		if (baseDir == null) {
			return null;
		}

		return new File(baseDir.getPath());
	}


	@Nullable
	public static Project getProject(@NotNull final DataContext dataContext) {
		return PlatformDataKeys.PROJECT.getData(dataContext);
	}


	public static Project getProject(final PsiElement psiElement) {
		return psiElement.getProject();
	}


	/**
	 * Locates the current PsiFile.
	 *
	 * @param dataContext The IntelliJ DataContext (can usually be obtained from the action-event).
	 * @return The current PsiFile or null if not found.
	 */
	@Nullable
	private static PsiFile getPsiFile(@NotNull final DataContext dataContext) {
		return PlatformDataKeys.PSI_FILE.getData(dataContext);
	}


	@Nullable
	public static PsiFile getPsiFile(@Nullable final PsiElement psiClass) {
		if (psiClass == null) {
			return null;
		}
		return psiClass.getContainingFile();
	}


	@Nullable
	private static VirtualFile getSelectedFile(@NotNull final DataContext dataContext) {
		final VirtualFile[] selectedFiles = getSelectedFiles(dataContext);
		if (selectedFiles.length == 0) {
			return null;
		} else {
			return selectedFiles[0];
		}
	}


	@NotNull
	private static VirtualFile[] getSelectedFiles(@NotNull final DataContext dataContext) {
		final Project project = getProject(dataContext);
		return FileEditorManager.getInstance(project).getSelectedFiles();
	}


	@NotNull
	public static List<VirtualFile> getAllModifiedFiles(@NotNull final DataContext dataContext) {
		final Project project = getProject(dataContext);
		final ChangeListManager changeListManager = ChangeListManager.getInstance(project);
		return changeListManager.getAffectedFiles();
	}

	@Nullable
	public static VirtualFile[] getVirtualFiles(@NotNull final DataContext dataContext) {
		return PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
	}


	@Nullable
	public static VirtualFile getVirtualFile(@NotNull final DataContext dataContext) {
		return PlatformDataKeys.VIRTUAL_FILE.getData(dataContext);
	}

	@NotNull
	public static String getPackage(@NotNull final PsiElement psiElement) {
		return ((PsiClassOwner) psiElement.getContainingFile()).getPackageName();
	}


	@NotNull
	public static String getPackageUrl(@NotNull final PsiElement psiElement) {
		return getPackage(psiElement).replace('.', '/');
	}

	@Nullable
	public static Module getModule(@NotNull final DataContext dataContext, @NotNull final Project project) {
		final VirtualFile selectedFile = getSelectedFile(dataContext);
		Module module = null;
		if (selectedFile != null) {
			module = ModuleUtilCore.findModuleForFile(selectedFile, project);
		}

		if (module == null) {
			module = LangDataKeys.MODULE.getData(dataContext);
		}

		if (module == null) {
			final Module[] modules = getModules(project);
			if (modules.length > 0) {
				module = modules[0];
			}
		}

		return module;
	}


	@NotNull
	private static Module[] getModules(final Project project) {
		final ModuleManager moduleManager = ModuleManager.getInstance(project);
		return moduleManager.getModules();
	}


	/**
	 * Retrieves the current PsiElement.
	 *
	 * @param dataContext The IntelliJ DataContext (can usually be obtained from the action-event).
	 * @return The current PsiElement or null if not found.
	 */
	@Nullable
	private static PsiElement getCurrentElement(@NotNull final DataContext dataContext) {

		/*
		 * Do not use "psi.Element" because this element could be contextless.
		 */
		//final PsiElement psiElement = (PsiElement) dataContext.getData("psi.Element");

		final Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
		if (editor != null) {
			final PsiFile psiFile = getPsiFile(dataContext);
			if (psiFile != null) {
				return psiFile.findElementAt(editor.getCaretModel().getOffset());
			}
		}
		return null;
	}


	/**
	 * Retrieves the current PsiClass.
	 *
	 * @param dataContext The IntelliJ DataContext (can usually be obtained from the action-event).
	 * @return The current PsiClass or null if not found.
	 */
	@Nullable
	public static PsiClass getCurrentClass(@NotNull final DataContext dataContext) {
		return findClass(getCurrentElement(dataContext));
	}


	/**
	 * Retrieves the current PsiMethod.
	 *
	 * @param dataContext The IntelliJ DataContext (can usually be obtained from the action-event).
	 * @return The current PsiMethod or null if not found.
	 */
	@SuppressWarnings("UnusedDeclaration")
	@Nullable
	public static PsiMethod getCurrentMethod(@NotNull final DataContext dataContext) {
		return findMethod(getCurrentElement(dataContext));
	}


	/**
	 * Finds the PsiClass for a specific PsiElement.
	 *
	 * @param element The PsiElement to locate the class for.
	 * @return The PsiClass you're looking for or null if not found.
	 */
	@Nullable
	private static PsiClass findClass(final PsiElement element) {
		final PsiClass psiClass = element instanceof PsiClass ? (PsiClass) element : PsiTreeUtil.getParentOfType(element, PsiClass.class);
		if (psiClass instanceof PsiAnonymousClass) {
			//noinspection TailRecursion
			return findClass(psiClass.getParent());
		}
		return psiClass;
	}

	/**
	 * Finds the PsiMethod for a specific PsiElement.
	 *
	 * @param element The PsiElement to locate the method for.
	 * @return The PsiMethod you're looking for or null if not found.
	 */
	@Nullable
	private static PsiMethod findMethod(final PsiElement element) {
		final PsiMethod method = element instanceof PsiMethod ? (PsiMethod) element : PsiTreeUtil.getParentOfType(element, PsiMethod.class);
		if (method != null && method.getContainingClass() instanceof PsiAnonymousClass) {
			//noinspection TailRecursion
			return findMethod(method.getParent());
		}
		return method;
	}

	@SuppressWarnings("UnusedDeclaration")
	@Nullable
	public static VirtualFile findFileByIoFile(final File file) {
		return LocalFileSystem.getInstance().findFileByIoFile(file);
	}

	/**
	 * Find a PsiClass using {@link GlobalSearchScope#allScope(Project)} }
	 *
	 * @param project   the idea project to search in
	 * @param classname like java/lang/Object.java or java.lang.Object.java or without file extension
	 * @return the PsiClass element
	 */
	@Nullable
	public static PsiClass findJavaPsiClass(@NotNull final Project project, @NotNull final String classname) {
		final String fqn = removeExtension(classname);
		final String dottedName = fqn.contains("/") ? fqn.replace('/', '.') : fqn;
		final GlobalSearchScope scope = GlobalSearchScope.allScope(project);
		return findJavaPsiClass(project, dottedName, scope);
	}

	@Nullable
	public static PsiClass findJavaPsiClass(@NotNull final Project project, @Nullable final Module module, @NotNull final String classname) {
		if (module == null) {
			return findJavaPsiClass(project, classname);
		}
		final String fqn = removeExtension(classname);
		final String dottedName = fqn.contains("/") ? fqn.replace('/', '.') : fqn;
		final GlobalSearchScope scope = GlobalSearchScope.moduleScope(module);
		return findJavaPsiClass(project, dottedName, scope);
	}

	public static String removeExtension(@NotNull final String name) {
		int pos = name.lastIndexOf('.');
		if (pos != -1) {
			return name.substring(0, pos);
		}
		return name;
	}

	@Nullable
	private static PsiClass findJavaPsiClass(final Project project, @NotNull final String dottedFqClassName, @NotNull final GlobalSearchScope searchScope) {
		/*
		 * Do not use findClass(), the returned class is "random" (eg: could be ClsClassImpl or PsiClassImpl), see javadoc
		 */
		final PsiClass[] classes = JavaPsiFacade.getInstance(project).findClasses(dottedFqClassName, searchScope);
		if (classes.length > 0) {
			if (classes.length > 1) {
				for (final PsiClass c : classes) {
					if (!(c instanceof PsiCompiledElement)) { // prefer non compiled
						return c;
					}
				}
			}
			return classes[0];
		}
		return null;
	}


	@Nullable
	public static PsiElement getElementAtLine(@NotNull final PsiFile file, final int line) {
		//noinspection ConstantConditions
		if (file == null) {
			return null;
		}
		final Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
		PsiElement element = null;
		try {
			if (document != null) {
				final int offset = document.getLineStartOffset(line);
				element = file.getViewProvider().findElementAt(offset);
				if (element != null) {
					if (document.getLineNumber(element.getTextOffset()) != line) {
						PsiElement prevSibling = element.getPrevSibling();
						if (prevSibling instanceof PsiComment && element.getParent() instanceof PsiClass || prevSibling instanceof PsiModifierList) {
							element = element.getParent();
						} else if (prevSibling instanceof PsiAnnotation) {
							element = element.getParent().getParent();
						} else {
							element = element.getNextSibling();
						}
					} else if (element.getParent() instanceof PsiModifierList) {
						element = element.getParent().getParent();
					} else if (element.getPrevSibling() instanceof PsiModifierList && element.getParent() instanceof PsiClass) {
						element = element.getParent();
					}
				}
			}
		} catch (@NotNull final IndexOutOfBoundsException ignore) {
		}

		return element instanceof PsiNameIdentifierOwner ? ((PsiNameIdentifierOwner) element).getNameIdentifier() : element;
	}

	@NotNull
	public static FileType getFileTypeByName(@NotNull final String filename) {
		return FileTypeManager.getInstance().getFileTypeByFileName(filename);
	}

//	@Nullable
//	private static PsiFile getPsiFile(@NotNull final Project project, @NotNull final ExtendedProblemDescriptor problem) {
//		final PsiFile file = problem.getPsiFile();
//		return PsiManager.getInstance(project).findFile(file.getVirtualFile());
//	}
//
//
//	@SuppressWarnings("UnusedDeclaration")
//	@Nullable
//	public static Document getDocument(@NotNull final Project project, @NotNull final ExtendedProblemDescriptor issue) {
//		final PsiFile psiFile = getPsiFile(project, issue);
//		return psiFile == null ? null : PsiDocumentManager.getInstance(project).getDocument(psiFile);
//	}
//
//	@Nullable
//	public static PsiElement findClassPsiElement(@Nullable final PsiFileSystemItem psiFile, @NotNull final BugInstance bugInstance, @NotNull final Project project) {
//		if (psiFile != null) {
//			final String classNameToFind = BugInstanceUtil.getSimpleClassName(bugInstance);
//			final ClassCollector cc = new ClassCollector(project);
//			cc.addContainingClasses(psiFile.getVirtualFile());
//			final Map<String, PsiElement> classes = cc.getClasses();
//
//			for (final Entry<String, PsiElement> entry : classes.entrySet()) {
//				final String fileName = new File(entry.getKey()).getName();
//				if (fileName.equals(classNameToFind + AbstractClassAdder.CLASS_FILE_SUFFIX)) {
//					return entry.getValue();
//				}
//			}
//		}
//		return null;
//	}
//
//	@Nullable
//	public static PsiElement findPsiElement(@Nullable final PsiFileSystemItem psiFile, @NotNull final BugInstance bugInstance, @NotNull final Project project) {
//		if (psiFile == null) {
//			return null;
//		}
//		PsiElement result = findClassPsiElement(psiFile, bugInstance, project);
//		final FieldAnnotation primaryField = bugInstance.getPrimaryField();
//		if (result != null && primaryField != null) {
//			result =  ((PsiClass) result).findFieldByName(primaryField.getFieldName(), false);
//		}
//		// FIXME: add finding the method
//		if (result == null) {
//			return null;
//		}
//		return result instanceof PsiAnonymousClass ? result : ((PsiNameIdentifierOwner) result).getNameIdentifier();
//	}

	@Nullable
	public static String getFirstProjectRootPath(final Project project) {
		final ProjectRootManager projectManager = ProjectRootManager.getInstance(project);
		final VirtualFile[] rootFiles = projectManager.getContentRoots();
		if (rootFiles.length == 0) {
			return null;
		}
		return rootFiles[0].getPath();
	}

	@Deprecated
	public static String collapsePathMacro(final ComponentManager project, @NotNull final String path) {
		final PathMacroManager macroManager = PathMacroManager.getInstance(project);
		return macroManager.collapsePath(path);
	}

	@Deprecated
	public static String expandPathMacro(final ComponentManager project, @NotNull final String path) {
		final PathMacroManager macroManager = PathMacroManager.getInstance(project);
		return macroManager.expandPath(path);
	}

	public static PsiElement findOnlyLambdaExpressionOrPsiElement(@NotNull final PsiElement psiElement) {
		final PsiLambdaExpression[] lastLambdaExpression = {null};
		final int[] lambdaCount = {0};

		psiElement.accept(new JavaRecursiveElementVisitor() {
			@Override
			public void visitLambdaExpression(PsiLambdaExpression lambdaExpression) {
				super.visitLambdaExpression(lambdaExpression);
				lastLambdaExpression[0] = lambdaExpression;
				++lambdaCount[0];
			}
		});
		return lambdaCount[0] == 1 ? lastLambdaExpression[0] : psiElement;
	}

	public static String getPluginVersion() {
		return PluginManager.getPlugin(PluginId.getId(PluginConstants.PLUGIN_ID)).getVersion();
	}

	public static String getFullClassPath(Project project) {
		String fullClassPath;
		if (EventQueue.isDispatchThread()) {
			fullClassPath = OrderEnumerator.orderEntries(project).recursively().getPathsList().getPathsString();
		} else {
			fullClassPath = ApplicationManager.getApplication().runReadAction((Computable<String>)() ->
				OrderEnumerator.orderEntries(project).recursively().getPathsList().getPathsString()
			);
		}
		List<String> ret = new ArrayList<>();
		Arrays.stream(fullClassPath.split(";")).forEach(n -> {
			if (n.endsWith(".jar")) {
				ret.add(n);
			}
		});
		return String.join(",", ret);
	}

	public static String getAllCompilerOutPath(Project project) {
		List<String> ret = new ArrayList<>();
		Module[] modules = ModuleManager.getInstance(project).getModules();
		for (Module module : modules) {
			CompilerModuleExtension compilerModuleExtension = CompilerModuleExtension.getInstance(module);
			VirtualFile compilerOutPath = compilerModuleExtension.getCompilerOutputPath();
			if (compilerOutPath != null) {
				ret.add(compilerOutPath.getCanonicalPath());
			}
		}
		return String.join(",", ret);
	}

	public static String getAllSourceRootPath(Project project) {
		List<String> ret = new ArrayList<>();
		Module[] modules = ModuleManager.getInstance(project).getModules();
		for (Module module : modules) {
			ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
			VirtualFile[] sourceRoots = rootManager.getSourceRoots(false);
			for (VirtualFile sourceRoot : sourceRoots) {
				ret.add(sourceRoot.getCanonicalPath());
			}
		}
		return String.join(",", ret);
	}

	public static PsiFile getPsiFile(Project project, File file) {
		return PsiManager.getInstance(project).findFile(findFileByIoFile(file));
	}

}
