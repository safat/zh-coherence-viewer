package com.zh.coherence.viewer.tools.report.jmx.chamomile.impl;

import com.zh.coherence.viewer.tools.report.jmx.chamomile.QuadraticDataProvider;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import org.xml.sax.Attributes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DefaultQuadraticDataProvider extends QuadraticDataProvider {
    private String[] path;

    public static final String TARGET_NODE = "node";
    public static final String TARGET_CACHE = "cache";
    public static final String TARGET_SERVICE = "service";

    @Override
    public String getInfo() {
        return JMXReport.getInstance().getPropertyContainer().getDescription(path[1]);
    }

    @Override
    public void updateData() {
        if (TARGET_NODE.equalsIgnoreCase(path[0])) {
            Map<Integer, Map<String, Object>> nodeReport = JMXReport.getInstance().getNodeInfo();
            String field = path[1];
            for (int key : data.keySet()) {
                data.get(key).setValue(nodeReport.get(key).get(field));
            }
        } else if (TARGET_CACHE.equalsIgnoreCase(path[0]) || TARGET_SERVICE.equalsIgnoreCase(path[0])) {
            Map<JMXReport.CacheKey, Map<String, Object>> cacheData;// = JMXReport.getInstance().getCacheInfo();
            if(TARGET_CACHE.equalsIgnoreCase(path[0])){
                cacheData = JMXReport.getInstance().getCacheInfo();
            }else{
                cacheData = JMXReport.getInstance().getServiceInfo();
            }
            Map<Integer, Double> assemble = new HashMap<Integer, Double>();
            int id;
            Double value;
            //collect info
            for (JMXReport.CacheKey key : cacheData.keySet()) {
                id = key.getId();
                try {
                    value = Double.valueOf(String.valueOf(cacheData.get(key).get(path[1])));
                    if(value == -1.0){
                        value = 0d;
                    }
                    Double v = assemble.get(id);
                    if(v != null){
                        assemble.put(id, value + v);
                    }else{
                        assemble.put(id, value);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            //set info
            for (int key : data.keySet()) {
                data.get(key).setValue(assemble.get(key));
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length).trim();
        if (value != null && !value.isEmpty()) {
            path = value.split("/");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

    }
}
