package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveCacheList extends AbstractAction {

    public SaveCacheList() {
        putValue(Action.SMALL_ICON, new IconLoader("icons/floppy.png"));
        putValue(Action.SHORT_DESCRIPTION, "Save");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
