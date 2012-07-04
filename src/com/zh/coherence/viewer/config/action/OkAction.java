package com.zh.coherence.viewer.config.action;

import com.zh.coherence.viewer.config.ConfigContainer;
import com.zh.coherence.viewer.config.ConfigPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OkAction extends AbstractAction{

    private ConfigContainer configContainer;

    public OkAction(ConfigContainer configContainer) {

        this.configContainer = configContainer;

        putValue(Action.NAME, "OK");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ConfigPanel panel = configContainer.getCurrentPanel();
        if(panel != null){
            panel.applyChanges();
        }
        configContainer.dispose();
    }
}
