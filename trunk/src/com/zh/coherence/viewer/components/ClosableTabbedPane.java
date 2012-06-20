package com.zh.coherence.viewer.components;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 16.02.12
 * Time: 0:24
 */
public class ClosableTabbedPane extends JTabbedPane {

    public ClosableTabbedPane() {
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for(int i = 0; i < getTabCount(); i++){
                    setTabComponentAt(i, new TabCloseIcon(ClosableTabbedPane.this, false));
                }
                int selected = getSelectedIndex();
                if (selected >= 0) {
                    setTabComponentAt(selected, new TabCloseIcon(ClosableTabbedPane.this, true));
                }
            }
        });
    }
}
