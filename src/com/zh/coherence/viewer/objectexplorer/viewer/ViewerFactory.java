package com.zh.coherence.viewer.objectexplorer.viewer;

public class ViewerFactory {
    public Viewer getViewer(String name, Object obj) {
        if (obj == null) {
            return new DefaultViewer(name, null);
        }if (obj.getClass().isArray()) {
            return new ArrayViewer(name, obj);
        } else {
            return new DefaultViewer(name, obj);
        }

//        return null;
    }
}
