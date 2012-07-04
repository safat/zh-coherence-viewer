package com.zh.coherence.viewer.menubar;

import com.zh.coherence.viewer.ResourceManager;
import com.zh.coherence.viewer.menubar.action.ShowSettingsAction;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 06.03.12
 * Time: 0:41
 */
public class FileJMenuBuilder {
    private ResourceManager manager;

    public JMenu buildMenu(ResourceManager manager) {
        this.manager = manager;

        JMenu menu = new JMenu("File");

        menu.add(new ShowSettingsAction(manager.getConfigContainer()));
        menu.addSeparator();
        menu.add(new JMenuItem(new FileExitAction()));

        return menu;
    }
}
