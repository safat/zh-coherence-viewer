package com.zh.coherence.viewer.tableview.actions;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaHighlighter;
import org.fife.ui.rtextarea.ChangeableHighlightPainter;
import org.fife.ui.rtextarea.RTextArea;
import org.jdesktop.swingx.JXSearchField;

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
    private String value;

    public ShowObjectInText(Object value) {
        this.value = String.valueOf(value);

        putValue(Action.NAME, "Show text");
        putValue(Action.SMALL_ICON, new ImageIcon("icons/text-icon.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JDialog dialog = new JDialog();
        final RTextArea editorPane = new RTextArea();
        final RSyntaxTextAreaHighlighter highlighter = new RSyntaxTextAreaHighlighter();
        editorPane.setHighlighter(highlighter);
        editorPane.setEditable(false);
        editorPane.setDoubleBuffered(true);
        editorPane.setText(value);

        Container container = dialog.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(new JScrollPane(editorPane), BorderLayout.CENTER);
        JToolBar north = new JToolBar();
        final JCheckBox wrap = new JCheckBox("wrap text", false);
        wrap.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                editorPane.setLineWrap(wrap.isSelected());
            }
        });
        north.add(wrap);
        north.addSeparator();
        final JXSearchField searchField = new JXSearchField("search");
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    highlighter.removeAllHighlights();
                    String str = searchField.getText();
                    if (str != null && !str.isEmpty()) {
                        int idx = 0;
                        do {
                            idx = value.indexOf(str, idx);
                            if (idx != -1) {
                                highlighter.addHighlight(idx, idx + str.length(),
                                        new ChangeableHighlightPainter(Color.ORANGE, false));
                                idx = idx + str.length();
                            }
                        } while (idx != -1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        north.add(searchField);

        container.add(north, BorderLayout.NORTH);

        JPanel buts = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton close = new JButton("Close");
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
