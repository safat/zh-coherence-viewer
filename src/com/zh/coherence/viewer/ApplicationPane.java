package com.zh.coherence.viewer;

import com.zh.coherence.viewer.components.ClosableTabbedPane;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;

import javax.swing.*;
import java.awt.*;

public class ApplicationPane extends JPanel{
    private ClosableTabbedPane tabbedPane;

    private int count = 0;

    public ApplicationPane() {
        super(new BorderLayout());

        tabbedPane = new ClosableTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void addTool(CoherenceViewerTool tool, String name) {
        tabbedPane.addTab(name + " #" + count ,tool.getPane());
        tabbedPane.setIconAt(tabbedPane.getTabCount()-1, null);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
        count ++;
    }
}
