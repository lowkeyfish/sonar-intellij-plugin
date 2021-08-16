package com.yujunyang.intellij.plugin.sonar.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class SampleVerticalScrollLayout implements LayoutManager {
    private Insets insets;

    public SampleVerticalScrollLayout() {
        this(new Insets(0, 0, 0, 0));
    }

    public SampleVerticalScrollLayout(Insets insets) {
        this.insets = insets;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            int membersCount = parent.getComponentCount();
            for (int i = 0; i < membersCount; i++) {
                Component m = parent.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    dim.height += d.height;
                    dim.width = Integer.MAX_VALUE;
                }
            }
            dim.height += insets.top + insets.bottom;
            return dim;
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            int membersCount = parent.getComponentCount();

            for (int i = 0; i < membersCount; i++) {
                Component m = parent.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getMinimumSize();
                    dim.height += d.height;
                    dim.width = Integer.max(dim.width, d.width);
                }
            }
            dim.height += insets.top + insets.bottom;
            dim.width += insets.left + insets.right;
            return dim;
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int membersCount = parent.getComponentCount();
            int x = insets.left;
            int y = insets.top;
            int width = parent.getWidth() - insets.left - insets.right;

            for (int i = 0 ; i < membersCount ; i++) {
                Component m = parent.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    m.setSize(width, d.height);
                    m.setLocation(x, y);
                    y += d.height;
                }
            }
        }
    }
}
