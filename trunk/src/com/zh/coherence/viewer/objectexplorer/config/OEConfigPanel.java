package com.zh.coherence.viewer.objectexplorer.config;

import com.zh.coherence.viewer.config.AbstractConfigPanel;
import com.zh.coherence.viewer.config.ConfigPanel;

import javax.swing.*;

public class OEConfigPanel extends AbstractConfigPanel {
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
        return new JLabel("TEST");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public ConfigPanel getChild(int index) {
        return null;
    }

    @Override
    public boolean leaveThePage() {
        return false;
    }
}
