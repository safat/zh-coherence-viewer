package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupTableModel;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UnCheckAllCachesAction extends AbstractAction {
    private BackupTableModel model;

    public UnCheckAllCachesAction(BackupTableModel model) {
        this.model = model;

        putValue(Action.SMALL_ICON, new IconLoader("icons/uncheck.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < model.getRowCount(); i++){
            model.setValueAt(Boolean.FALSE, i, 0);
        }
        model.fireTableDataChanged();
    }
}
