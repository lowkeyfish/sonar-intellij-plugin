package com.yujunyang.intellij.plugin.sonar.gui.toolwindow;

import java.awt.Color;
import javax.swing.Box;
import javax.swing.BoxLayout;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;

public class MessagePanel extends JBPanel {
    private String message;

    public MessagePanel(String message) {
        this.message = message;
        init();
    }

    private void init() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        JBLabel emptyLabel = new JBLabel(message);
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(emptyLabel);
        add(Box.createVerticalGlue());
    }
}
