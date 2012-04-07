package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupTableModel;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 07.04.12
 * Time: 0:12
 */
public class CheckAllCachesAction extends AbstractAction{
    private BackupTableModel model;

    public CheckAllCachesAction(BackupTableModel model) {
        this.model = model;

        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.CHECK_ALL));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.changeCheck(true);
    }
}
