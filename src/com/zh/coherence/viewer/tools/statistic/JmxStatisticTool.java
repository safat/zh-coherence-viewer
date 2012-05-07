package com.zh.coherence.viewer.tools.statistic;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.statistic.action.UpdateDataAction;
import com.zh.coherence.viewer.tools.statistic.tabs.CachesInfoPane;
import com.zh.coherence.viewer.tools.statistic.tabs.ClusterInfoPane;
import com.zh.coherence.viewer.tools.statistic.tabs.MemoryInfoPane;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;

public class JmxStatisticTool extends JXPanel implements CoherenceViewerTool {

    private JTabbedPane tabbedPane;

    public JmxStatisticTool() {
        super(new BorderLayout());

        JXHeader header = new JXHeader("JMX Statistic",
                "Cluster, Memory and Nodes information",
                IconHelper.getInstance().getIcon(IconType.FILM));

        add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.add("Overview", new ClusterInfoPane());
        tabbedPane.add("Memory", new MemoryInfoPane());
        tabbedPane.add("Caches", new CachesInfoPane());

        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.add(new UpdateDataAction());
        add(toolBar, BorderLayout.WEST);
    }

    @Override
    public JComponent getPane() {
        return this;
    }

}
