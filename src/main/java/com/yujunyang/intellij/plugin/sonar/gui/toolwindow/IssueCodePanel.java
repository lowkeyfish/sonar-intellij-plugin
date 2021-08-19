package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.FileDescriptor;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

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

//    public void show(List<? extends AbstractIssue> issues) {
//        AbstractIssue issue = issues.get(0);
//        if (issue.getPsiFile() != lastPsiFile && editor != null) {
//            EditorFactory.getInstance().releaseEditor(editor);
//            editor = null;
//            lastPsiFile = null;
//            removeAll();
//        }
//
//        if (editor == null) {
//            JBLabel filePathLabel = new JBLabel(issue.getPsiFile().getName(), issue.getPsiFile().getIcon(Iconable.ICON_FLAG_READ_STATUS), SwingConstants.LEFT);
//            filePathLabel.setBorder(JBUI.Borders.empty(5));
//            add(filePathLabel, BorderLayout.NORTH);
//
//            lastPsiFile = issue.getPsiFile();
//            editor = createEditor(lastPsiFile);
//            JComponent component = editor.getComponent();
//            add(component, BorderLayout.CENTER);
//        }
//
//        // 使用红框标出问题代码行
//        editor.getMarkupModel().removeAllHighlighters();
//
//        for (int i = 0; i < issues.size(); i++) {
//            addRangeHighlighter(issues.get(i), editor);
//        }
//
////        // 必要，否则界面部分将无法正常显示
////        revalidate();
////        repaint();
//
//        // 打开源文件并定位到问题代码
//        UIUtils.navigateToOffset(issue.getPsiFile(), issue.getTextRange().getStartOffset());
//
//        // 滚动定位到第一个问题代码（重复块可能有多个）
////        editor.getCaretModel().moveToOffset(issue.getTextRange().getStartOffset());
////        editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
//
//        LogicalPosition pos = new LogicalPosition(editor.getDocument().getLineNumber(issue.getTextRange().getStartOffset()), 0);
//        editor.getCaretModel().moveToLogicalPosition(pos);
//        editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
//    }

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





        LogicalPosition pos = new LogicalPosition(70, 0);
        e.getScrollingModel().scrollTo(pos, ScrollType.MAKE_VISIBLE);

        JComponent component = e.getComponent();
        add(component, BorderLayout.CENTER);

        // 打开源文件并定位到问题代码
        UIUtils.navigateToOffset(issue.getPsiFile(), issue.getTextRange().getStartOffset());
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
        document.setReadOnly(true);
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
