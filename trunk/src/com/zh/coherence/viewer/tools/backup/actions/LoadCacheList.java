package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoadCacheList extends AbstractAction {
    public LoadCacheList() {
        putValue(Action.SMALL_ICON, new IconLoader("icons/folder-gray-open.png"));
        putValue(Action.SHORT_DESCRIPTION, "Load cache list");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
