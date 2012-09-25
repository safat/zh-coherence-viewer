package com.zh.coherence.viewer.tools.report.jmx.chamomile.impl;

import com.zh.coherence.viewer.tools.report.jmx.chamomile.ColorIcon;
import com.zh.coherence.viewer.tools.report.jmx.chamomile.Firefly;
import com.zh.coherence.viewer.tools.report.jmx.chamomile.QuadraticLabel;
import com.zh.coherence.viewer.tools.report.jmx.chamomile.QuadraticManager;
import org.xml.sax.Attributes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RangeQuadraticLabel implements QuadraticLabel {

    private List<Range> ranges = new ArrayList<Range>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (QuadraticManager.TAG_ARG.equalsIgnoreCase(qName)) {
            String[] range = attributes.getValue("range").split("~");
            String color = attributes.getValue("color");
            ranges.add(new Range(Double.valueOf(range[0]), Double.parseDouble(range[1]), Color.decode(color)));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

    }

    @Override
    public Color lightFireflies(Collection<Firefly> fireflies) {
        return null;
    }

    @Override
    public JPanel getLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(ranges.size(), 2, 0, 2));
        panel.setBackground(Color.WHITE);
        JLabel label;
        for (Range range : ranges) {
            label = new JLabel("" + range.firstValue + "-" + range.secondValue, new ColorIcon(range.color), SwingConstants.LEFT);
            label.setBackground(Color.WHITE);
            panel.add(label);
        }

        return panel;
    }

    private class Range {
        double firstValue;
        double secondValue;

        Color color;

        private Range(double firstValue, double secondValue, Color color) {
            this.firstValue = firstValue;
            this.secondValue = secondValue;
            this.color = color;
        }
    }
}
