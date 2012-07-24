package com.zh.coherence.viewer.tools.statistic.report.nodeinfo;

import com.zh.coherence.viewer.tools.statistic.report.Named;

public class NodeInfo implements Named{
    private String name;

    private int memAvailable = 0;

    private int memMaximum = 0;

    private int units = 0;

    public NodeInfo( String name, int memAvailable, int memMaximum, int units) {
        this.memAvailable = memAvailable;
        this.memMaximum = memMaximum;
        this.name = name;
        this.units = units;
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

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
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
