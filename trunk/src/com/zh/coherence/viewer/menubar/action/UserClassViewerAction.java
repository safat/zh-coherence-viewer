package com.zh.coherence.viewer.menubar.action;

import com.zh.coherence.viewer.userclassmanager.UserClassViewer;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import com.zh.coherence.viewer.utils.ui.ZHDialogFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 26.05.12
 * Time: 18:26
 */
public class UserClassViewerAction extends AbstractAction{

    public UserClassViewerAction() {
        putValue(Action.NAME, "User classes viewer");
        putValue(Action.SMALL_ICON, new IconLoader("icons/document-j.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UserClassViewer viewer = new UserClassViewer();
        ZHDialogFrame dialog = new ZHDialogFrame(viewer, "User classes viewer");

        dialog.show(800,600);
    }
}
