package com.zh.coherence.viewer.tableview.config;

import com.zh.coherence.viewer.config.AbstractConfigPanel;

import javax.swing.*;

public class TableViewConfig extends AbstractConfigPanel{
    @Override
    public void applyChanges() {
    }

    @Override
    public void cancelChanges() {
    }

    @Override
    public void showHelp() {
    }

    @Override
    public boolean isHelpAvailable() {
        return false;
    }

    @Override
    public JComponent getConfigPanel() {
        return null;
    }

    @Override
    public boolean leaveThePage() {
        return true;
    }
}
