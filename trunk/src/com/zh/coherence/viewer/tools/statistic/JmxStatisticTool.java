package com.zh.coherence.viewer.tools.statistic;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.statistic.action.UpdateDataAction;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.tools.statistic.tabs.CachesInfoPane;
import com.zh.coherence.viewer.tools.statistic.tabs.ClusterInfoPane;
import com.zh.coherence.viewer.tools.statistic.tabs.NodeInfoPane;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;

public class JmxStatisticTool extends JXPanel implements CoherenceViewerTool {

    private JTabbedPane tabbedPane;
    private JMXReport jmxReport;

    public JmxStatisticTool() {
        super(new BorderLayout());

        JXHeader header = new JXHeader("JMX Statistic",
                "Cluster, Memory and Nodes information",
                new IconLoader("icons/film.png"));

        add(header, BorderLayout.NORTH);

        jmxReport = new JMXReport();
        jmxReport.refresh();

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.add("Overview", new ClusterInfoPane());
        tabbedPane.add("Memory", new NodeInfoPane(jmxReport));
        tabbedPane.add("Caches", new CachesInfoPane(jmxReport));

        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.add(new UpdateDataAction(jmxReport));
        add(toolBar, BorderLayout.WEST);

    }

    @Override
    public JComponent getPane() {
        return this;
    }

}
