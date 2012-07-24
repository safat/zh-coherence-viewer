package com.zh.coherence.viewer.tools.statistic.config.actions;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;

public class MoveTableItemUpAction extends AbstractAction {

    private TableModel model;

    public MoveTableItemUpAction(TableModel model) {
        this.model = model;

        putValue(Action.NAME, "move up");
        putValue(Action.SMALL_ICON, new IconLoader("icons/up.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
