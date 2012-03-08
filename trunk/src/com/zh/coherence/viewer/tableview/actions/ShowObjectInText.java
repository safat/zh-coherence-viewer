package com.zh.coherence.viewer.tableview.actions;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 08.03.12
 * Time: 13:40
 */
public class ShowObjectInText extends AbstractAction {
    private Object value;

    public ShowObjectInText(Object value) {
        this.value = value;

        putValue(Action.NAME, "show text");
        putValue(Action.SMALL_ICON, new ImageIcon("icons/text-icon.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JDialog dialog = new JDialog();
        final JTextArea text = new JTextArea(value.toString());
        text.setEditable(false);
        text.setFont(new Font("Dialog", Font.PLAIN, 12));
        Container container = dialog.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(new JScrollPane(text), BorderLayout.CENTER);
        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JCheckBox wrap = new JCheckBox("wrap text", false);
        wrap.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                text.setLineWrap(wrap.isSelected());
            }
        });
        north.add(wrap);
        container.add(north, BorderLayout.NORTH);

        JPanel buts = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton close = new JButton("close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buts.add(close);
        container.add(buts, BorderLayout.SOUTH);

        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
