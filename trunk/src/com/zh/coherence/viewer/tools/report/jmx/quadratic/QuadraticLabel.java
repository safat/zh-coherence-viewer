package com.zh.coherence.viewer.tools.report.jmx.quadratic;

import javax.swing.*;
import java.util.Collection;

public interface QuadraticLabel extends QuadraticTagHandler {

    public void lightFireflies(Collection<Firefly> fireflies);

    public JPanel getLabelPanel();
}
