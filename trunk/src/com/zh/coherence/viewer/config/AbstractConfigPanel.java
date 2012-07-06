package com.zh.coherence.viewer.config;

import javax.swing.*;
import java.util.List;

public abstract class AbstractConfigPanel implements ConfigPanel{

    protected String configName;

    private Icon icon;

    private List<ConfigPanel> children = null;

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

    public List<ConfigPanel> getChildren() {
        return children;
    }

    public void setChildren(List<ConfigPanel> children) {
        this.children = children;
    }

    @Override
    public int getChildCount() {
        return children == null? 0 : children.size();
    }

    @Override
    public ConfigPanel getChild(int index) {
        return children.get(index);
    }
}
