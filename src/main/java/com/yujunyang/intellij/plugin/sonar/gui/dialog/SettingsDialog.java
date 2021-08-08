package com.yujunyang.intellij.plugin.sonar.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.BooleanFunction;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.gui.error.ErrorPainter;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingsDialog extends DialogWrapper {
    private Project project;
    private ErrorPainter errorPainter;
    private JBTextField sonarHostUrlField;

    public SettingsDialog(@Nullable Project project) {
        super(true);
        this.project = project;

        init();
        setTitle("SonarAnalyzer Settings");
        setResizable(false);
        getContentPanel().setBorder(JBUI.Borders.empty());
        ((JComponent)getContentPanel().getComponent(1)).setBorder(JBUI.Borders.empty(0, 12, 8, 12));
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JBPanel content = new JBPanel();
        content.setPreferredSize(new Dimension(440, content.getPreferredSize().height));
        content.setBorder(JBUI.Borders.empty(20, 20));
        BoxLayout layout = new BoxLayout(content, BoxLayout.Y_AXIS);
        content.setLayout(layout);

        JBLabel sonarQubeLogoLabel = new JBLabel(PluginIcons.SONAR_QUBE);
        sonarQubeLogoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(UIUtils.wrappedInBorderLayoutPanel(sonarQubeLogoLabel, BorderLayout.CENTER));
        content.add(Box.createVerticalStrut(10));

        errorPainter = new ErrorPainter();
        errorPainter.installOn((JPanel) getContentPanel(), () -> {
        });

        JBLabel sonarHostUrlLabel = new JBLabel("SonarQube URL: ");
        WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        sonarHostUrlField = new JBTextField(workspaceSettings.getSonarHostUrl());
        sonarHostUrlField.getEmptyText().setText("Example: http://localhost:9000");
        BooleanFunction<JBTextField> statusVisibleFunction = jbTextField -> "".equals(sonarHostUrlField.getText());
        sonarHostUrlField.putClientProperty("StatusVisibleFunction", statusVisibleFunction);
        sonarHostUrlField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                errorPainter.setValid(sonarHostUrlField, !StringUtil.isEmptyOrSpaces(sonarHostUrlField.getText()));
            }
        });

        content.add(UIUtils.wrappedInBorderLayoutPanel(
                new Pair<>(sonarHostUrlLabel, BorderLayout.WEST),
                new Pair<>(sonarHostUrlField, BorderLayout.CENTER))
        );
        content.add(Box.createVerticalStrut(20));

        return content;
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
        String sonarHostUrl = sonarHostUrlField.getText();
        if (StringUtil.isEmptyOrSpaces(sonarHostUrl)) {
            return;
        }

        WorkspaceSettings.getInstance().sonarHostUrl = sonarHostUrl;
        close(0);
    }

}
