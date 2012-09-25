package com.zh.coherence.viewer.tools.report.jmx.chamomile;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class GraphPanel extends JPanel {

    private List<FireflyCarrier> list = new LinkedList<FireflyCarrier>();
    private GridLayout gridLayout = new GridLayout();

    public GraphPanel() {
        gridLayout.setHgap(2);
        gridLayout.setVgap(2);
        setLayout(gridLayout);
    }

    public void addCarrier(FireflyCarrier carrier){
        list.add(carrier);
        add(carrier);

        int columns = (int) Math.floor(Math.sqrt(list.size()));
        System.err.println("columns: " + columns);
        gridLayout.setColumns(columns);
        gridLayout.setRows(list.size() / columns);
        updateUI();
    }

    public void clear(){
        this.removeAll();
        gridLayout = new GridLayout();
    }
}
