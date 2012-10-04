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

public class PercentRangeQuadraticLabel implements QuadraticLabel {
    private List<Range> ranges = new ArrayList<Range>();

    @Override
    public void lightFireflies(Collection<Firefly> fireflies) {
        //collect info
        Double value;
        double max = 0, min = Double.MAX_VALUE, percent;

        for(Firefly firefly : fireflies){
            if(firefly.getValue() == null){
                continue;
            }
            try{
                value = Double.valueOf(String.valueOf(firefly.getValue()));
                if(value > max){
                    max = value;
                }
                if(value < min){
                    min = value;
                }
            }catch (NumberFormatException ex){
                System.err.println("Couldn't process : " + firefly.getValue());
            }
        }
        //light fireflies
        min = -min;

        max += min;
        for(Firefly firefly : fireflies){
            if(firefly.getValue() == null){
                continue;
            }
            try{
                value = Double.valueOf(String.valueOf(firefly.getValue()));
                //find percent
                value += min;
                if(value != 0 && max != 0){
                    percent = value / max * 100;
                }else {
                    percent = 0;
                }
                Color c = null;
                for(Range range : ranges){
                    if(range.isInRange(percent)){
                        c = range.color;
                        break;
                    }
                }
                firefly.setColor(c == null?Color.GRAY:c);
            }catch (NumberFormatException ex){
                System.err.println("Couldn't process : " + firefly.getValue());
            }
        }
    }

    @Override
    public JPanel getLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(ranges.size(), 2, 0, 2));
        panel.setBackground(Color.WHITE);
        JLabel label;
        for (Range range : ranges) {
            label = new JLabel("" + range.firstValue + "% - " + range.secondValue + "%", new ColorIcon(range.color), SwingConstants.LEFT);
            label.setBackground(Color.WHITE);
            panel.add(label);
        }

        return panel;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        Range range = RangeHelper.parseRangeTag(qName, attributes);
        if (range != null) {
            ranges.add(range);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

    }
}
