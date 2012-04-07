package com.zh.coherence.viewer.objectexplorer.viewer;

public class ViewerFactory {
    public Viewer getViewer(String name, Object obj) {
        if (obj == null) {
            return new DefaultViewer(name, obj);
        }if (obj.getClass().isArray()) {
            return new ArrayViewer(name, obj);
        } else if (obj instanceof Object) {
            return new DefaultViewer(name, obj);
        }

        return null;
    }
}
