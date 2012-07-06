package com.zh.coherence.viewer.objectexplorer.config;

import com.zh.coherence.viewer.utils.icons.IconLoader;
import org.jdesktop.swingx.JXList;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RemoveClassesAction extends AbstractAction {

    private ClassesListModel listModel;
    private JXList list;

    public RemoveClassesAction(ClassesListModel listModel, JXList list) {
        this.listModel = listModel;
        this.list = list;

        putValue(Action.NAME, "Remove");
        putValue(Action.SMALL_ICON, new IconLoader("icons/minus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object[] rows = list.getSelectedValues();
        for(Object obj : rows){
            listModel.getClasses().remove(obj);
        }
        listModel.fireChanges();
    }
}
