package com.zh.coherence.viewer.tools.report.jmx.quadratic;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.report.jmx.action.AutoRefreshJmxDataAction;
import com.zh.coherence.viewer.tools.report.jmx.action.RefreshJmxDataAction;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import layout.TableLayout;
import org.jdesktop.swingx.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

public class QuadraticTool extends JXPanel implements CoherenceViewerTool, ChangeListener {

    private GraphPanel gp = new GraphPanel();

    private QuadraticManager manager;

    private QuadraticReportItem currentReportItem;

    private JPanel labelContainer;

    @Override
    public JComponent getPane() {
        manager = new QuadraticManager();
        labelContainer = new JPanel(new BorderLayout());
        labelContainer.setBackground(Color.WHITE);
        createUI();
        return this;
    }

    public QuadraticTool() {
        setBackground(Color.WHITE);
    }

    private void createUI() {
        setLayout(new BorderLayout());
        final JTextArea info = new JTextArea();

        //glass control pane
        JXPanel controlPanel = new JXPanel(new VerticalLayout(4));
        controlPanel.setBackground(Color.WHITE);

        controlPanel.add(new JXTitledSeparator(
                "<html><b>Quadratic control panel</b></html>", SwingConstants.HORIZONTAL));
        JXPanel buttons = new JXPanel(new TableLayout(new double[][]{
                {FILL, PREFERRED, 25, PREFERRED, FILL}, {PREFERRED}
        }));
        buttons.setBackground(Color.WHITE);
        buttons.add(new JXButton(new RefreshJmxDataAction()), "1,0");
        buttons.add(new JXButton(new AutoRefreshJmxDataAction()), "3,0");
        controlPanel.add(buttons);
        controlPanel.add(new JXTitledSeparator("<html><b>Report Type</b></html>", SwingConstants.HORIZONTAL));
        final JXComboBox categoryChooser = new JXComboBox();
        categoryChooser.addItem("--- Not selected ---");
        for (String item : manager.getCategories()) {
            categoryChooser.addItem(item);
        }
        controlPanel.add(categoryChooser);

        final JXComboBox reportItem = new JXComboBox();
        //todo listener
        controlPanel.add(reportItem);

        categoryChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reportItem.removeAllItems();
                String category = String.valueOf(categoryChooser.getSelectedItem());
                Collection<QuadraticReportItem> reports = manager.getReports(category);
                if (reports != null) {
                    for (Object o : reports) {
                        reportItem.addItem(o);
                    }
                }
            }
        });
        reportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //update label

                //update info
                Object selected = reportItem.getSelectedItem();
                if (selected != null && selected instanceof QuadraticReportItem) {
                    currentReportItem = (QuadraticReportItem) selected;
                    info.setText(currentReportItem.getDataProvider().getInfo());
                    labelContainer.removeAll();
                    labelContainer.add(currentReportItem.getLabel().getLabelPanel());
                    rebuildGraphPanel();
                } else {
                    info.setText("");
                    currentReportItem = null;
                    labelContainer.removeAll();
                    rebuildGraphPanel();
                }

            }
        });

        controlPanel.add(new JXTitledSeparator("<html><b>Label</b></html>", SwingConstants.HORIZONTAL));
        JPanel labelWrapper = new JPanel(new BorderLayout(4,0));
        labelWrapper.add(new JLabel(), BorderLayout.WEST);
        labelWrapper.add(labelContainer, BorderLayout.CENTER);
        controlPanel.add(labelWrapper);

        controlPanel.add(new JXTitledSeparator("<html><b>Info</b></html>", SwingConstants.HORIZONTAL));
        info.setColumns(25);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setFont(new Font("Dialog", Font.PLAIN, 10));
        info.setEditable(false);
        controlPanel.add(new JScrollPane(info));

        controlPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(controlPanel, BorderLayout.WEST);

        //quadratic panel
        JXPanel chamomilePanel = new JXPanel(new BorderLayout());

        chamomilePanel.add(gp, BorderLayout.CENTER);
        add(chamomilePanel, BorderLayout.CENTER);
        JMXReport.getInstance().addListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        rebuildGraphPanel();
    }

    private void rebuildGraphPanel() {
        if(currentReportItem != null && currentReportItem.getDataProvider() != null){
            currentReportItem.getDataProvider().initialize(gp);
            currentReportItem.getLabel().lightFireflies(currentReportItem.getDataProvider().getFireflies().values());
        }else{
            //todo!
        }
    }
}
