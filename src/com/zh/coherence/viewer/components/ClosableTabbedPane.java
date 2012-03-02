package com.zh.coherence.viewer.components;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 16.02.12
 * Time: 0:24
 */
public class ClosableTabbedPane extends JTabbedPane {
    public ClosableTabbedPane() {
    }

    public void setIconAt(int index, Icon icon) {
        TabCloseIcon closeIcon = new TabCloseIcon();
        super.setIconAt(index, closeIcon);
    }

}
