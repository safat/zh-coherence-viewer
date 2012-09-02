package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.tools.backup.BackupMaker;
import com.zh.coherence.viewer.tools.backup.RestoreMaker;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StartAction extends AbstractAction{
    private BackupContext context;

    public StartAction(BackupContext context) {
        this.context = context;

        putValue(Action.NAME, "Start");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.START));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent parent = (JComponent) e.getSource();
        if(context.getPath() == null || context.getPath().isEmpty()){
            JOptionPane.showMessageDialog(parent, "Path to folder cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(context.getAction() == BackupContext.BackupAction.BACKUP){
            final BackupMaker maker = new BackupMaker(context);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    maker.make();
                    System.err.println("Time: " + (System.currentTimeMillis() - time));
                }
            });
            thread.start();
        } else {
            final RestoreMaker restoreMaker = new RestoreMaker(context, parent);
            restoreMaker.make();
        }
    }
}
