package com.zh.coherence.viewer.tools.statistic.tabs;

import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.tools.statistic.report.NodeReport;
import com.zh.coherence.viewer.tools.statistic.report.nodeinfo.MachineInfo;
import com.zh.coherence.viewer.tools.statistic.report.nodeinfo.NodeInfo;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class NodeInfoPane extends JXPanel {

    private JXTreeTable treeTable;
    private TreeTableModelImpl treeTableModel;
    private JMXReport report;

    public NodeInfoPane(JMXReport report) {
        this.report = report;
        initUi();
    }

    private void initUi(){
        setLayout(new BorderLayout());
        treeTable = new JXTreeTable();
        treeTableModel = new TreeTableModelImpl(report.getNodeReport());
        treeTable.setTreeTableModel(treeTableModel);
        treeTable.setRootVisible(true);
        report.addListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                treeTableModel.fireNewRoot();
                treeTable.expandAll();
            }
        });

        add(new JScrollPane(treeTable), BorderLayout.CENTER);
    }

    private class TreeTableModelImpl extends AbstractTreeTableModel {
        private TreeTableModelImpl(Object root) {
            super(root);
        }

        private String[] header = new String[] {"Tree", "mem Max", "mem Available", "mem Busy"};

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int column) {
            return header[column];
        }

        @Override
        public Object getValueAt(Object o, int i) {
            if(o instanceof MachineInfo){
                MachineInfo info = (MachineInfo) o;
                switch (i){
                    case 0: return info.getName();
                    case 1: return info.getMemMaximum();
                    case 2: return info.getMemAvailable();
                    case 3: return info.getMemMaximum() - info.getMemAvailable();
                }
            }else if(o instanceof NodeInfo){
                NodeInfo info = (NodeInfo) o;
                switch (i){
                    case 0: return info.getName();
                    case 1: return info.getMemMaximum();
                    case 2: return info.getMemAvailable();
                    case 3: return info.getMemMaximum() - info.getMemAvailable();
                }
            }else if(o instanceof NodeReport){
                NodeReport report = ((NodeReport) o);
                switch (i){
                    case 0: return "Memory report";
                    case 1: return report.getMemMaximum();
                    case 2: return report.getMemAvailable();
                    case 3: return report.getMemBusy();
                }
            }
            return null;
        }

        @Override
        public Object getChild(Object parent, int index) {
            if(parent instanceof NodeReport){
                return ((NodeReport) parent).getData().get(index);
            }else if(parent instanceof MachineInfo){
                return ((MachineInfo) parent).getNodes().get(index);
            }
            return null;
        }

        @Override
        public int getChildCount(Object parent) {
            if(parent instanceof NodeReport){
                return ((NodeReport)parent).size();
            }else if(parent instanceof MachineInfo){
                return ((MachineInfo) parent).getNodes().size();
            }
            return 0;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            if(parent instanceof NodeReport){
                return ((NodeReport) parent).getData().indexOf(child);
            }else if(parent instanceof MachineInfo){
                return ((MachineInfo)parent).getNodes().indexOf(child);
            }

            return 0;
        }

        public void fireNewRoot(){
            modelSupport.fireNewRoot();
        }
    }
}
