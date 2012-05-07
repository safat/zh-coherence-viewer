package com.zh.coherence.viewer.tools;

public abstract class ToolFounder {
    private ToolDescription description;

    public abstract CoherenceViewerTool newTool();

    public abstract boolean isAvailable();

    public ToolDescription getDescription() {
        return description;
    }

    public void setDescription(ToolDescription description) {
        this.description = description;
    }
}
