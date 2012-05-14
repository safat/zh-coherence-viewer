package com.zh.coherence.viewer.tools.statistic.report.nodeinfo;

public class NodeInfo {
    private String name;

    private int memAvailable = 0;

    private int memMaximum = 0;

    public NodeInfo( String name, int memAvailable, int memMaximum) {
        this.memAvailable = memAvailable;
        this.memMaximum = memMaximum;
        this.name = name;
    }

    public int getMemAvailable() {
        return memAvailable;
    }

    public int getMemMaximum() {
        return memMaximum;
    }

    public String getName() {
        return name;
    }

    public int getMemBusy(){
        return memMaximum - memAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeInfo nodeInfo = (NodeInfo) o;

        if (name != null ? !name.equals(nodeInfo.name) : nodeInfo.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
