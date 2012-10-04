package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.tools.backup.BackupMaker;
import com.zh.coherence.viewer.tools.backup.RestoreMaker;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;

public class StartAction extends AbstractAction {
    private BackupContext context;
    private AbstractTableModel tableModel;

    public StartAction(BackupContext context, AbstractTableModel tableModel) {
        this.context = context;
        this.tableModel = tableModel;

        putValue(Action.NAME, "Start");
        putValue(Action.SMALL_ICON, new IconLoader("icons/start-icon.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JComponent parent = (JComponent) e.getSource();
        if (context.getPath() == null || context.getPath().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Path to folder cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        final long time = System.currentTimeMillis();
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                if (context.getAction() == BackupContext.BackupAction.BACKUP) {
                    BackupMaker maker = new BackupMaker(context, tableModel);
                    maker.make();
                } else {
                    RestoreMaker restoreMaker = new RestoreMaker(context, parent);
                    restoreMaker.make();
                }

                return null;
            }

            @Override
            protected void done() {
                super.done();
                System.err.println("Time: " + (System.currentTimeMillis() - time));
            }
        }.execute();
    }
}
