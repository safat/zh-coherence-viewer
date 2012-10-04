package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RemoveElementsFromListAction extends AbstractAction{
    private BackupContext context;
    private JXTable table;

    public RemoveElementsFromListAction(BackupContext context, JXTable table) {
        this.context = context;
        this.table = table;

        putValue(Action.NAME, "Remove");
        putValue(Action.SMALL_ICON, new IconLoader("icons/minus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        BackupTableModel model = context.getBackupTableModel();
//        for(int row : table.getSelectedRows()){
//            model.removeValue((String) model.getValueAt(row, 1));
//        }
    }
}
