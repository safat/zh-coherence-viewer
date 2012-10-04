package com.zh.coherence.viewer.tools.report.jmx.quadratic.impl;

import com.zh.coherence.viewer.tools.report.jmx.quadratic.QuadraticDataProvider;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

public class StatusHAQuadraticDataProvider extends QuadraticDataProvider {

    public static final String MACHINE_SAFE = "MACHINE-SAFE";
    public static final String NODE_SAFE = "NODE-SAFE";
    public static final String ENDANGERED = "ENDANGERED";

    @Override
    public String getInfo() {
        return JMXReport.getInstance().getPropertyContainer().getDescription("StatusHA");
    }

    @Override
    public void updateData() {
        Map<JMXReport.CacheKey, Map<String, Object>> cacheData = JMXReport.getInstance().getServiceInfo();
        Map<Integer, Integer> assemble = new HashMap<Integer, Integer>();
        int id;
        for (JMXReport.CacheKey key : cacheData.keySet()) {
            id = key.getId();
            try {
                String value = String.valueOf(cacheData.get(key).get("StatusHA"));
                int code = getIntCode(value);
                Integer result = assemble.get(id);
                if (result == null) {
                    result = code;
                } else {
                    if (code > result) {
                        result = code;
                    }
                }
                assemble.put(id, result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        for (int key : data.keySet()) {
            data.get(key).setValue(getStringCode(assemble.get(key)));
        }
    }

    private String getStringCode(Integer iCode){
        if(iCode == null){
            return "n/a";
        }else{
            switch (iCode){
                case 0:
                    return MACHINE_SAFE;
                case 1:
                    return NODE_SAFE;
                case 2:
                    return ENDANGERED;
                default:
                    return "n/a";
            }
        }
    }

    private int getIntCode(String sCode) {
        int result = -1;
        if (sCode != null || !sCode.isEmpty()) {
            char ch = sCode.charAt(0);
            switch (ch) {
                case 'M':
                    result = 0;
                    break;
                case 'N':
                    result = 1;
                    break;
                case 'E':
                    result = 2;
            }
        }
        return result;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

    }
}
