package com.zh.coherence.viewer.tableview.user;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;

public abstract class BaseUserObjectViewer implements UserObjectViewer{
    private Icon icon;

    private String name;

    private Target target;

    @Override
    public Icon getIcon() {
        return icon != null ? icon : new IconLoader("icons/eye.png");
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
