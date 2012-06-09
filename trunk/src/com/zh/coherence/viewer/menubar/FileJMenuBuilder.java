package com.zh.coherence.viewer.menubar;

import com.zh.coherence.viewer.menubar.action.ShowSettingsAction;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 06.03.12
 * Time: 0:41
 */
public class FileJMenuBuilder {
    public JMenu buildMenu() {
        JMenu menu = new JMenu("File");

        menu.add(new ShowSettingsAction());
        menu.addSeparator();
        menu.add(new JMenuItem(new FileExitAction()));

        return menu;
    }
}
