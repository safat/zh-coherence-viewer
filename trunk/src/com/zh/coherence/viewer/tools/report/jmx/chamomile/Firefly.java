package com.zh.coherence.viewer.tools.report.jmx.chamomile;

import layout.TableLayout;

import javax.swing.*;
import java.awt.*;

public class Firefly extends JPanel {

    private String name;
    private Object value;

    private Color color;

    private JLabel valueLabel;

    public Firefly(String name) {
        super(new BorderLayout());
        this.name = name;
        initUI();
    }

    public void setColor(Color color) {
        this.color = color;
        setBackground(color);
    }

    private void initUI(){
        valueLabel = new JLabel();
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(valueLabel, BorderLayout.CENTER);
        setColor(Color.GRAY);
    }

    public void setBackground(Color color){
        super.setBackground(color);
        if(valueLabel != null){
            int r = 255 - color.getRed();
            int g = 255 - color.getGreen();
            int b = 255 - color.getBlue();

            valueLabel.setForeground(new Color(r,g,b).brighter());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        valueLabel.setText(String.valueOf(value));
    }
}
