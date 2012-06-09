package com.zh.coherence.viewer.tools.query.user;

import com.zh.coherence.viewer.objectexplorer.ObjectExplorer;
import com.zh.coherence.viewer.tableview.user.BaseUserObjectViewer;

import javax.swing.*;

public class ObjectExplorerViewer extends BaseUserObjectViewer {
    @Override
    public JComponent buildPane(Object value) {
        ObjectExplorer explorer = new ObjectExplorer();
        explorer.setData(value);

        return explorer;
    }

    @Override
    public boolean isSupport(Object value) {
        return value != null;
    }
}
