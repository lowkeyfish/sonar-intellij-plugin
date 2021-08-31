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

package com.yujunyang.intellij.plugin.sonar.gui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.config.SonarQubeSettings;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.gui.dialog.AddSonarPropertyDialog;
import com.yujunyang.intellij.plugin.sonar.gui.dialog.AddSonarQubeConnectionDialog;
import com.yujunyang.intellij.plugin.sonar.resources.ResourcesLoader;
import org.jetbrains.annotations.NotNull;

public class ApplicationSettingsPanel extends JBPanel {
    private JBTable connectionsTable;
    private DefaultTableModel connectionsTableModel;
    private List<SonarQubeSettings> connections = new ArrayList<>();
    private JBTable propertiesTable;
    private DefaultTableModel propertiesTableModel;
    private Map<String, String> properties = new HashMap<>();
    private ComboBox uiLanguagesComboBox;

    public ApplicationSettingsPanel() {
        init();
    }

    public List<SonarQubeSettings> getConnections() {
        return connections;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getUILanguageLocale() {
        return UIUtils.getLocaleByLanguageDesc(uiLanguagesComboBox.getSelectedItem().toString());
    }

    private void init() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        initUILanguages();
        add(Box.createVerticalStrut(15));
        initConnections();
        add(Box.createVerticalStrut(15));
        initSonarProperties();

        // 这个不用主动调用，settings窗口打开时就会调用重写的reset方法，内部就是下面的reset
        // reset();
    }

    private void initUILanguages() {
        JBPanel panel = new JBPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(new JBLabel(ResourcesLoader.getString("settings.uiLanguages.label") + " "));

        uiLanguagesComboBox = new ComboBox();
        panel.add(uiLanguagesComboBox);
        uiLanguagesComboBox.setEditable(false);
        UIUtils.getAllUILanguagesDesc().forEach(n -> {
            uiLanguagesComboBox.addItem(n);
        });
        uiLanguagesComboBox.setSelectedItem(UIUtils.getLanguageDescByLocale(WorkspaceSettings.getInstance().uiLanguageLocale));

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

        add(panel);
    }

