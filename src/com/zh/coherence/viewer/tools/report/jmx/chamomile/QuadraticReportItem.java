package com.zh.coherence.viewer.tools.report.jmx.chamomile;

public class QuadraticReportItem {
    private String name;

    private QuadraticLabel label;

    private QuadraticDataProvider dataProvider;

    public QuadraticReportItem() {
    }

    public QuadraticReportItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuadraticLabel getLabel() {
        return label;
    }

    public void setLabel(QuadraticLabel label) {
        this.label = label;
    }

    public QuadraticDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(QuadraticDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public QuadraticDataProvider initDataProvider(String clazz){
        try {
            dataProvider = (QuadraticDataProvider) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            System.err.println("Couldn't initialize class: " + clazz);
            e.printStackTrace();
        }

        return dataProvider;
    }

    public QuadraticLabel initLabel(String clazz){
        try {
            label = (QuadraticLabel) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            System.err.println("Couldn't initialize class: " + clazz);
            e.printStackTrace();
        }

        return label;
    }

    @Override
    public String toString() {
        return name;
    }
}
