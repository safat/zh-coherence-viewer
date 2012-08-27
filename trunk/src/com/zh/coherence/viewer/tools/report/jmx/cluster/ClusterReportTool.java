package com.zh.coherence.viewer.tools.report.jmx.cluster;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.report.jmx.action.AutoRefreshJmxDataAction;
import com.zh.coherence.viewer.tools.report.jmx.action.RefreshJmxDataAction;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import layout.TableLayout;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXRadioGroup;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

public class ClusterReportTool extends JXPanel implements CoherenceViewerTool, ChangeListener {

    private JTextField clusterName, totalNodesCount, license;
    private JProgressBar totalMemory;
    private static final String MODEL_SERVICES = "Services";
    private static final String MODEL_CLUSTER = "Cluster info";

    private ClusterReportDataProvider dataProvider;
    private ServiceTableModel serviceTableModel;
    private ClusterTableModel clusterTableModel;
    private JXTable table;

    public ClusterReportTool() {
        dataProvider = new ClusterReportDataProvider();
    }

    @Override
    public JComponent getPane() {
        this.setLayout(new BorderLayout());
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setDividerLocation(200);
        add(split, BorderLayout.CENTER);

        JPanel north = new JPanel();
        JPanel south = new JPanel();
        split.setLeftComponent(north);
        split.setRightComponent(south);

//NORTH
        TableLayout northLayout = new TableLayout(new double[][]{
                {2, FILL, 2},
                {2, PREFERRED, 2, PREFERRED, 2, FILL, 2}
        });
        north.setLayout(northLayout);
        //Info
        JComponent infoPanel = generateNorthInfoPane();
        north.add(infoPanel, "1,1");
        serviceTableModel = new ServiceTableModel(dataProvider);
        table = new JXTable(){
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (column == 0 && getModel() instanceof ClusterTableModel) {
                    JComponent jc = (JComponent) c;
                    String key = getValueAt(row, column).toString();
                    jc.setToolTipText(dataProvider.getPropertyDescription(key));
                }
                return c;
            }
        };
        north.add(generateNorthToolPanel(), "1,3");
        north.add(new JScrollPane(table), "1,5");
//SOUTH

        JMXReport.getInstance().addListener(this);
        return this;
    }

    private JComponent generateNorthInfoPane() {
        JPanel infoPanel = new JPanel(new TableLayout(new double[][]{
                {PREFERRED, 2, FILL, 2, PREFERRED, 2, FILL, 2, PREFERRED, 2, FILL, 2, PREFERRED, 2, FILL, 2, PREFERRED, 2, PREFERRED}, {FILL}}));
        infoPanel.add(new JLabel("Cluster name:"), "0,0");
        clusterName = new JTextField();
        clusterName.setEditable(false);
        clusterName.setEnabled(false);
        infoPanel.add(clusterName, "2,0");
        infoPanel.add(new JLabel("Total nodes:"), "4,0");
        totalNodesCount = new JTextField();
        totalNodesCount.setEditable(false);
        totalNodesCount.setEnabled(false);
        infoPanel.add(totalNodesCount, "6,0");
        infoPanel.add(new JLabel("Total memory:"), "8,0");
        totalMemory = new JProgressBar();
        totalMemory.setEnabled(false);
        totalMemory.setForeground(Color.LIGHT_GRAY);
        infoPanel.add(totalMemory, "10,0");
        infoPanel.add(new JLabel("License:"), "12,0");
        license = new JTextField();
        license.setEditable(false);
        license.setEnabled(false);
        infoPanel.add(license, "14,0");
        JButton refresh = new JButton(new RefreshJmxDataAction());
        infoPanel.add(refresh, "16,0");
        JToggleButton autoRefresh = new JToggleButton(new AutoRefreshJmxDataAction());
        infoPanel.add(autoRefresh, "18,0");

        return infoPanel;
    }

    private JComponent generateNorthToolPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());

        final JXRadioGroup<String> radioGroup = new JXRadioGroup<String>(new String[]{MODEL_SERVICES, MODEL_CLUSTER});
        panel.add(radioGroup, BorderLayout.CENTER);

        serviceTableModel = new ServiceTableModel(dataProvider);
        clusterTableModel = new ClusterTableModel(dataProvider);

        radioGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(MODEL_SERVICES.equals(radioGroup.getSelectedValue())){
                    table.setModel(serviceTableModel);
                }else{
                    table.setModel(clusterTableModel);
                }
                stateChanged(null);
            }
        });

        radioGroup.setSelectedValue(MODEL_SERVICES);

        return panel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        clusterName.setText(dataProvider.getClusterName());
        totalNodesCount.setText("" + dataProvider.getClusterSize());
        ClusterReportDataProvider.MemoryInfo memoryInfo = dataProvider.getTotalMemory();
        totalMemory.setMaximum(memoryInfo.maxMB);
        totalMemory.setMinimum(0);
        totalMemory.setValue(memoryInfo.maxMB - memoryInfo.availableMB);
        totalMemory.setString(memoryInfo.busyLabel + "/" + memoryInfo.maxLabel);
        totalMemory.setStringPainted(true);
        license.setText(dataProvider.getLicense());

        ((AbstractTableModel)table.getModel()).fireTableDataChanged();
    }
}