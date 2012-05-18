package com.zh.coherence.viewer.tools.statistic.report.cache;

import com.zh.coherence.viewer.tools.statistic.report.Named;

public class CacheNodeInfo implements Named{
    private int size;
    private long totalPuts;
    private long totalGets;
    private long cacheHits;
    private double averageGetMillis;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverageGetMillis() {
        return averageGetMillis;
    }

    public void setAverageGetMillis(double averageGetMillis) {
        this.averageGetMillis = averageGetMillis;
    }

    public long getCacheHits() {
        return cacheHits;
    }

    public void setCacheHits(long cacheHits) {
        this.cacheHits = cacheHits;
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
}
