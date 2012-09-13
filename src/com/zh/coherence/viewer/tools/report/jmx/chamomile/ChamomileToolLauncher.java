package com.zh.coherence.viewer.tools.report.jmx.chamomile;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolLauncher;

public class ChamomileToolLauncher extends ToolLauncher {
    @Override
    public CoherenceViewerTool newTool() {
        ChamomileTool tool = new ChamomileTool();
        return tool;
    }

    @Override
    public boolean isAvailable() {
        return JMXManager.getInstance().isEnabled();
    }
}
