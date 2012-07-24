package com.zh.coherence.viewer.tools.statistic.config.actions;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;

public class RemoveTableItemAction extends AbstractAction {

    private TableModel model;

    public RemoveTableItemAction(TableModel model) {
        this.model = model;

        putValue(Action.NAME, "Remove");
        putValue(Action.SMALL_ICON, new IconLoader("icons/minus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
