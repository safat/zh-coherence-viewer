package com.zh.coherence.viewer.utils.ui;

import com.zh.coherence.viewer.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingUtilities.windowForComponent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 27.04.12
 * Time: 1:06
 */
public class ZHDialogFrame extends JFrame {
    private JComponent pane;
    private String title;

    public ZHDialogFrame(JComponent pane, String title) {
        this.pane = pane;
        this.title = title;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        JDialogHelper.escFrame(this);

        Container container = getContentPane();
        container.add(pane, BorderLayout.CENTER);
        JPanel buts = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buts.add(close);
        container.add(buts, BorderLayout.SOUTH);
    }

    public void show(int width, int height){
        setSize(width, height);
        setLocationRelativeTo(windowForComponent((Component) ResourceManager.getInstance().getApplicationPane()));
        setVisible(true);
    }
}
