package com.zh.coherence.viewer.tools.report.jmx.chamomile.impl;

import com.zh.coherence.viewer.tools.report.jmx.chamomile.QuadraticDataProvider;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import org.xml.sax.Attributes;

import java.util.Map;

public class DefaultQuadraticDataProvider extends QuadraticDataProvider {
    private String[] path;

    public static final String TARGET_NODE = "node";

    @Override
    public String getInfo() {
        return JMXReport.getInstance().getPropertyContainer().getDescription(path[1]);
    }

    @Override
    public void updateData() {
        if(TARGET_NODE.equalsIgnoreCase(path[0])){
            Map<Integer, Map<String, Object>> nodeReport = JMXReport.getInstance().getNodeInfo();
            String field = path[1];
            for(int key : data.keySet()){
                data.get(key).setValue(nodeReport.get(key).get(field));
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch,  start, length).trim();
        if(value != null && !value.isEmpty()){
            path = value.split("/");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

    }
}
