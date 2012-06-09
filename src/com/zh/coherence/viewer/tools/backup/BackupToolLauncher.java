package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolLauncher;

public class BackupToolLauncher extends ToolLauncher {

    @Override
    public CoherenceViewerTool newTool() {
        return new BackupTool();
    }
}
