package com.zh.coherence.viewer.tools.report.jmx.quadratic.impl;

import java.awt.*;

public class Range {
    double firstValue;
    double secondValue;

    Color color;

    public Range(double firstValue, double secondValue, Color color) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.color = color;
    }

    public boolean isInRange(double value) {
        return value >= firstValue && value <= secondValue;
    }
}
