package com.zh.coherence.viewer.objectexplorer.config;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddClassAction extends AbstractAction {

    private ClassesListModel listModel;

    public AddClassAction(ClassesListModel listModel) {
        this.listModel = listModel;

        putValue(Action.NAME, "ADD");
        putValue(Action.SMALL_ICON, new IconLoader("icons/plus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();
        String name = JOptionPane.showInputDialog(component, "Input full class name, please");
        //check class name
        try {
            Class clazz = Class.forName(name, false, ClassLoader.getSystemClassLoader());
            listModel.getClasses().add(name);
            listModel.fireChanges();
        } catch (ClassNotFoundException e1) {
            JOptionPane.showMessageDialog(component, "class: " + name + " not found");
        }
    }
}
