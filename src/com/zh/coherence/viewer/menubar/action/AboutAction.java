package com.zh.coherence.viewer.menubar.action;

import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import com.zh.coherence.viewer.utils.ui.ZHDialog;
import org.jdesktop.swingx.JXImageView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 26.05.12
 * Time: 17:28
 */
public class AboutAction extends AbstractAction{

    public AboutAction() {
        putValue(Action.NAME, "About");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.INFORMATION));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JXImageView view = new JXImageView();
        try {
            view.setImage(new File("config/splash.png"));
            view.setDragEnabled(false);
            view.setEditable(false);
            ZHDialog dialog = new ZHDialog(view, "About");
            dialog.setModal(true);
            dialog.show(410,300);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
