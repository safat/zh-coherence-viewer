package com.zh.coherence.viewer.tools.statistic.config;

public class TabItem {

    private String id;

    private String title;

    private Boolean enableChart;

    private Integer groupIndex;

    private String icon;

    public Boolean getEnableChart() {
        return enableChart;
    }

    public void setEnableChart(Boolean enableChart) {
        this.enableChart = enableChart;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
