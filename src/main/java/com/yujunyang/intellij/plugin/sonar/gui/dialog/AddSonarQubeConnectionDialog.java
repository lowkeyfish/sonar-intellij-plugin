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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.BooleanFunction;
import com.intellij.util.Consumer;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.api.SonarApiImpl;
import com.yujunyang.intellij.plugin.sonar.common.EventDispatchThreadHelper;
import com.yujunyang.intellij.plugin.sonar.common.exceptions.ApiRequestFailedException;
import com.yujunyang.intellij.plugin.sonar.common.exceptions.AuthorizationException;
import com.yujunyang.intellij.plugin.sonar.config.SonarQubeSettings;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.gui.error.ErrorPainter;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddSonarQubeConnectionDialog extends DialogWrapper {
    private ErrorPainter errorPainter;
    private JBTextField nameField;
    private JBTextField urlField;
    private JBTextField tokenField;
    private List<String> existNames;
    private boolean edit;
    private Consumer<SonarQubeSettings> saveConsumer;


    public AddSonarQubeConnectionDialog(Consumer<SonarQubeSettings> saveConsumer) {
        super(true);
        this.existNames = new ArrayList<>();
        this.saveConsumer = saveConsumer;

        init();
        setTitle("Add SonarQube connection");
        setResizable(false);
        getContentPanel().setBorder(JBUI.Borders.empty());
        ((JComponent)getContentPanel().getComponent(1)).setBorder(JBUI.Borders.empty(0, 12, 8, 12));
    }

    public void initConnection(String name, String url, String token) {
        this.edit = true;
        setTitle("Edit SonarQube connection");
        this.nameField.setText(name);
        this.nameField.setEnabled(false);
        this.urlField.setText(url);
        this.tokenField.setText(token);
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

        JBLabel sonarQubeLogoLabel = new JBLabel(PluginIcons.SONAR_QUBE);
        sonarQubeLogoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(UIUtils.wrappedInBorderLayoutPanel(sonarQubeLogoLabel, BorderLayout.CENTER), BorderLayout.NORTH);

        errorPainter = new ErrorPainter();
        errorPainter.installOn((JPanel) getContentPanel(), () -> {
        });

        JBPanel formPanel = new JBPanel(new GridBagLayout());
        formPanel.setBorder(JBUI.Borders.empty(20, 0, 20, 0));
        GridBagConstraints c = new GridBagConstraints();

        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JBLabel("Name: "), c);
        c.gridx = 1;
        c.weightx = 1;
        nameField = createFiled("", "");
        formPanel.add(nameField, c);

        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 0;
        c.insets = new Insets(5, 0, 0, 0);
        formPanel.add(new JBLabel("URL: "), c);
        c.gridx = 1;
        c.weightx = 1;
        urlField = createFiled("", "Example: http://localhost:9000");
        formPanel.add(urlField, c);

        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0;
        formPanel.add(new JBLabel("Token: "), c);
        c.gridx = 1;
        c.weightx = 1;
        tokenField = createFiled("", "");
        formPanel.add(tokenField, c);

        content.add(formPanel, BorderLayout.CENTER);

        return content;
    }

    private JBTextField createFiled(String defaultText, String emptyText) {
        JBTextField textField = new JBTextField(defaultText);
        textField.getEmptyText().setText(emptyText);
        BooleanFunction<JBTextField> statusVisibleFunction = jbTextField -> StringUtil.isEmptyOrSpaces(textField.getText());
        textField.putClientProperty("StatusVisibleFunction", statusVisibleFunction);
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

        String url = urlField.getText().trim();
        if (StringUtil.isEmptyOrSpaces(url)) {
            return;
        }

        String token = tokenField.getText().trim();
        if (StringUtil.isEmptyOrSpaces(token)) {
            return;
        }

        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
            Messages.showDialog("Please provide a valid URL", "Error", new String[] { "Ok" }, 0, Messages.getErrorIcon());
            return;
        }

        if (!edit && existNames.contains(name)) {
            Messages.showDialog("There is already a connection with that name. Please choose another name", "Error", new String[] { "Ok" }, 0, Messages.getErrorIcon());
            return;
        }

        Task task = new Task.Modal(null, "Test connection to SonarQube", false) {
            @Override
            public void run(@NotNull final ProgressIndicator indicator) {
                try {
                    indicator.setText(String.format("Connecting to server %s", url));
                    SonarApiImpl.checkConnection(url, token);
                    SonarQubeSettings sonarQubeSettings = new SonarQubeSettings();
                    {
                        sonarQubeSettings.name = name;
                        sonarQubeSettings.url = url;
                        sonarQubeSettings.token = token;
                    }
                    EventDispatchThreadHelper.invokeLater(() -> {
                        saveConsumer.consume(sonarQubeSettings);
                        close(0);
                    });
                } catch (AuthorizationException e) {
                    EventDispatchThreadHelper.invokeLater(() -> {
                        Messages.showDialog("Failed to connect to the server. Please check the Token.", "Error", new String[] { "Ok" }, 0, Messages.getErrorIcon());
                    });
                    return;
                } catch (ApiRequestFailedException e) {
                    EventDispatchThreadHelper.invokeLater(() -> {
                        Messages.showDialog("Failed to connect to the server. Please check the URL.", "Error", new String[] { "Ok" }, 0, Messages.getErrorIcon());
                    });
                    return;
                }
            }
        };
        task.queue();
    }

}
