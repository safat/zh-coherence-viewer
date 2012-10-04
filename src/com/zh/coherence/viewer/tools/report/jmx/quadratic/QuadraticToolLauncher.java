package com.zh.coherence.viewer.tools.report.jmx.quadratic;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolLauncher;

public class QuadraticToolLauncher extends ToolLauncher {
    @Override
    public CoherenceViewerTool newTool() {
        QuadraticTool tool = new QuadraticTool();
        return tool;
    }

    @Override
    public boolean isAvailable() {
        return JMXManager.getInstance().isEnabled();
    }
}
