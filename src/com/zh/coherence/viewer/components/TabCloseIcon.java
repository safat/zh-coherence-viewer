package com.zh.coherence.viewer.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 16.02.12
 * Time: 0:30
 */
public class TabCloseIcon implements Icon {
    private final Icon mIcon;
    private JTabbedPane mTabbedPane = null;
    private transient Rectangle mPosition = null;

    public TabCloseIcon(Icon mIcon) {
        this.mIcon = mIcon;
    }

    public TabCloseIcon() {
        this(new ImageIcon("icons/close-icon.gif"));
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (null == mTabbedPane) {
            mTabbedPane = (JTabbedPane) c;
            mTabbedPane.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!e.isConsumed() && mPosition.contains(e.getX(), e.getY())) {
                        final int index = mTabbedPane.getSelectedIndex();
                        mTabbedPane.remove(index);
                        e.consume();
                    }
                }
            });
        }

        mPosition = new Rectangle(x, y, getIconWidth(), getIconHeight());
        mIcon.paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return mIcon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return mIcon.getIconHeight();
    }
}
