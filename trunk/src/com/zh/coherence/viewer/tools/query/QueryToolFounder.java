package com.zh.coherence.viewer.tools.query;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolFounder;

public class QueryToolFounder extends ToolFounder {
    @Override
    public CoherenceViewerTool newTool() {
        return new QueryTool();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
