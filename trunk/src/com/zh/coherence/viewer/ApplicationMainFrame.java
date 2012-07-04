package com.zh.coherence.viewer;

import com.zh.coherence.viewer.connection.ConnectionDialog;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;

public class ApplicationMainFrame extends JFrame {
    private ResourceManager resourceManager;
    private ApplicationContext context;

    public ApplicationMainFrame() throws HeadlessException {
        super("ZH Coherence Viewer [0.3.7]");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        context = new ClassPathXmlApplicationContext(new String[]{"context.xml"});
        resourceManager = context.getBean("resourceManager", ResourceManager.class);
    }

    public void showFrame() {
        setSize(1024, 700);
        setContentPane(resourceManager.getApplicationPane());

        setJMenuBar(resourceManager.getMenuBar());
        setLocationRelativeTo(null);
        setVisible(true);

        ConnectionDialog connectionDialog = new ConnectionDialog(this);
        connectionDialog.setLocationRelativeTo(this);
        connectionDialog.setVisible(true);
    }
}
