package org.zh.coherence.viewer.gui.menu.file;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 24.12.12
 * Time: 23:16
 */
public class FileMenu extends JMenu {
    public FileMenu() {
        super("File");
        setMnemonic('F');

        init();
    }

    private void init() {
        add(new CreateProject());
    }
}
