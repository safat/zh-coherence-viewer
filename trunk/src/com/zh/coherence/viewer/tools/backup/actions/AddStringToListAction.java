package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddStringToListAction extends AbstractAction{
    private BackupContext context;

    public AddStringToListAction(BackupContext context) {
        this.context = context;

        putValue(Action.NAME, "Add");
        putValue(Action.SMALL_ICON, new IconLoader("icons/plus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component parent = (Component) e.getSource();
        String name = JOptionPane.showInputDialog(parent, "input cache's name:",
                "Cache's name", JOptionPane.QUESTION_MESSAGE);
        if(name != null && !name.isEmpty()){
//            context.getBackupTableModel().addValue(name);
        }
    }
}
