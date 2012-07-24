package com.zh.coherence.viewer.utils.ui;

import layout.TableLayout;
import layout.TableLayoutConstants;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;

/**
 * JXPanel with horizontal layout
 */
public class ZHHorizontalPanel extends JXPanel {
    public static enum HorizontalPanelAlignment {LEFT, RIGHT, CENTER, FILL}

    /**
     * predefined horizontal panel with alignment: LEFT, horizontal gap: 0 and vertical gap: 0
     *
     * @param components Components which will be added on panel
     */
    public ZHHorizontalPanel(JComponent... components) {
        this(HorizontalPanelAlignment.LEFT, 0, 0, components);
    }

    public ZHHorizontalPanel(HorizontalPanelAlignment alignment, int hGap, int vGap, JComponent... components) {
        TableLayout layout = new TableLayout();
        setLayout(layout);
        int vIndex = 0;
        int hIndex = 0;
        if (vGap != 0) {
            layout.insertRow(0, vGap);
            layout.insertRow(1, TableLayoutConstants.PREFERRED);
            layout.insertRow(2, vGap);
            vIndex = 1;
        } else {
            layout.insertRow(0, TableLayoutConstants.PREFERRED);
        }
        //left gap

        layout.insertColumn(hIndex, hGap);
        if (alignment == HorizontalPanelAlignment.RIGHT || alignment == HorizontalPanelAlignment.CENTER) {
            layout.insertColumn(++hIndex, TableLayoutConstants.FILL);
        }
        double componentSize =
                alignment == HorizontalPanelAlignment.FILL ? TableLayoutConstants.FILL : TableLayoutConstants.PREFERRED;
        for (JComponent component : components) {
            layout.insertColumn(++hIndex, componentSize);
            add(component, "" + hIndex + "," + vIndex);
            layout.insertColumn(++hIndex, hGap);
        }
        if (alignment == HorizontalPanelAlignment.LEFT || alignment == HorizontalPanelAlignment.CENTER) {
            layout.insertColumn(++hIndex, TableLayoutConstants.FILL);
        }
    }
}
