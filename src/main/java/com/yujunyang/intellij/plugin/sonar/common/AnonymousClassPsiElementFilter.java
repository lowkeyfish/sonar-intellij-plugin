
package com.yujunyang.intellij.plugin.sonar.common;

import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;

class AnonymousClassPsiElementFilter implements PsiElementFilter {

	private final PsiElement _psiElement;


	AnonymousClassPsiElementFilter(final PsiElement psiElement) {
		_psiElement = psiElement;
	}


	public boolean isAccepted(final PsiElement e) {
		return e instanceof PsiAnonymousClass && _psiElement.equals(PsiTreeUtil.getParentOfType(e, PsiClass.class));
	}


	@Override
	public String toString() {
		return "AnonymousClassPsiElementFilter{psiElement=" + _psiElement + '}';
	}
}
