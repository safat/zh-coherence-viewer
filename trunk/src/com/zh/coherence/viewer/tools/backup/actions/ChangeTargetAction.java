package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.03.12
 * Time: 21:07
 */
public class ChangeTargetAction extends AbstractAction{
    private BackupContext context;
    private BackupContext.Target target;

    public ChangeTargetAction(BackupContext context, BackupContext.Target target, String name) {
        this.context = context;
        this.target = target;

        putValue(Action.NAME, name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(((JRadioButton)e.getSource()).isSelected()){
            context.setTarget(target);
        }
    }
}
