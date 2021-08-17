package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import org.jetbrains.annotations.NotNull;

public class IssueCodePanel extends JBPanel {
    private Project project;
    private Editor editor;
    private PsiFile lastPsiFile;

    public IssueCodePanel(Project project) {
        this.project = project;
        init();
    }

    public void refresh(@NotNull AbstractIssue issue) {
        // TODO: 1.定位 2.editor偶尔不显示 3.重复块

        if (issue.getPsiFile() != lastPsiFile && editor != null) {
            EditorFactory.getInstance().releaseEditor(editor);
            editor = null;
            lastPsiFile = null;
        }

        if (editor == null) {
            removeAll();

            JBTextArea filePathTextArea = UIUtils.createWrapLabelLikedTextArea(IdeaUtils.getPath(issue.getPsiFile()));
            filePathTextArea.setBorder(JBUI.Borders.empty(5));
            add(filePathTextArea, BorderLayout.NORTH);

            lastPsiFile = issue.getPsiFile();
            editor = createEditor(lastPsiFile);
            JComponent component = editor.getComponent();
            add(component, BorderLayout.CENTER);
        }

        editor.getMarkupModel().removeAllHighlighters();

        TextRange textRange = issue.getTextRange();
        RangeMarker marker = editor.getDocument().createRangeMarker(textRange.getStartOffset(), textRange.getEndOffset());
        editor.getMarkupModel().addRangeHighlighter(
                marker.getStartOffset(),
                marker.getEndOffset(),
                HighlighterLayer.FIRST - 1,
                new TextAttributes(null, null, JBColor.RED, EffectType.BOXED, Font.BOLD),
                HighlighterTargetArea.EXACT_RANGE);

        editor.getScrollingModel().scrollVertically(issue.getTextRange().getStartOffset());
    }

    private void init() {
        setLayout(new BorderLayout());
    }

    private Editor createEditor(PsiFile psiFile) {
        final Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
        final Editor editor = EditorFactory.getInstance().createEditor(document, project, psiFile.getFileType(), false);
        editor.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, JBColor.border()));
//        final EditorColorsScheme scheme = editor.getColorsScheme();
//        scheme.setEditorFontSize(scheme.getEditorFontSize() - 1);

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
