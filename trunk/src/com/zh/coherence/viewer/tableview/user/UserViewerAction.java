package com.zh.coherence.viewer.tableview.user;

import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import com.zh.coherence.viewer.utils.ui.ZHDialog;
import com.zh.coherence.viewer.utils.ui.ZHDialogFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UserViewerAction extends AbstractAction{
    private UserViewerItem item;
    private Object value;

    public UserViewerAction(UserViewerItem item, Object value) {
        this.item = item;
        this.value = value;

        putValue(Action.NAME, item.getName());
        if(item.getIcon() != null){
            putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.valueOf(item.getIcon())));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (item.getTarget()){
            case DIALOG:
                try{
                    UserObjectViewer viewer = (UserObjectViewer) Class.forName(item.getRenderer()).newInstance();
                    ZHDialog dialog = new ZHDialog(viewer.getPane(value), item.getName());
                    dialog.show(800,600);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            case FRAME:
                try{
                    UserObjectViewer viewer = (UserObjectViewer) Class.forName(item.getRenderer()).newInstance();
                    ZHDialogFrame dialog = new ZHDialogFrame(viewer.getPane(value), item.getName());
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
