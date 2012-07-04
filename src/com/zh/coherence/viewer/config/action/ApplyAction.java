package com.zh.coherence.viewer.config.action;

import com.zh.coherence.viewer.config.ConfigContainer;
import com.zh.coherence.viewer.config.ConfigPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ApplyAction extends AbstractAction {

    private ConfigContainer configContainer;

    public ApplyAction(ConfigContainer configContainer) {
        this.configContainer = configContainer;

        putValue(Action.NAME, "Apply");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ConfigPanel panel = configContainer.getCurrentPanel();
        if (panel != null) {
            panel.applyChanges();
        }
    }
}
