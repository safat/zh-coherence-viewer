package com.zh.coherence.viewer.config;

import javax.swing.*;

public abstract class AbstractConfigPanel implements ConfigPanel{

    protected String configName;

    private Icon icon;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
