package org.zh.coherence.viewer.gui.menu;

import org.zh.coherence.viewer.gui.menu.file.FileMenu;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 24.12.12
 * Time: 23:17
 */
public class ApplicationMenu extends JMenuBar{

    public ApplicationMenu() {
        add(new FileMenu());
    }
}
