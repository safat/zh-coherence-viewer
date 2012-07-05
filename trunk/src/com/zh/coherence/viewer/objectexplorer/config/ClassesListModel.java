package com.zh.coherence.viewer.objectexplorer.config;

import javax.swing.*;
import java.util.List;

public class ClassesListModel extends AbstractListModel {
    private List<String> classes = null;

    @Override
    public int getSize() {
        if (classes == null) {
            return 0;
        }
        return classes.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (classes != null) {
            return classes.get(index);
        } else {
            return null;
        }
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
        fireChanges();
    }

    public void fireChanges() {
        if (classes != null) {
            this.fireContentsChanged(this, 0, classes.size() - 1);
        }
    }
}
