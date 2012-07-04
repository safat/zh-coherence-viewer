package com.zh.coherence.viewer.config;

import javax.swing.*;

public interface ConfigPanel {
    /**
     * @return name of config, will be shown in the tree
     */
    String getConfigName();

    /**
     * Apply changes made at the panel
     */
    void applyChanges();

    /**
     * Cancel changes made at the panel
     */
    void cancelChanges();

    /**
     * show help for panel
     */
    void showHelp();

    /**
     * @return TRUE if help is available
     */
    boolean isHelpAvailable();

    /**
     * @return Icon for the panel or NULL if it doesn't available
     */
    Icon getIcon();

    /**
     * @return Panel which will be shown at right side
     */
    JComponent getConfigPanel();

    /**
     * @return the number of children. Returns 0 if the node is a leaf or if it has no children
     */
    int getChildCount();

    /**
     * @param index index of child (from 0 to getChildrenCount() - 1)
     * @return child panel
     */
    ConfigPanel getChild(int index);

    /**
     * Application checks the Config Panel status when it loses focus
     * @return TRUE if config was changed.
     */
    boolean isConfigChanged();
}
