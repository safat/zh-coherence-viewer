package com.zh.coherence.viewer.tools.report.jmx.chamomile;

import com.zh.coherence.viewer.tools.statistic.report.JMXReport;

import java.util.HashMap;
import java.util.Map;

public abstract class QuadraticDataProvider implements QuadraticTagHandler {
    private JMXReport report;

    protected Map<Integer, Firefly> data = new HashMap<Integer, Firefly>();

    public QuadraticDataProvider() {
        report = JMXReport.getInstance();
    }

    public void initialize(GraphPanel graphPanel){
        graphPanel.clear();
        data.clear();
        Map<String, FireflyCarrier> carrierMap = new HashMap<String, FireflyCarrier>();

        Map<Integer, Map<String, Object>> nodeInfo = report.getNodeInfo();
        Firefly firefly;
        Map<String, Object> values;
        for(int id : nodeInfo.keySet()){
            firefly = new Firefly(String.valueOf(id));
            data.put(id, firefly);
            values = nodeInfo.get(id);
            String server = (String) values.get("MachineName");

            FireflyCarrier carrier = carrierMap.get(server);
            if(carrier == null){
                carrier = new FireflyCarrier(server);
                carrierMap.put(server, carrier);
            }
            carrier.addFirefly(firefly);
        }

        for(FireflyCarrier carrier : carrierMap.values()){
            graphPanel.addCarrier(carrier);
        }

        updateData();
    }

    public Map<Integer, Firefly> getFireflies(){
        return data;
    }

    public abstract String getInfo();

    public abstract void updateData();
}
