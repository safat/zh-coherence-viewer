package com.zh.coherence.viewer.tableview.actions;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 14.03.12
 * Time: 22:06
 */
public class TableHighlighterAction extends AbstractAction {
    private ImageIcon yellow = new ImageIcon("icons/yellow-hint-icon.png");
    private ImageIcon white = new ImageIcon("icons/white-hint-icon.png");
    private Highlighter highlighter = HighlighterFactory.createAlternateStriping();

    private JToggleButton button;
    private JXTable table;

    public TableHighlighterAction(JToggleButton button, JXTable table) {
        this.button = button;
        this.table = table;

        putValue(Action.NAME, "Enable Highlighting");
        updateIcon(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (button.getModel().isSelected()) {
            table.setHighlighters(highlighter);
        } else {
            table.removeHighlighter(highlighter);
        }

        updateIcon(button);
    }

    private void updateIcon(JToggleButton button) {
        putValue(Action.SMALL_ICON, button.getModel().isSelected() ? yellow : white);
    }
}