    private void initConnections() {
        addTableLabel(ResourcesLoader.getString("settings.sonarQubeConnections.tableTitle"));

        connectionsTableModel = createDefaultTableModel(new String[] { "Name", "Url" });


        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction(ResourcesLoader.getString("settings.action.add"), "", AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                AddSonarQubeConnectionDialog addSonarQubeConnectionDialog = new AddSonarQubeConnectionDialog((sonarQubeSettings) -> addSonarQubeConnection(sonarQubeSettings));
                addSonarQubeConnectionDialog.setExistNames(connections.stream().map(n -> n.name).collect(Collectors.toList()));
                addSonarQubeConnectionDialog.show();
            }
        });
        actionGroup.add(new AnAction(ResourcesLoader.getString("settings.action.remove"), "", AllIcons.General.Remove) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(connectionsTable.getSelectionModel().getAnchorSelectionIndex() > -1);
            }

            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                int selectionIndex = connectionsTable.getSelectionModel().getAnchorSelectionIndex();
                connectionsTableModel.removeRow(selectionIndex);
                connections.remove(selectionIndex);
                connectionsTableModel.fireTableDataChanged();
            }
        });
        actionGroup.add(new AnAction(ResourcesLoader.getString("settings.action.edit"), "", AllIcons.Actions.Edit) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(connectionsTable.getSelectionModel().getAnchorSelectionIndex() > -1);
            }

            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                int selectionIndex = connectionsTable.getSelectionModel().getAnchorSelectionIndex();
                SonarQubeSettings selectedSonarQubeSettings = connections.get(selectionIndex);
                AddSonarQubeConnectionDialog addSonarQubeConnectionDialog = new AddSonarQubeConnectionDialog((sonarQubeSettings) -> updateSonarQubeConnection(selectionIndex, sonarQubeSettings));
                addSonarQubeConnectionDialog.setExistNames(connections.stream().map(n -> n.name).collect(Collectors.toList()));
                addSonarQubeConnectionDialog.initConnection(selectedSonarQubeSettings.name, selectedSonarQubeSettings.url, selectedSonarQubeSettings.token);
                addSonarQubeConnectionDialog.show();
            }
        });

        connectionsTable = createTable(ResourcesLoader.getString("settings.sonarQubeConnections.tableEmpty"), connectionsTableModel, actionGroup);
    }



    private void initSonarProperties() {
        addTableLabel(ResourcesLoader.getString("settings.sonarScannerProperties.tableTitle"));

        propertiesTableModel = createDefaultTableModel(new String[] { "Name", "Value" });

        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction(ResourcesLoader.getString("settings.action.add"), "", AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                AddSonarPropertyDialog addSonarPropertyDialog = new AddSonarPropertyDialog((property) -> addSonarScannerProperty(property));
                addSonarPropertyDialog.setExistNames(properties.keySet().stream().collect(Collectors.toList()));
                addSonarPropertyDialog.show();
            }
        });
        actionGroup.add(new AnAction(ResourcesLoader.getString("settings.action.remove"), "", AllIcons.General.Remove) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(propertiesTable.getSelectionModel().getAnchorSelectionIndex() > -1);
            }
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                int selectionIndex = propertiesTable.getSelectionModel().getAnchorSelectionIndex();
                String name = propertiesTableModel.getValueAt(selectionIndex, 0).toString();
                propertiesTableModel.removeRow(selectionIndex);
                properties.remove(name);
                propertiesTableModel.fireTableDataChanged();
            }
        });
        actionGroup.add(new AnAction(ResourcesLoader.getString("settings.action.edit"), "", AllIcons.Actions.Edit) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(propertiesTable.getSelectionModel().getAnchorSelectionIndex() > -1);
            }
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                int selectionIndex = propertiesTable.getSelectionModel().getAnchorSelectionIndex();
                String name = propertiesTableModel.getValueAt(selectionIndex, 0).toString();
                String value = properties.get(name);
                AddSonarPropertyDialog addSonarPropertyDialog = new AddSonarPropertyDialog((property) -> updateSonarScannerProperty(selectionIndex, property));
                addSonarPropertyDialog.setExistNames(properties.keySet().stream().collect(Collectors.toList()));
                addSonarPropertyDialog.initProperty(name, value);
                addSonarPropertyDialog.show();
            }
        });

        propertiesTable = createTable(ResourcesLoader.getString("settings.sonarScannerProperties.tableEmpty"), propertiesTableModel, actionGroup);
    }

    private JBTable createTable(String emptyText, TableModel tableModel, ActionGroup actionGroup) {
        JBPanel tablePanel = new JBPanel(new BorderLayout());
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        tablePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
        tablePanel.setBorder(JBUI.Borders.customLine(UIUtils.borderColor(), 1));
        tablePanel.setAlignmentX(LEFT_ALIGNMENT);
        add(tablePanel);

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.setBorder(JBUI.Borders.customLine(UIUtils.borderColor(), 0, 0, 0, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JBTable table = new JBTable();
        table.getEmptyText().setText(emptyText);
        table.setModel(tableModel);
        scrollPane.setViewportView(table);

        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("left", actionGroup, false);
        JComponent actionToolbarComponent = actionToolbar.getComponent();
        actionToolbarComponent.setBorder(JBUI.Borders.empty());
        tablePanel.add(actionToolbarComponent, BorderLayout.EAST);

        return table;
    }

    private void addTableLabel(String text) {
        JLabel connectionsLabel = new JLabel(text);
        connectionsLabel.setAlignmentX(LEFT_ALIGNMENT);
        connectionsLabel.setBorder(JBUI.Borders.empty(0, 0, 5, 0));
        add(connectionsLabel);
    }

    private DefaultTableModel createDefaultTableModel(String[] columns) {
        DefaultTableModel tableModel = new DefaultTableModel(0, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setColumnIdentifiers(columns);
        return tableModel;
    }

    private void addSonarQubeConnection(SonarQubeSettings sonarQubeSettings) {
        connections.add(sonarQubeSettings);
        connectionsTableModel.addRow(new Object[] { sonarQubeSettings.name, sonarQubeSettings.url });
    }

    private void updateSonarQubeConnection(int selectionIndex, SonarQubeSettings sonarQubeSettings) {
        SonarQubeSettings original = connections.get(selectionIndex);
        original.name = sonarQubeSettings.name;
        original.url = sonarQubeSettings.url;
        original.token = sonarQubeSettings.token;

        connectionsTableModel.setValueAt(sonarQubeSettings.url, selectionIndex, 1);
    }

    private void addSonarScannerProperty(Pair<String, String> property) {
        properties.put(property.first, property.second);
        propertiesTableModel.addRow(new Object[] { property.first, property.second });
    }

    private void updateSonarScannerProperty(int selectionIndex, Pair<String, String> property) {
        properties.replace(property.first, property.second);

        propertiesTableModel.setValueAt(property.second, selectionIndex, 1);
    }

    public void reset() {
        uiLanguagesComboBox.setSelectedItem(UIUtils.getLanguageDescByLocale(WorkspaceSettings.getInstance().uiLanguageLocale));

        connections.clear();
        int connectionsTableRowCount = connectionsTableModel.getRowCount();
        for (int i = connectionsTableRowCount - 1; i >= 0; i--) {
            connectionsTableModel.removeRow(i);
        }
        WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        Set<SonarQubeSettings> existConnections = workspaceSettings.sonarQubeConnections;
        existConnections.forEach(c -> {
            SonarQubeSettings sqs = new SonarQubeSettings();
            {
                sqs.name = c.name;
                sqs.url = c.url;
                sqs.token = c.token;
            }
            connections.add(sqs);
            connectionsTableModel.addRow(new Object[] { c.name, c.url });
        });

        properties.clear();
        int propertiesTableRowCount = propertiesTableModel.getRowCount();
        for (int i = propertiesTableRowCount - 1; i >= 0; i--) {
            propertiesTableModel.removeRow(i);
        }
        Map<String, String> existProperties = WorkspaceSettings.getInstance().sonarProperties;
        for (Map.Entry<String, String> item : existProperties.entrySet()) {
            properties.put(item.getKey(), item.getValue());
            propertiesTableModel.addRow(new Object[] { item.getKey(), item.getValue() });
        }
    }
}
