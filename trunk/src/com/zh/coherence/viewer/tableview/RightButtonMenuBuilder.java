package com.zh.coherence.viewer.tableview;

import com.zh.coherence.viewer.ResourceManager;
import com.zh.coherence.viewer.tableview.user.ObjectViewersContainer;
import com.zh.coherence.viewer.tableview.user.UserViewerAction;
import com.zh.coherence.viewer.tableview.user.UserViewerItem;

import javax.swing.*;
import java.util.Collection;

public class RightButtonMenuBuilder {
    public JPopupMenu buildMenu(Object value){
        JPopupMenu menu = new JPopupMenu();

        ResourceManager resourceManager = ResourceManager.getInstance();
        ObjectViewersContainer viewersContainer = resourceManager.getViewersContainer();
        Collection<UserViewerItem> coll = viewersContainer.getAvailableViewers(value);
        for (UserViewerItem item : coll){
            menu.add(new UserViewerAction(item, value));
        }
        return menu;
    }
}
