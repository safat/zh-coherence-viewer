package com.zh.coherence.viewer.menubar;

import javax.swing.*;
import java.util.List;

public class SubMenu {
    private String name;
    private Icon icon;
    private List<Object> child;

    public List<Object> getChild() {
        return child;
    }

    public void setChild(List<Object> child) {
        this.child = child;
    }

    public Icon getIcon() {
        return icon;
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
}
