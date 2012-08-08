package com.zh.coherence.viewer.tools.report.jmx.cluster;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import layout.TableLayout;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;

public class ClusterReportTool extends JXPanel implements CoherenceViewerTool {

    @Override
    public JComponent getPane() {
        this.setLayout(new BorderLayout());
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setDividerLocation(250);
        add(split, BorderLayout.CENTER);

        JPanel north = new JPanel();
        JPanel south = new JPanel();
        split.setLeftComponent(north);
        split.setRightComponent(south);

        TableLayout northLayout = new TableLayout(new double[][]{
                {2, TableLayout.FILL, 2},
                {2, TableLayout.PREFERRED, 2, TableLayout.PREFERRED, 2, TableLayout.FILL,2}
        });
        north.setLayout(northLayout);

        return this;
    }
}
