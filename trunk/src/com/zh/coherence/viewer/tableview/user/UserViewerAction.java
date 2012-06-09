package com.zh.coherence.viewer.tableview.user;

import com.zh.coherence.viewer.utils.ui.ZHDialog;
import com.zh.coherence.viewer.utils.ui.ZHDialogFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UserViewerAction extends AbstractAction{
    private UserObjectViewer viewer;
    private Object value;

    public UserViewerAction(UserObjectViewer viewer, Object value) {
        this.viewer = viewer;
        this.value = value;

        putValue(Action.NAME, viewer.getName());
        if(viewer.getIcon() != null){
            putValue(Action.SMALL_ICON, viewer.getIcon());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (viewer.getTarget()){
            case DIALOG:
                try{
                    ZHDialog dialog = new ZHDialog(viewer.buildPane(value), viewer.getName());
                    dialog.show(800,600);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            case FRAME:
                try{
                    ZHDialogFrame dialog = new ZHDialogFrame(viewer.buildPane(value), viewer.getName());
                    dialog.show(800,600);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            case TAB:

                break;
        }
    }
}
