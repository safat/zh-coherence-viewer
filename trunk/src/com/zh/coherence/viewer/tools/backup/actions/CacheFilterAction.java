package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupTableModel;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import com.zh.coherence.viewer.utils.ui.ZHDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CacheFilterAction extends AbstractAction {
    private BackupTableModel backupTableModel;
    private JTable table;

    public CacheFilterAction(BackupTableModel backupTableModel, JTable table) {
        putValue(Action.SMALL_ICON, new IconLoader("icons/filter.png"));
        putValue(Action.SHORT_DESCRIPTION, "Edit filter for selected cache");

        this.backupTableModel = backupTableModel;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent parent = (JComponent) e.getSource();
        JPanel content = new JPanel(new BorderLayout());
//north panel
        JPanel north = new JPanel();
        final JCheckBox enable = new JCheckBox("Enable filter");
        north.setBorder(BorderFactory.createEtchedBorder());
        north.add(enable);

        //assemble
        content.add(north, BorderLayout.NORTH);


        final ZHDialog dialog = new ZHDialog(content, "Filter editor", new Runnable() {
            @Override
            public void run() {
                int row = table.getSelectedRow();
                BackupTableModel.CacheInfo info = backupTableModel.getCacheInfoList().get(row);
                info.enableFilter = enable.isSelected();
                backupTableModel.sendEvent(row);
            }
        }, "OK");

        dialog.setModal(true);
        dialog.show(parent, 600, 450);
    }

}