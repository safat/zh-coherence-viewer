package com.zh.coherence.viewer.tools.statistic;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolFounder;

public class JmxStatisticToolFounder extends ToolFounder {
    @Override
    public CoherenceViewerTool newTool() {
        return new JmxStatisticTool();
    }

    @Override
    public boolean isAvailable() {
        return JMXManager.getInstance().isEnabled();
    }
}
