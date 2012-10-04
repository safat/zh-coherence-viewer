package com.zh.coherence.viewer.tools.report.jmx.quadratic;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private JLabel titleLabel;

    public HeaderPanel(String title) {
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);

        add(titleLabel);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = getBackground();
        Color color2 = color1.darker();
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
