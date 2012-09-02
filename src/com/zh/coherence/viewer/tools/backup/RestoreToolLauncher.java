package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolLauncher;

public class RestoreToolLauncher extends ToolLauncher {
    @Override
    public CoherenceViewerTool newTool() {
        return new BackupTool(BackupContext.BackupAction.RESTORE);
    }
}
