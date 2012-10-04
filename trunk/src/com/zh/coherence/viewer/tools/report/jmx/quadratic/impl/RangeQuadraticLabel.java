package com.zh.coherence.viewer.tools.report.jmx.quadratic.impl;

import com.zh.coherence.viewer.tools.report.jmx.quadratic.ColorIcon;
import com.zh.coherence.viewer.tools.report.jmx.quadratic.Firefly;
import com.zh.coherence.viewer.tools.report.jmx.quadratic.QuadraticLabel;
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
        Range range = RangeHelper.parseRangeTag(qName, attributes);
        if(range != null){
            ranges.add(range);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

    }

    @Override
    public void lightFireflies(Collection<Firefly> fireflies) {
        Double value;
        for(Firefly firefly : fireflies){
            try{
                value = Double.valueOf(String.valueOf(firefly.getValue()));
                Color c = null;
                for(Range range : ranges){
                    if(range.isInRange(value)){
                        c = range.color;
                        break;
                    }
                }
                firefly.setColor(c == null?Color.GRAY:c);
            }catch (NumberFormatException ex){
                System.err.println("value is not numeric: " + firefly.getValue());
            }
        }
    }

    @Override
    public JPanel getLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(ranges.size(), 2, 0, 2));
        panel.setBackground(Color.WHITE);
        JLabel label;
        for (Range range : ranges) {
            label = new JLabel("" + range.firstValue + " - " + range.secondValue, new ColorIcon(range.color), SwingConstants.LEFT);
            label.setBackground(Color.WHITE);
            panel.add(label);
        }

        return panel;
    }
}
