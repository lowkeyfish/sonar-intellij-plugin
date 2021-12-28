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

package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.FileDescriptor;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.Alarm;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;

public class IssueCodePanel extends JBPanel {
    private Project project;
    private Editor editor;
    private PsiFile lastPsiFile;

    public IssueCodePanel(Project project) {
        this.project = project;
        init();
    }

    public void show(List<? extends AbstractIssue> issues) {
        AbstractIssue issue = issues.get(0);
        removeAll();

        JBLabel filePathLabel = new JBLabel(issue.getPsiFile().getName(), issue.getPsiFile().getIcon(Iconable.ICON_FLAG_READ_STATUS), SwingConstants.LEFT);
        filePathLabel.setBorder(JBUI.Borders.empty(5));
        add(filePathLabel, BorderLayout.NORTH);

        Editor e = createEditor(issue.getPsiFile());

        // 使用红框标出问题代码行
        e.getMarkupModel().removeAllHighlighters();
        for (int i = 0; i < issues.size(); i++) {
            addRangeHighlighter(issues.get(i), e);
        }

        JComponent component = e.getComponent();
        add(component, BorderLayout.CENTER);

        // 立即调用问题代码的定位会存在不能准确滚动到问题行的问题
        // 用invokeLater解决了
        SwingUtilities.invokeLater(() -> {
            e.getCaretModel().moveToOffset(issue.getTextRange().getStartOffset());
            e.getScrollingModel().scrollToCaret(ScrollType.CENTER);
        });

    }

    private void addRangeHighlighter(AbstractIssue issue, Editor editor) {
        TextRange textRange = issue.getTextRange();
        RangeMarker marker = editor.getDocument().createRangeMarker(textRange.getStartOffset(), textRange.getEndOffset());
        editor.getMarkupModel().addRangeHighlighter(
                marker.getStartOffset(),
                marker.getEndOffset(),
                HighlighterLayer.FIRST - 1,
                new TextAttributes(null, null, JBColor.RED, EffectType.BOXED, Font.BOLD),
                HighlighterTargetArea.EXACT_RANGE);
    }

    private void init() {
        setLayout(new BorderLayout());
    }

    private Editor createEditor(PsiFile psiFile) {
        final Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
        final Editor editor = EditorFactory.getInstance().createEditor(document, project, psiFile.getFileType(), false);
        editor.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, JBColor.border()));

        final EditorSettings editorSettings = editor.getSettings();
        editorSettings.setLineMarkerAreaShown(true);
        editorSettings.setLineNumbersShown(true);
        editorSettings.setFoldingOutlineShown(true);
        editorSettings.setAnimatedScrolling(true);
        editorSettings.setWheelFontChangeEnabled(true);
        editorSettings.setVariableInplaceRenameEnabled(true);

        return editor;
    }
}
