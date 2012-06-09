package com.zh.coherence.viewer.tools.query;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolLauncher;

public class QueryToolLauncher extends ToolLauncher {

    @Override
    public CoherenceViewerTool newTool() {
        return new QueryTool();
    }
}
