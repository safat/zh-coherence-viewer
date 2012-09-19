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

        for(int i = 0; i < 32; i++){
            FireflyCarrier carrier = new FireflyCarrier("Test_" + i);
            for(int j = 0; j < 16; j++){
                Firefly f1 = new Firefly("F1_" + i + "_j_"+j);
                f1.setValue("F1_" + i + "_j_"+j);
                carrier.addFirefly(f1);
            }

            addCarrier(carrier);
        }
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
}
