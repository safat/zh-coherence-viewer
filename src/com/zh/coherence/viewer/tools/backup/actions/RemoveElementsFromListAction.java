package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import org.jdesktop.swingx.JXList;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 21.03.12
 * Time: 0:20
 */
public class RemoveElementsFromListAction extends AbstractAction{
    private BackupContext context;
    private JXList list;

    public RemoveElementsFromListAction(BackupContext context, JXList list) {
        this.context = context;
        this.list = list;

        putValue(Action.NAME, "Remove");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.MINUS));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultListModel model = context.getCaches();
        for(Object selected : list.getSelectedValues()){
            model.removeElement(selected);
        }
    }
}
