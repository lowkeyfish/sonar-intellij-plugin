
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
