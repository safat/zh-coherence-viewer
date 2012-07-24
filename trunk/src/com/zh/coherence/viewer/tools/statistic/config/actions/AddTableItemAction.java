package com.zh.coherence.viewer.tools.statistic.config.actions;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;

public class AddTableItemAction extends AbstractAction {

    private TableModel model;

    public AddTableItemAction(TableModel model) {
        this.model = model;

        putValue(Action.NAME, "ADD");
        putValue(Action.SMALL_ICON, new IconLoader("icons/plus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
