package com.zh.coherence.viewer.menubar;

import com.zh.coherence.viewer.ApplicationMainPane;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 06.03.12
 * Time: 0:41
 */
public class FileJMenuBuilder {
    public JMenu buildMenu(ApplicationMainPane mainPane){
        JMenu menu = new JMenu("File");
        menu.add(new JMenuItem(new FileExitAction()));

        return menu;
    }
}
