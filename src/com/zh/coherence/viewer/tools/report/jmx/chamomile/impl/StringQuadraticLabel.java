package com.zh.coherence.viewer.tools.report.jmx.chamomile.impl;

import com.zh.coherence.viewer.tools.report.jmx.chamomile.ColorIcon;
import com.zh.coherence.viewer.tools.report.jmx.chamomile.Firefly;
import com.zh.coherence.viewer.tools.report.jmx.chamomile.QuadraticLabel;
import com.zh.coherence.viewer.tools.report.jmx.chamomile.QuadraticManager;
import org.xml.sax.Attributes;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StringQuadraticLabel implements QuadraticLabel {
    private Map<String, Color> data = new HashMap<String, Color>();

    @Override
    public void lightFireflies(Collection<Firefly> fireflies) {
        for(Firefly firefly : fireflies){
            try{
                Color color = data.get(String.valueOf(firefly.getValue()));
                if(color == null){
                    color = Color.GRAY;
                }
                firefly.setColor(color);
            }catch (Exception ex){
                System.err.println("Other unknown exception with value: " + firefly.getValue());
            }
        }
    }

    @Override
    public JPanel getLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(data.size(), 2, 0, 2));
        panel.setBackground(Color.WHITE);
        JLabel label;
        for (Map.Entry<String, Color> entry : data.entrySet()) {
            label = new JLabel(entry.getKey(), new ColorIcon(entry.getValue()), SwingConstants.LEFT);
            label.setBackground(Color.WHITE);
            panel.add(label);
        }

        return panel;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (QuadraticManager.TAG_ARG.equalsIgnoreCase(qName)) {
            String key = attributes.getValue("text");
            String color = attributes.getValue("color");
            data.put(key,  Color.decode(color));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

    }
}
