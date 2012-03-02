package com.zh.coherence.viewer.tools.query;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.CoherenceViewerToolCreator;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 1:19
 */
public class QueryToolCreator implements CoherenceViewerToolCreator{
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
        return new QueryTool();
    }
}
