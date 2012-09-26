package com.zh.coherence.viewer.tools.report.jmx.chamomile.impl;

import com.zh.coherence.viewer.tools.report.jmx.chamomile.QuadraticManager;
import org.xml.sax.Attributes;

import java.awt.*;

public class RangeHelper {
    public static Range parseRangeTag(String qName, Attributes attributes) {
        if (QuadraticManager.TAG_ARG.equalsIgnoreCase(qName)) {
            String[] range = attributes.getValue("range").split("~");
            String color = attributes.getValue("color");
            return new Range(Double.valueOf(range[0]), Double.parseDouble(range[1]), Color.decode(color));
        } else {
            return null;
        }
    }
}
