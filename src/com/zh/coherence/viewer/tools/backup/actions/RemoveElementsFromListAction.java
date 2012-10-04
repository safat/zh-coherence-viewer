package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.tools.backup.BackupTool;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RemoveElementsFromListAction extends AbstractAction{
    private BackupContext context;
    private JXTable table;
    private BackupTool backupTool;

    public RemoveElementsFromListAction(BackupContext context, JXTable table, BackupTool backupTool) {
        this.context = context;
        this.table = table;
        this.backupTool = backupTool;

        putValue(Action.NAME, "Remove");
        putValue(Action.SMALL_ICON, new IconLoader("icons/minus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] row = table.getSelectedRows();
        for(int i = row.length - 1; i >=0 ; i--){
            context.getCacheInfoList().remove(row[i]);
        }
        backupTool.updateTable();
    }
}
