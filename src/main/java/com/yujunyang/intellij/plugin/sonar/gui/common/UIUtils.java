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

package com.yujunyang.intellij.plugin.sonar.gui.common;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiFile;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;


public final class UIUtils {
    public static void addLabel(JComponent parent, String text) {
        JBLabel label = new JBLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
    }

    public static JBTextArea addTextArea(JComponent parent, String text, int maxHeight) {
        JBTextArea textArea = new JBTextArea();
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textArea.setText(text);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        JBScrollPane textAreaScrollPane = new JBScrollPane(textArea);
        textAreaScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        textAreaScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxHeight));
        textAreaScrollPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, maxHeight));
        textAreaScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, maxHeight));
        parent.add(textAreaScrollPane);
        return textArea;
    }

    public static ComboBox addLanguageComboBox(JComponent parent) {
        List<String> languages = new ArrayList<>();
        languages.add("Java");
        languages.add("YAML");
        languages.add("JSON");
        languages.add("XML");
        languages.add("Properties");
        languages.add("SQL");
        languages.add("Groovy");
        languages.add("Kotlin");
        languages.add("其他");

        ComboBox comboBox = new ComboBox();
        languages.forEach(n -> {
            comboBox.addItem(n);
        });
        comboBox.setEditable(true);
        parent.add(comboBox);
        return comboBox;
    }

    public static Editor addEditor(JComponent parent, String code, boolean readOnly, String fileType) {
        Document document = EditorFactory.getInstance().createDocument(code);
        document.setReadOnly(readOnly);

        Editor editor = EditorFactory.getInstance().createEditor(document);
        JComponent component = editor.getComponent();
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        component.setMinimumSize(new Dimension(Integer.MAX_VALUE, 300));
        component.setPreferredSize(new Dimension(Integer.MAX_VALUE, 300));
        parent.add(component);
        return editor;
    }

    public static Editor createEditor(String code, boolean readOnly, String fileType) {
        Document document = EditorFactory.getInstance().createDocument(code);
        document.setReadOnly(readOnly);

        Editor editor = EditorFactory.getInstance().createEditor(document);
        return editor;
    }

    public static void scrollTo(JBScrollPane pane, int position) {
        switch (position) {
            case SwingConstants.TOP:
                pane.getVerticalScrollBar().setValue(0);
                break;
            case SwingConstants.BOTTOM:
                pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
                break;
            default:
                break;
        }
    }

    public static void setBackgroundRecursively(@NotNull Component component) {
        setBackgroundRecursively(component, UIUtils.backgroundColor());
    }

    public static void setBackgroundRecursively(@NotNull Component component, Color color) {
        if (component instanceof JBPanel || component instanceof JBScrollPane || component instanceof JBTextArea) {
            component.setBackground(color);
        }

        if (component instanceof Container) {
            for (Component c : ((Container)component).getComponents()) {
                setBackgroundRecursively(c, color);
            }
        }
    }

    public static JBTextArea addAndReturnTitledSeparatorTextAreaToBoxLayoutParent(JComponent parent, String title, String text) {
        addTitledSeparator(parent, title);
        JBTextArea textArea = createTextArea(text);
        parent.add(textArea);
        return textArea;
    }

    public static JBTextArea createTextArea(String text) {
        JBTextArea textArea = new JBTextArea();
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, UIUtil.isUnderDarcula() ? Color.gray : Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textArea.setText(text);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setAutoscrolls(true);
        return textArea;
    }

    public static TitledSeparator createTitledSeparator(String title) {
        TitledSeparator titledSeparator = new TitledSeparator(title);
        titledSeparator.setBorder(JBUI.Borders.empty(0, 0, 5, 0));
        return titledSeparator;
    }

    public static void addTitledSeparator(JComponent parent, String title) {
        parent.add(createTitledSeparator(title));
    }

    public static ComboBox addAndReturnSnippetTypeComboBox(JComponent parent, String selected) {
        ComboBox typeComboBox = new ComboBox();
        typeComboBox.addItem("POM");
        typeComboBox.addItem("CONFIG");
        typeComboBox.addItem("CODE");
        typeComboBox.setSelectedItem(selected);
        parent.add(typeComboBox);
        return typeComboBox;
    }

    public static ComboBox addAndReturnSnippetCodeLanguageComboBox(JComponent parent, String selected) {
        ComboBox languageComboBox = addLanguageComboBox(parent);
        languageComboBox.setSelectedItem(selected);
        return languageComboBox;
    }

    public static JBPanel addAndReturnScrollContentPane(JComponent parent) {
        parent.setLayout(new BorderLayout());
        JBPanel pane = new JBPanel(new BorderLayout());
        JBScrollPane scrollPane = new JBScrollPane();
        parent.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(pane);

        JBPanel contentPane = new JBPanel();
        contentPane.setBorder(JBUI.Borders.empty(10));
        BoxLayout layout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        contentPane.setLayout(layout);
        pane.add(contentPane, BorderLayout.NORTH);
        return contentPane;
    }

    public static void addPaneWrappedTipLabel(JComponent parent, String text) {
        JBPanel labelPane = new JBPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JBLabel label = new JBLabel(text);
        label.setForeground(Color.GRAY);
        labelPane.add(label);
        parent.add(labelPane);
    }

    public static JBLabel createTagLabel(String tag) {
        JBLabel tagLabel = new JBLabel(tag);
        tagLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor()),
                BorderFactory.createEmptyBorder(0, 2, 0,2)));
        tagLabel.setForeground(UIUtils.labelForegroundColor());
        return tagLabel;
    }

    public static void showErrorDialogInThread(String message) {
        SwingUtilities.invokeLater(() -> {
            Messages.showDialog(message, "提示", new String[] { "确定" }, 0, Messages.getErrorIcon());
        });
    }

    public static void showErrorDialog(String message) {
        Messages.showDialog(message, "提示", new String[] { "确定" }, 0, Messages.getErrorIcon());
    }

    public static int showConfirmDialog(String message) {
        return Messages.showDialog(message, "提示",
                new String[] { "确认", "取消" }, 1, Messages.getQuestionIcon());
    }

    public static void showWarningDialog(String message) {
        Messages.showDialog(message, "提示", new String[] { "确定" }, 0, Messages.getWarningIcon());
    }

    public static Color borderColor() {
        if (UIUtil.isUnderDarcula()) {
            return new Color(81, 81, 81);
        }
        return Color.LIGHT_GRAY;
    }

    public static Color backgroundColor() {
        return UIUtil.isUnderDarcula() ? new Color(49, 51, 53) : Color.WHITE;
    }

    public static Color highlightBackgroundColor() {
        return UIUtil.isUnderDarcula() ? new Color(54, 57, 59) : new Color(245, 249, 255);
    }

    public static Color labelForegroundColor() {
        return UIUtil.isUnderDarcula() ? new Color(140, 140, 140) : Color.GRAY;
    }

    public static FileType getFileType(String snippetNameWithExtension) {
        FileType fileType = FileTypeManager.getInstance().getFileTypeByFileName(snippetNameWithExtension);
        return fileType == UnknownFileType.INSTANCE ? PlainTextFileType.INSTANCE : fileType;
    }


    public static JBPanel createBorderLayoutPanel() {
        JBPanel panel = new JBPanel(new BorderLayout());
        return panel;
    }

    public static JBPanel wrappedInBorderLayoutPanel(JComponent component) {
        return wrappedInBorderLayoutPanel(component, BorderLayout.WEST);
    }

    public static JBPanel wrappedInBorderLayoutPanel(JComponent component, Object constraints) {
        JBPanel panel = createBorderLayoutPanel();
        panel.add(component, constraints);
        return panel;
    }

    public static JBPanel wrappedInBorderLayoutPanel(Pair<JComponent, Object>... components) {
        JBPanel panel = createBorderLayoutPanel();
        for (int i = 0; i < components.length; i++) {
            Pair<JComponent, Object> component = components[i];
            panel.add(component.first, component.second);
        }
        return panel;
    }

    public static JBLabel createHorizontalAlignmentCenterLabel(String text) {
        JBLabel ret = new JBLabel(text);
        ret.setHorizontalAlignment(SwingConstants.CENTER);
        return ret;
    }

    public static JBLabel createHorizontalAlignmentCenterLabel(String text, Font font) {
        JBLabel ret = new JBLabel(text);
        ret.setHorizontalAlignment(SwingConstants.CENTER);
        ret.setFont(font);
        return ret;
    }

    public static JBPanel createBoxLayoutPanel(int axis) {
        JBPanel panel = new JBPanel();
        BoxLayout layout = new BoxLayout(panel, axis);
        panel.setLayout(layout);
        return panel;
    }

    public static Pair<String, Icon> typeInfo(String type) {
        switch (type) {
            case "BUG":
                return new Pair<>("Bug", PluginIcons.BUGS);
            case "VULNERABILITY":
                return new Pair<>("漏洞", PluginIcons.VULNERABILITY);
            case "CODE_SMELL":
                return new Pair<>("异味", PluginIcons.CODE_SMELL);
            default:
                return new Pair<>("", null);
        }
    }

    public static Pair<String, Icon> severityInfo(String severity) {
        switch (severity) {
            case "BLOCKER":
                return new Pair<>("阻断", PluginIcons.BLOCKER);
            case "CRITICAL":
                return new Pair<>("严重", PluginIcons.CRITICAL);
            case "MAJOR":
                return new Pair<>("主要", PluginIcons.MAJOR);
            case "MINOR":
                return new Pair<>("次要", PluginIcons.MINOR);
            case "INFO":
                return new Pair<>("提示", PluginIcons.INFO);
            default:
                return new Pair<>("", null);
        }
    }

    public static JBTextArea createWrapLabelLikedTextArea(String text) {
        JBTextArea textArea = new JBTextArea(text);
        textArea.setFont(UIUtil.getLabelFont());
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
//        textArea.setOpaque(true);
//        textArea.setBackground(UIUtils.backgroundColor());
        return textArea;
    }

    public static void navigateToOffset(PsiFile psiFile, int offset) {
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(psiFile.getProject(), psiFile.getVirtualFile(), offset);
        openFileDescriptor.navigate(true);
    }

    public static void navigateToLine(PsiFile psiFile, int line) {
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(psiFile.getProject(), psiFile.getVirtualFile(), line, 0);
        openFileDescriptor.navigate(true);
    }

}
