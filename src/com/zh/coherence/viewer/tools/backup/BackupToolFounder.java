package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolFounder;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.03.12
 * Time: 20:52
 */
public class BackupToolFounder extends ToolFounder {

    @Override
    public CoherenceViewerTool newTool() {
        return new BackupTool();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
