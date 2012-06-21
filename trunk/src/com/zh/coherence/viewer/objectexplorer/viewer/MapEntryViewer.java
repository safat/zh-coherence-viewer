package com.zh.coherence.viewer.objectexplorer.viewer;

import java.util.Map;

public class MapEntryViewer implements Viewer {

    private Map.Entry entry = null;

    public MapEntryViewer(Map.Entry entry) {
        this.entry = entry;
    }

    @Override
    public String getText() {
        return String.valueOf(entry.getValue());
    }

    @Override
    public String getNodeName() {
        String name = getName();
        if (name.length() > 15) {
            name = name.substring(0, 12) + "...";
        }
        return name;
    }

    @Override
    public String getName() {
        return String.valueOf(entry.getKey());
    }

    @Override
    public Object getSubject() {
        return entry.getValue();
    }

    public Map.Entry getEntry() {
        return entry;
    }

    public void setEntry(Map.Entry entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return getNodeName();
    }
}
