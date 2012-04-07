package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 21.03.12
 * Time: 0:11
 */
public class AddStringToListAction extends AbstractAction{
    private BackupContext context;

    public AddStringToListAction(BackupContext context) {
        this.context = context;

        putValue(Action.NAME, "Add");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.PLUS));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component parent = (Component) e.getSource();
        String name = JOptionPane.showInputDialog(parent, "input cache's name:",
                "Cache's name", JOptionPane.QUESTION_MESSAGE);
        if(name != null && !name.isEmpty()){
            context.getBackupTableModel().addValue(name);
        }
    }
}
