
package com.yujunyang.intellij.plugin.sonar.common;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;

class InnerClassPsiElementFilter implements PsiElementFilter {

	private final PsiElement _psiElement;


	InnerClassPsiElementFilter(final PsiElement psiElement) {
		_psiElement = psiElement;
	}


	public boolean isAccepted(final PsiElement e) {
		return e instanceof PsiClass && _psiElement.equals(PsiTreeUtil.getParentOfType(e, PsiClass.class));
	}


	@Override
	public String toString() {
		return "InnerClassPsiElementFilter{psiElement=" + _psiElement + '}';
	}
}
