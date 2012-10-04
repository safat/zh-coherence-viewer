package com.zh.coherence.viewer.tools.report.jmx.quadratic;

import layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class FireflyCarrier extends JPanel {

    private List<Firefly> list = new LinkedList<Firefly>();
    private JPanel centerPanel;

    private GridLayout gridLayout;

    public FireflyCarrier(String title) {
        super(new TableLayout(new double[][]{
                {TableLayout.FILL},
                {TableLayout.PREFERRED, TableLayout.FILL}
        }));
        setBackground(Color.WHITE);

        add(new HeaderPanel(title), "0,0");

        gridLayout = new GridLayout();
        gridLayout.setHgap(1);
        gridLayout.setVgap(1);
        centerPanel = new JPanel(gridLayout);
        add(centerPanel, "0,1");
        centerPanel.setBackground(Color.BLACK);
    }

    public void addFirefly(Firefly firefly){
        list.add(firefly);
        centerPanel.add(firefly);
        int columns = (int) Math.floor(Math.sqrt(list.size()));
        gridLayout.setColumns(columns);
        gridLayout.setRows(list.size() / columns);
    }
}
