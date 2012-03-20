package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.03.12
 * Time: 20:59
 */
public class BackupAction extends AbstractAction{
    private BackupContext context;
    private BackupContext.BackupAction action;

    public BackupAction(BackupContext context, BackupContext.BackupAction action, String name) {
        this.context = context;
        this.action = action;

        putValue(Action.NAME, name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JRadioButton radio = (JRadioButton) e.getSource();
        if(radio.isSelected()){
            context.setAction(action);
        }
    }
}
