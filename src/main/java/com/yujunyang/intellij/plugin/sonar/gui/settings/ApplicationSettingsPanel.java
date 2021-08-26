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
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DefaultSingleSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.yujunyang.intellij.plugin.sonar.gui.common.UIUtils;
import javafx.scene.control.TableSelectionModel;
import org.jetbrains.annotations.NotNull;

public class ApplicationSettingsPanel extends JBPanel {
    private JBTable connectionsTable;
    private DefaultTableModel connectionsTableModel;
    private int connectionsTableSelectedIndex = -1;
    private JBTable propertiesTable;
    private DefaultTableModel propertiesTableModel;
    private int propertiesTableSelectedIndex = -1;

    public ApplicationSettingsPanel() {
        init();
    }

    private void init() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        initConnections();
        add(Box.createVerticalStrut(15));
        initSonarProperties();
    }

    private void initConnections() {
        addTableLabel("SonarQube connections:");

        connectionsTableModel = new DefaultTableModel(0, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        connectionsTableModel.setColumnIdentifiers(new String[] { "Name", "Url" });
        connectionsTableModel.addRow(new String[] { "1", "1" });
        connectionsTableModel.addRow(new String[] { "2", "2" });


        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction("Add", "", AllIcons.General.Add) {


            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

            }
        });
        actionGroup.add(new AnAction("Remove", "", AllIcons.General.Remove) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(connectionsTableSelectedIndex > -1);
            }

            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

            }
        });
        actionGroup.add(new AnAction("Edit", "", AllIcons.Actions.Edit) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(connectionsTableSelectedIndex > -1);
            }

            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

            }
        });

        connectionsTable = createTable("No connections", connectionsTableModel, actionGroup);
        connectionsTable.getSelectionModel().addListSelectionListener(e -> {
            DefaultListSelectionModel target = (DefaultListSelectionModel)e.getSource();
            connectionsTableSelectedIndex = target.getAnchorSelectionIndex();
        });
    }

    private void initSonarProperties() {
        addTableLabel("SonarScanner properties:");

        String[] columnNames = { "Name", "Value" };
        String[][] data = {
            {
                "asdfsdf", "123123"
            },
                {
                        "asdfsdf", "123123"
                }
        };
        TableModel tableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return data.length;
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return "";
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction("Add", "", AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

            }
        });
        actionGroup.add(new AnAction("Remove", "", AllIcons.General.Remove) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(propertiesTableSelectedIndex > -1);
            }
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

            }
        });
        actionGroup.add(new AnAction("Edit", "", AllIcons.Actions.Edit) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(propertiesTableSelectedIndex > -1);
            }
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

            }
        });

        propertiesTable = createTable("No properties", tableModel, actionGroup);
        propertiesTable.getSelectionModel().addListSelectionListener(e -> {
            DefaultListSelectionModel target = (DefaultListSelectionModel)e.getSource();
            propertiesTableSelectedIndex = target.getAnchorSelectionIndex();
        });
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
        tablePanel.add(actionToolbar.getComponent(), BorderLayout.EAST);

        return table;
    }

    private void addTableLabel(String text) {
        JLabel connectionsLabel = new JLabel(text);
        connectionsLabel.setAlignmentX(LEFT_ALIGNMENT);
        connectionsLabel.setBorder(JBUI.Borders.empty(0, 0, 5, 0));
        add(connectionsLabel);
    }
}
