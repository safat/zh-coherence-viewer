package com.zh.coherence.viewer.tools.statistic.report.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 18.05.12
 * Time: 21:44
 */
public class CacheInfo {
    private List<CacheNodeInfo> nodes = new ArrayList<CacheNodeInfo>();

    private int size = 0;

    private long totalPuts = 0;

    private long totalGets = 0;

    private String name;

    public CacheInfo(String name) {
        this.name = name;
    }

    public void addCacheNodeInfo(CacheNodeInfo info){
        nodes.add(info);
        size += info.getSize();
        totalPuts += info.getTotalPuts();
        totalGets += info.getTotalGets();
    }

    public List<CacheNodeInfo> getNodes() {
        return nodes;
    }

    public void setNodes(List<CacheNodeInfo> nodes) {
        this.nodes = nodes;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalGets() {
        return totalGets;
    }

    public void setTotalGets(long totalGets) {
        this.totalGets = totalGets;
    }

    public long getTotalPuts() {
        return totalPuts;
    }

    public void setTotalPuts(long totalPuts) {
        this.totalPuts = totalPuts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
