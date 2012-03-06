package com.zh.coherence.viewer.config;

import org.jdesktop.swingx.JXList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 07.03.12
 * Time: 0:16
 */
public class PofConfigPane extends JPanel {
    private JXList list;
    private JToolBar toolBar;

    public PofConfigPane() {
        super(new BorderLayout());

        toolBar = new JToolBar();
        toolBar.add(new AddFileAction());
        toolBar.add(new RemoveFileAction());

        list = new JXList();

        add(toolBar, BorderLayout.NORTH);
        add(list, BorderLayout.CENTER);
    }

    private class AddFileAction extends AbstractAction {
        private AddFileAction() {
            putValue(Action.SMALL_ICON, new ImageIcon("icons/plus.png"));
            putValue(Action.NAME, "Add");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //todo implement
        }
    }

    private class RemoveFileAction extends AbstractAction {
        private RemoveFileAction() {
            putValue(Action.SMALL_ICON, new ImageIcon("icons/close-icon.png"));
            putValue(Action.NAME, "Remove");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //todo implement
        }
    }
}
