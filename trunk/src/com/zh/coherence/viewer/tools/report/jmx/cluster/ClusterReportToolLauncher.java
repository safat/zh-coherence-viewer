package com.zh.coherence.viewer.tools.report.jmx.cluster;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolLauncher;

public class ClusterReportToolLauncher extends ToolLauncher {
    @Override
    public CoherenceViewerTool newTool() {
        return new ClusterReportTool();
    }

    @Override
    public boolean isAvailable() {
        return JMXManager.getInstance().isEnabled();
    }
}
