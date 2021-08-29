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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.options.ex.Settings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.config.ProjectSettings;
import com.yujunyang.intellij.plugin.sonar.config.SonarQubeSettings;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import com.yujunyang.intellij.plugin.sonar.extensions.ApplicationSettingsConfigurable;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import com.yujunyang.intellij.plugin.sonar.gui.dialog.AddSonarPropertyDialog;
import org.jetbrains.annotations.NotNull;

public class ProjectSettingsPanel extends JBPanel {
    private Project project;
    private ComboBox connectionNameComboBox;
    private JBCheckBox inheritedFromApplicationCheckBox;
    private JBTable propertiesTable;
    private DefaultTableModel propertiesTableModel;
    private Map<String, String> properties = new HashMap<>();

    public ProjectSettingsPanel(Project project) {
        this.project = project;
        init();
    }

    public String getConnectionName() {
        if (connectionNameComboBox.getSelectedItem() == null) {
            return null;
        }
        return connectionNameComboBox.getSelectedItem().toString();
    }

    public boolean isInheritedFromApplication() {
        return inheritedFromApplicationCheckBox.isSelected();
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    private void init() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        initConnectionName();
        add(Box.createVerticalStrut(15));
        initSonarProperties();
        inheritedFromApplicationCheckBox = new JBCheckBox("Inherited SonarScanner properties from global settings");
        inheritedFromApplicationCheckBox.setAlignmentX(LEFT_ALIGNMENT);
        add(inheritedFromApplicationCheckBox);

        // 这个不用主动调用，settings窗口打开时就会调用重写的reset方法，内部就是下面的reset
        // reset();
    }

    private void initConnectionName() {
        JBPanel panel = new JBPanel(new BorderLayout());
        panel.setAlignmentX(LEFT_ALIGNMENT);
        add(panel);
        panel.add(new JBLabel("SonarQube connection: "), BorderLayout.WEST);

        connectionNameComboBox = new ComboBox(WorkspaceSettings.getInstance().sonarQubeConnections.stream().map(n -> n.name).toArray());
        connectionNameComboBox.setEditable(false);
        panel.add(connectionNameComboBox, BorderLayout.CENTER);
//        connectionNameComboBox.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                connectionNameComboBox.removeAllItems();
//                Set<SonarQubeSettings> connections = WorkspaceSettings.getInstance().sonarQubeConnections;
//                connections.forEach(n -> {
//                    connectionNameComboBox.addItem(n.name);
//                });
//                if (connections.size() > 0) {
//                    String selectedItem = ProjectSettings.getInstance(project).sonarQubeConnectionName;
//                    if (StringUtil.isEmpty(selectedItem)) {
//                        connectionNameComboBox.setSelectedIndex(0);
//                    } else {
//                        connectionNameComboBox.setSelectedItem(selectedItem);
//                    }
//                }
//                connectionNameComboBox.revalidate();
//                connectionNameComboBox.repaint();
//            }
//        });

        JButton button = new JButton("Configure the connection");
        JComponent that = this;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Settings allSettings = Settings.KEY.getData(DataManager.getInstance().getDataContext(that));
                if (allSettings != null) {
                    final ApplicationSettingsConfigurable globalConfigurable = allSettings.find(ApplicationSettingsConfigurable.class);
                    if (globalConfigurable != null) {
                        allSettings.select(globalConfigurable);
                    }
                } else {
                    ApplicationSettingsConfigurable globalConfigurable = new ApplicationSettingsConfigurable();
                    ShowSettingsUtil.getInstance().editConfigurable(that, globalConfigurable);
                }
            }
        });
        panel.add(button, BorderLayout.EAST);

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
    }


    private void initSonarProperties() {
        addTableLabel("SonarScanner properties:");

        propertiesTableModel = createDefaultTableModel(new String[] { "Name", "Value" });

        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction("Add", "", AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                AddSonarPropertyDialog addSonarPropertyDialog = new AddSonarPropertyDialog((property) -> addSonarScannerProperty(property));
                addSonarPropertyDialog.setExistNames(properties.keySet().stream().collect(Collectors.toList()));
                addSonarPropertyDialog.show();
            }
        });
        actionGroup.add(new AnAction("Remove", "", AllIcons.General.Remove) {
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
        actionGroup.add(new AnAction("Edit", "", AllIcons.Actions.Edit) {
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

        propertiesTable = createTable("No properties", propertiesTableModel, actionGroup);
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

    private void addSonarScannerProperty(Pair<String, String> property) {
        properties.put(property.first, property.second);
        propertiesTableModel.addRow(new Object[] { property.first, property.second });
    }

    private void updateSonarScannerProperty(int selectionIndex, Pair<String, String> property) {
        properties.replace(property.first, property.second);

        propertiesTableModel.setValueAt(property.second, selectionIndex, 1);
    }

    public void reset() {
        ProjectSettings projectSettings = ProjectSettings.getInstance(project);
        connectionNameComboBox.setSelectedItem(projectSettings.sonarQubeConnectionName);

        properties.clear();
        int propertiesTableRowCount = propertiesTableModel.getRowCount();
        for (int i = 0; i < propertiesTableRowCount; i++) {
            propertiesTableModel.removeRow(i);
        }
        Map<String, String> existProperties = projectSettings.sonarProperties;
        for (Map.Entry<String, String> item : existProperties.entrySet()) {
            properties.put(item.getKey(), item.getValue());
            propertiesTableModel.addRow(new Object[] { item.getKey(), item.getValue() });
        }

        inheritedFromApplicationCheckBox.setSelected(projectSettings.inheritedFromApplication);
    }
}
