package com.zh.coherence.viewer.menubar;

import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 06.03.12
 * Time: 0:42
 */
public class FileExitAction extends AbstractAction{
    public FileExitAction() {
        putValue(Action.NAME, "Exit");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.EXIT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
