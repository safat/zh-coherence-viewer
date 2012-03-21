package com.zh.coherence.viewer.utils.panes;

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
 * Date: 15.03.12
 * Time: 23:36
 */
public class SearchTextPanel extends JPanel{
    private RTextArea editorPane = new RTextArea();
    private RSyntaxTextAreaHighlighter highlighter = new RSyntaxTextAreaHighlighter();
    private JToolBar north = new JToolBar();
    private JXSearchField searchField = new JXSearchField("search");

    private String value = null;

    public SearchTextPanel() {
        setLayout(new BorderLayout());

        add(new JScrollPane(editorPane), BorderLayout.CENTER);
        editorPane.setHighlighter(highlighter);
        editorPane.setEditable(false);
        editorPane.setDoubleBuffered(true);

        final JCheckBox wrap = new JCheckBox("wrap text", false);
        wrap.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                editorPane.setLineWrap(wrap.isSelected());
            }
        });
        north.add(wrap);
        north.addSeparator();

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

        add(north, BorderLayout.NORTH);

        wrap.setSelected(true);
    }

    public void setText(String text){
        value = text;
        editorPane.setText(text);
    }
}
