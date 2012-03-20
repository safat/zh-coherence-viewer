package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.CoherenceViewerToolCreator;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.03.12
 * Time: 20:52
 */
public class BackupToolCreator implements CoherenceViewerToolCreator {
    private String name;

    @Override
    public void setToolName(String name) {
        this.name = name;
    }

    @Override
    public String getToolName() {
        return name;
    }

    @Override
    public CoherenceViewerTool createToolContainer() {
        return new BackupTool();
    }
}
