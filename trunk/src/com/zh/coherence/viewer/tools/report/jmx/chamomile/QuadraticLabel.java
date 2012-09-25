package com.zh.coherence.viewer.tools.report.jmx.chamomile;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public interface QuadraticLabel extends QuadraticTagHandler {

    public Color lightFireflies(Collection<Firefly> fireflies);

    public JPanel getLabelPanel();
}
