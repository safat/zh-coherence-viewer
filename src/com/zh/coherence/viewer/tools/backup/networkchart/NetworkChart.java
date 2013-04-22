package com.zh.coherence.viewer.tools.backup.networkchart;

import com.zh.coherence.viewer.utils.FileUtils;
import org.jdesktop.swingx.JXPanel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Iterator;

public class NetworkChart extends JXPanel {

    private NetworkChartModel model;

    public NetworkChart(NetworkChartModel model) {
        setModel(model);
    }

    public NetworkChart() {
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics = (Graphics2D) g;

        int size = model.getValues().size();
        if (size > 0) {
            int max = model.getMax();
            Iterator<Integer> iterator = model.getValues().iterator();
            Integer value;

            graphics.setColor(Color.BLUE);
            graphics.setStroke(new BasicStroke(2));
            int width = getWidth();
            int height = getHeight();
            double coeff = (double) height / max;

            while (iterator.hasNext()) {
                value = iterator.next();
                int x1 = width - size * 2;
                int y1 = height - (int) (value * coeff);
                graphics.drawLine(x1, y1, x1, height);
                size--;
            }
        }
//saved megabytes
        String all = FileUtils.convertToStringRepresentation(model.getSum());
        if (model.getSum() > 0) {
            graphics.setColor(new Color(255, 255, 255, 150));
            graphics.fillRoundRect(15, getHeight() - 20, all.length() * 6, 15, 4, 4);
            graphics.setColor(Color.BLUE);
            graphics.drawString(all, 16, getHeight() - 8);
        }
//current
        if (model.getValues().size() > 0) {
            int shift = 15 + (all.length() * 6) + 5;
            String curr = FileUtils.convertToStringRepresentation(model.getValues().getLast()/2) + "/s";
            graphics.setColor(new Color(255, 255, 255, 200));
            graphics.fillRoundRect(shift, getHeight() - 20, (curr.length()+1) * 6, 15, 4, 4);
            Font oldFont = graphics.getFont();
            graphics.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize()));
            graphics.setColor(new Color(50, 150, 50));
            graphics.drawString(curr, shift + 1, getHeight() - 8);
        }
    }

    public NetworkChartModel getModel() {
        return model;
    }

    public void setModel(NetworkChartModel model) {
        this.model = model;
        model.addListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                repaint();
            }
        });
    }
}
