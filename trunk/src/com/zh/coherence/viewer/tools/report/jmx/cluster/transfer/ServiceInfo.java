package com.zh.coherence.viewer.tools.report.jmx.cluster.transfer;

public class ServiceInfo {
    public static final String MACHINE_SAFE = "MACHINE-SAFE";
    public static final String NODE_SAFE = "NODE-SAFE";
    public static final String ENDANGERED = "ENDANGERED";

    public String name;
    public int totalNodes;
    public int storageNodes;
    public int caches;
    public int objects;
    public long units;
    public String status;
    public int senior;
    public String type;
}
