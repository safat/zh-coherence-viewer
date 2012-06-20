package com.zh.coherence.viewer.components;

import com.zh.coherence.viewer.utils.icons.IconLoader;

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
public class TabCloseIcon extends JPanel {

    public TabCloseIcon(final JTabbedPane pane, boolean enableCloseButton) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        setOpaque(false);

        JLabel label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(TabCloseIcon.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        Icon close = new IconLoader("icons/close-icon.gif");
        JLabel closeLabel = new JLabel(close);
        if(enableCloseButton){
            add(closeLabel);
        }
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = pane.indexOfTabComponent(TabCloseIcon.this);
                if (i != -1) {
                    pane.remove(i);
                }
            }
        });
    }
}
