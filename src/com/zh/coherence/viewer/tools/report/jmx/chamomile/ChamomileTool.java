package com.zh.coherence.viewer.tools.report.jmx.chamomile;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.report.jmx.action.AutoRefreshJmxDataAction;
import com.zh.coherence.viewer.tools.report.jmx.action.RefreshJmxDataAction;
import layout.TableLayout;
import org.jdesktop.swingx.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

public class ChamomileTool extends JXPanel implements CoherenceViewerTool {
    @Override
    public JComponent getPane() {
        return this;
    }

    public ChamomileTool() {
        setBackground(Color.WHITE);
        createUI();
    }

    private void createUI() {
        setLayout(new BorderLayout());
        //glass control pane
        JXPanel controlPanel = new JXPanel(new VerticalLayout(4));
        controlPanel.setBackground(Color.WHITE);

        controlPanel.add(new JXTitledSeparator(
                "<html><b>Chamomile control panel</b></html>", SwingConstants.HORIZONTAL));
        JXPanel buttons = new JXPanel(new TableLayout(new double[][]{
                {FILL, PREFERRED, 25, PREFERRED, FILL}, {PREFERRED}
        }));
        buttons.setBackground(Color.WHITE);
        buttons.add(new JXButton(new RefreshJmxDataAction()), "1,0");
        buttons.add(new JXButton(new AutoRefreshJmxDataAction()), "3,0");
        controlPanel.add(buttons);
        controlPanel.add(new JXTitledSeparator("<html><b>Preset</b></html>", SwingConstants.HORIZONTAL));
        JXComboBox dataType = new JXComboBox();
        //todo model
        controlPanel.add(dataType);
        controlPanel.add(new JXTitledSeparator("<html><b>User</b></html>", SwingConstants.HORIZONTAL));
        JXComboBox userReportType = new JXComboBox();
        //todo model
        controlPanel.add(userReportType);

        JXComboBox userReportItem = new JXComboBox();
        //todo model
        controlPanel.add(userReportItem);

        controlPanel.add(new JXTitledSeparator("<html><b>Label</b></html>", SwingConstants.HORIZONTAL));

        //todo info
        controlPanel.add(new JXTitledSeparator("<html><b>Info</b></html>", SwingConstants.HORIZONTAL));
        JTextArea info = new JTextArea();
        controlPanel.add(info);

        //chart
        JPanel clusterAveragePanel = new JPanel();
        controlPanel.add(new JXTitledSeparator("<html><b>Average for the Cluster</b></html>", SwingConstants.HORIZONTAL));

        controlPanel.add(clusterAveragePanel);



        controlPanel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        add(controlPanel, BorderLayout.WEST);

        //chamomile panel
        JXPanel chamomilePanel = new JXPanel(new BorderLayout());

        final GraphPanel gp = new GraphPanel();
        chamomilePanel.add(gp, BorderLayout.CENTER);
        add(chamomilePanel, BorderLayout.CENTER);
    }
}
