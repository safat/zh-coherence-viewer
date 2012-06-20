package com.zh.coherence.viewer.utils.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZHDialog extends JDialog {
    private JComponent pane;
    private String title;

    private Action userAction = null;

    public ZHDialog(JComponent pane, String title, Action userAction) {
        this.userAction = userAction;
        this.pane = pane;
        this.title = title;
        init();
    }

    public ZHDialog(JComponent pane, String title) {
//        super(windowForComponent((Component) ResourceManager.getInstance().getApplicationPane()),
//                ModalityType.APPLICATION_MODAL);

        this.pane = pane;
        this.title = title;

        init();
    }

    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        JDialogHelper.escDialog(this);

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
        if(userAction != null){
            buts.add(new JButton(userAction));
        }
        buts.add(close);
        container.add(buts, BorderLayout.SOUTH);
    }

    public void show(int width, int height) {
        setSize(width, height);
//        setPreferredSize(new Dimension(width, height));
//        setLocationRelativeTo(windowForComponent((Component) ResourceManager.getInstance().getApplicationPane()));
        setVisible(true);
    }
}
