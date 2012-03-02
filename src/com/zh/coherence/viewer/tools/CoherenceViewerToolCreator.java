package com.zh.coherence.viewer.tools;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 1:12
 */
public interface CoherenceViewerToolCreator {
    void setToolName(String name);

    String getToolName();

    CoherenceViewerTool createToolContainer();
}
