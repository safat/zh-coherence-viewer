package com.zh.coherence.viewer.tools.statistic.config;

import java.util.ArrayList;
import java.util.List;

public class TabContainer {

    private String name;

    private TabType type;

    private List<TabItem> items = new ArrayList<TabItem>();

    public void addTabItem(TabItem tabItem){
        if(items == null){
            items = new ArrayList<TabItem>();
        }

        items.add(tabItem);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TabType getType() {
        return type;
    }

    public void setType(TabType type) {
        this.type = type;
    }

    public List<TabItem> getItems() {
        return items;
    }

    public void setItems(List<TabItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TabContainer");
        sb.append("{name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
