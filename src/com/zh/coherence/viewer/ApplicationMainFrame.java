package com.zh.coherence.viewer;

import com.zh.coherence.viewer.connection.ConnectionDialog;
import com.zh.coherence.viewer.menubar.FileJMenuBuilder;
import com.zh.coherence.viewer.tools.ToolsJMenuBuilder;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 0:11
 */
public class ApplicationMainFrame extends JFrame {
    private ResourceManager resourceManager;

    public ApplicationMainFrame() throws HeadlessException {
        super("ZH Coherence Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        resourceManager = ResourceManager.getInstance();
    }

    public void showFrame() {
        setSize(1024, 700);
        //todo save window size and position to the properties file
        //>>> load they each new application run
        ApplicationMainPane mainPane = new ApplicationMainPane();
        resourceManager.setApplicationPane(mainPane);
        setContentPane(mainPane);

        //prepare default menu bar
        resourceManager.addMenu(new FileJMenuBuilder().buildMenu(mainPane));
        resourceManager.addMenu(new ToolsJMenuBuilder().buildMenu(mainPane));

        setJMenuBar(resourceManager.getMenuBar());
        setLocationRelativeTo(null);
        setVisible(true);

        ConnectionDialog connectionDialog = new ConnectionDialog(this);
        connectionDialog.setLocationRelativeTo(this);
        connectionDialog.setVisible(true);
    }
}
