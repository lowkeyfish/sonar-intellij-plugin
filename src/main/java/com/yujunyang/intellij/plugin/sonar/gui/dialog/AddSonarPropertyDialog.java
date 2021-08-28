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

package com.yujunyang.intellij.plugin.sonar.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.Consumer;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.yujunyang.intellij.plugin.sonar.gui.error.ErrorPainter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddSonarPropertyDialog extends DialogWrapper {
    private static final List<String> VALID_PROPERTIES = Arrays.asList("sonar.exclusions", "sonar.cpd.exclusions");

    private ErrorPainter errorPainter;
    private JBTextField nameField;
    private JBTextArea valueTextArea;
    private List<String> existNames;
    private boolean edit;
    private Consumer<Pair<String, String>> saveConsumer;


    public AddSonarPropertyDialog(Consumer<Pair<String, String>> saveConsumer) {
        super(true);
        this.existNames = new ArrayList<>();
        this.saveConsumer = saveConsumer;

        init();
        setTitle("Add SonarScanner property");
        setResizable(true);
        getContentPanel().setBorder(JBUI.Borders.empty());
        ((JComponent)getContentPanel().getComponent(1)).setBorder(JBUI.Borders.empty(0, 12, 8, 12));
    }

    public void initProperty(String name, String value) {
        this.edit = true;
        setTitle("Edit SonarScanner property");
        this.nameField.setText(name);
        this.nameField.setEnabled(false);
        this.valueTextArea.setText(value);
    }

    public void setExistNames(List<String> existNames) {
        this.existNames = existNames;
    }



    @Override
    protected @Nullable JComponent createCenterPanel() {
        JBPanel content = new JBPanel();
        content.setPreferredSize(new Dimension(440, content.getPreferredSize().height));
        content.setBorder(JBUI.Borders.empty(20, 20));
        content.setLayout(new BorderLayout());

        errorPainter = new ErrorPainter();
        errorPainter.installOn((JPanel) getContentPanel(), () -> {
        });

        JBPanel formPanel = new JBPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JBLabel("Name: "), c);
        c.gridx = 1;
        c.weightx = 1;
        nameField = createFiled("");
        formPanel.add(nameField, c);

        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 0;
        c.insets = new Insets(5, 0, 0, 0);
        formPanel.add(new JBLabel("Value: "), c);
        c.gridx = 1;
        c.weightx = 1;
        c.ipady = 100;
        valueTextArea = new JBTextArea("");
        valueTextArea.setFont(UIUtil.getLabelFont());
        valueTextArea.setLineWrap(true);
        valueTextArea.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.setViewportView(valueTextArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 1, 0, 1),
                BorderFactory.createLineBorder(UIUtil.getBoundsColor(), 1)
        ));
        scrollPane.setBackground(UIUtil.getPanelBackground());
        formPanel.add(scrollPane, c);

        content.add(formPanel, BorderLayout.CENTER);

        return content;
    }

    private JBTextField createFiled(String defaultText) {
        JBTextField textField = new JBTextField(defaultText);
        textField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                errorPainter.setValid(textField, !StringUtil.isEmptyOrSpaces(textField.getText()));
            }
        });
        errorPainter.setValid(textField, !StringUtil.isEmptyOrSpaces(textField.getText()));
        return textField;
    }


    @NotNull
    @Override
    protected Action[] createActions() {
        DialogWrapper that = this;
        Action saveAction = new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        };
        Action closeAction = new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                that.close(0);
            }
        };
        return new Action[] { saveAction, closeAction };
    }

    private void save() {
        String name = nameField.getText().trim();
        if (StringUtil.isEmptyOrSpaces(name)) {
            return;
        }

        String value = valueTextArea.getText().trim();

        if (!edit && existNames.contains(name)) {
            Messages.showDialog("There is already a property with that name. Please choose another name", "Error", new String[] { "Ok" }, 0, Messages.getErrorIcon());
            return;
        }

        if (!VALID_PROPERTIES.contains(name)) {
            Messages.showDialog(String.format("Only [%s] is supported for property names",
                    String.join(", ", VALID_PROPERTIES)), "Error", new String[] { "Ok" }, 0, Messages.getErrorIcon());
            return;
        }

        saveConsumer.consume(new Pair<>(name, value));
        close(0);
    }
}
