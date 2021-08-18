package com.yujunyang.intellij.plugin.sonar.gui.common;

import java.awt.Dimension;
import java.awt.Insets;

import com.intellij.ui.components.JBTextArea;

public class YTextArea extends JBTextArea {

    public YTextArea(String text) {
        super(text);
    }

//    @Override
//    public Dimension getPreferredSize() {
//        Dimension d = super.getPreferredSize();
//        Insets insets = getInsets();
//        String[] lines = getText().split("\n");
//        int columns = 0;
//        int rows = lines.length;
//        for (String line : lines) {
//            columns = Math.max(columns, line.length());
//        }
//        if (columns != 0) {
//            d.width = Math.max(d.width, columns * getColumnWidth() +
//                    insets.left + insets.right);
//        }
//        if (rows != 0) {
//            d.height = Math.max(d.height, rows * getRowHeight() +
//                    insets.top + insets.bottom);
//        }
//
//        if (d.width > 400) {
//            if (d.width % 400 == 0) {
//                d.height *= d.width / 400;
//            } else {
//                d.height *= d.width / 400 + 1;
//            }
//            d.width = 400;
//        }
//
//
//
//        return d;
//    }


    @Override
    public Dimension getMaximumSize() {
        return new Dimension(400, 1000);
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getMinimumSize();
    }
}
