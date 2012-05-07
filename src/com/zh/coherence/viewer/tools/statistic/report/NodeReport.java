package com.zh.coherence.viewer.tools.statistic.report;

import java.util.Map;

public class NodeReport {
    private Map<String, Object> attributes;

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public int getMemoryMax(){
        return 0;
    }

    public int getMemoryAvailable(){
        return 0;
    }

    public void updateData(Map<String, Object> attributes){
        //todo average
    }
}
