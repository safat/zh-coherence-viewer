package com.zh.coherence.viewer;

import com.zh.coherence.viewer.components.ClosableTabbedPane;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 0:36
 */
public class ApplicationMainPane extends JPanel implements ApplicationPane{
    private ClosableTabbedPane tabbedPane;

    private int count = 0;

    public ApplicationMainPane() {
        super(new BorderLayout());

        tabbedPane = new ClosableTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void addTool(CoherenceViewerTool tool, String name) {
        tabbedPane.addTab(name + " #" + count ,tool.getPane());
        tabbedPane.setIconAt(tabbedPane.getTabCount()-1, null);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
        count ++;
    }
}
