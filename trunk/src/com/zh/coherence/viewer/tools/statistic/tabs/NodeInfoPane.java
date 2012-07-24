package com.zh.coherence.viewer.tools.statistic.tabs;

import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.tools.statistic.report.NodeReport;
import com.zh.coherence.viewer.tools.statistic.report.TreeTableStringValue;
import com.zh.coherence.viewer.tools.statistic.report.nodeinfo.MachineInfo;
import com.zh.coherence.viewer.tools.statistic.report.nodeinfo.NodeInfo;
import com.zh.coherence.viewer.utils.FileUtils;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
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

    private void initUi() {
        setLayout(new BorderLayout());
        treeTable = new JXTreeTable();
        treeTableModel = new TreeTableModelImpl(report.getNodeReport());
        treeTable.setTreeTableModel(treeTableModel);
        treeTable.setRootVisible(true);
        treeTable.setTreeCellRenderer(new DefaultTreeRenderer(getIconValue(), new TreeTableStringValue()));
        report.addListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                treeTableModel.fireNewRoot();
                treeTable.expandAll();
            }
        });

        add(new JScrollPane(treeTable), BorderLayout.CENTER);
    }

    private IconValue getIconValue(){
        return new IconValue() {
            @Override
            public Icon getIcon(Object o) {
                if(o instanceof NodeReport){
                    return IconHelper.getInstance().getIcon(com.zh.coherence.viewer.utils.icons.IconType.REPORT);
                }else if(o instanceof MachineInfo){
                    return IconHelper.getInstance().getIcon(com.zh.coherence.viewer.utils.icons.IconType.SERVER_NETWORK);
                }else if(o instanceof NodeInfo){
                    return IconHelper.getInstance().getIcon(com.zh.coherence.viewer.utils.icons.IconType.NODE);
                }
                return null;
            }
        };
    }

    private class TreeTableModelImpl extends AbstractTreeTableModel {
        public TreeTableModelImpl(Object root) {
            super(root);
        }

        private String[] header = new String[]{"Tree", "mem Max", "mem Available", "mem Busy", "Average busy"};

        @Override
        public int getColumnCount() {
            return header.length;
        }

        @Override
        public String getColumnName(int column) {
            return header[column];
        }

        @Override
        public Object getValueAt(Object o, int i) {
            if (o instanceof MachineInfo) {
                MachineInfo info = (MachineInfo) o;
                switch (i) {
                    case 1:
                        return FileUtils.convertToStringRepresentation(info.getMemMaximum(), "MB");
                    case 2:
                        return FileUtils.convertToStringRepresentation(info.getMemAvailable(), "MB");
                    case 3:
                        return FileUtils.convertToStringRepresentation(
                                info.getMemMaximum() - info.getMemAvailable(), "MB");
                    case 4:
                        return FileUtils.convertToStringRepresentation(info.getAverage(), "MB");
                    case 5:
                        return FileUtils.convertToStringRepresentation(info.getUnits(), "MB");
                }
            } else if (o instanceof NodeInfo) {
                NodeInfo info = (NodeInfo) o;
                switch (i) {
                    case 1:
                        return FileUtils.convertToStringRepresentation(info.getMemMaximum(), "MB");
                    case 2:
                        return FileUtils.convertToStringRepresentation(info.getMemAvailable(), "MB");
                    case 3:
                        return FileUtils.convertToStringRepresentation(
                                info.getMemMaximum() - info.getMemAvailable(), "MB");
//                    case 4:
//                        return FileUtils.convertToStringRepresentation(info.getUnits(), "MB");
                }
            } else if (o instanceof NodeReport) {
                NodeReport report = ((NodeReport) o);
                switch (i) {
                    case 1:
                        return FileUtils.convertToStringRepresentation(report.getMemMaximum(), "MB");
                    case 2:
                        return FileUtils.convertToStringRepresentation(report.getMemAvailable(), "MB");
                    case 3:
                        return FileUtils.convertToStringRepresentation(report.getMemBusy(), "MB");
                    case 4:
                        return FileUtils.convertToStringRepresentation(report.getAverage(), "MB");
                    case 5:
                        return FileUtils.convertToStringRepresentation(report.getUnits(), "MB");
                }
            }
            return "";
        }

        @Override
        public Object getChild(Object parent, int index) {
            if (parent instanceof NodeReport) {
                return ((NodeReport) parent).getData().get(index);
            } else if (parent instanceof MachineInfo) {
                return ((MachineInfo) parent).getNodes().get(index);
            }
            return null;
        }

        @Override
        public int getChildCount(Object parent) {
            if (parent instanceof NodeReport) {
                return ((NodeReport) parent).size();
            } else if (parent instanceof MachineInfo) {
                return ((MachineInfo) parent).getNodes().size();
            }
            return 0;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            if (parent instanceof NodeReport) {
                return ((NodeReport) parent).getData().indexOf(child);
            } else if (parent instanceof MachineInfo) {
                return ((MachineInfo) parent).getNodes().indexOf(child);
            }

            return 0;
        }

        public void fireNewRoot() {
            modelSupport.fireNewRoot();
        }
    }
}
