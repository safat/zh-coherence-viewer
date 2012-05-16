package com.zh.coherence.viewer.tools.statistic.report.nodeinfo;

import java.util.ArrayList;
import java.util.List;

public class MachineInfo {

    private String name;

    private int memAvailable = 0;

    private int memMaximum = 0;

    private List<NodeInfo> nodes = new ArrayList<NodeInfo>();

    public MachineInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NodeInfo> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeInfo> nodes) {
        this.nodes = nodes;
    }

    public int getMemAvailable() {
        return memAvailable;
    }

    public int getMemMaximum() {
        return memMaximum;
    }

    public void incMemAvailable(int mem){
        memAvailable += mem;
    }

    public void incMemMax(int mem){
        memMaximum += mem;
    }

    public int getAverage(){
        if(nodes.size() == 0){
            return 0;
        }
        int res = 0;
        for(NodeInfo node : nodes){
            res += node.getMemBusy();
        }
        res = res / nodes.size();
        return res;
    }
}
