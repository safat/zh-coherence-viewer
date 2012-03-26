package com.zh.coherence.viewer.menubar.action;

import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 22.03.12
 * Time: 0:37
 */
public class ShowSettingsAction extends AbstractAction{
    public ShowSettingsAction() {

        putValue(Action.NAME, "Settings");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.SETTINGS));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
