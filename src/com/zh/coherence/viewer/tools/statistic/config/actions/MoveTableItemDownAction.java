package com.zh.coherence.viewer.tools.statistic.config.actions;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;

public class MoveTableItemDownAction extends AbstractAction {

    private TableModel model;

    public MoveTableItemDownAction(TableModel model) {
        this.model = model;

        putValue(Action.NAME, "move down");
        putValue(Action.SMALL_ICON, new IconLoader("icons/down.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
