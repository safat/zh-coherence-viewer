package com.zh.coherence.viewer.tools.statistic.tabs;

import com.zh.coherence.viewer.tools.statistic.report.CacheReport;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.tools.statistic.report.TreeTableStringValue;
import com.zh.coherence.viewer.tools.statistic.report.cache.CacheInfo;
import com.zh.coherence.viewer.tools.statistic.report.cache.CacheNodeInfo;
import com.zh.coherence.viewer.utils.FileUtils;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class CachesInfoPane extends JXPanel {
    private JXTreeTable treeTable;
    private TreeTableModelImpl treeTableModel;
    private JMXReport report;

    public CachesInfoPane(JMXReport report) {
        this.report = report;
        initUi();
    }

    private void initUi() {
        setLayout(new BorderLayout());
        treeTable = new JXTreeTable();
        treeTableModel = new TreeTableModelImpl(report.getCacheReport());
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
                if(o instanceof CacheReport){
                    return new IconLoader("icons/report.png");
                }else if(o instanceof CacheInfo){
                    return new IconLoader("icons/database.png");
                }else if(o instanceof CacheNodeInfo){
                    return new IconLoader("icons/node.png");
                }
                return null;
            }
        };
    }

    private class TreeTableModelImpl extends AbstractTreeTableModel {

        private String[] headers = new String[]{"Tree", "Size", "TotalPuts", "TotalGets", "CacheHits", "Units"};

        public TreeTableModelImpl(Object root) {
            super(root);
        }

        @Override
        public int getColumnCount() {
            return headers.length;
        }

        @Override
        public String getColumnName(int column) {
            return headers[column];
        }

        @Override
        public Object getValueAt(Object o, int i) {
            if (o instanceof CacheReport) {
                CacheReport report = (CacheReport) o;
                switch (i) {
                    case 1:
                        return report.getTotalUnits();
                    default:
                        return "";
                }
            } else if (o instanceof CacheInfo) {
                CacheInfo info = (CacheInfo) o;
                switch (i) {
                    case 1:
                        return info.getSize();
                    case 2:
                        return info.getTotalPuts();
                    case 3:
                        return info.getTotalPuts();
                    case 4:
                        return info.getCacheHits();
                    case 5:
                        return FileUtils.convertToStringRepresentation(info.getUnits());
                    default:
                        return "";
                }
            }else if(o instanceof CacheNodeInfo){
                CacheNodeInfo info = (CacheNodeInfo) o;
                switch (i) {
                    case 1:
                        return info.getSize();
                    case 2:
                        return info.getTotalPuts();
                    case 3:
                        return info.getTotalGets();
                    case 4:
                        return info.getCacheHits();
                    case 5:
                        return FileUtils.convertToStringRepresentation(info.getUnits());
                    default:
                        return "";
                }
            }

            return null;
        }

        @Override
        public Object getChild(Object parent, int index) {
            if (parent instanceof CacheReport) {
                return ((CacheReport) parent).getData().get(index);
            } else if (parent instanceof CacheInfo) {
                return ((CacheInfo) parent).getNodes().get(index);
            }
            return "";
        }

        @Override
        public int getChildCount(Object parent) {
            if (parent instanceof CacheReport) {
                return ((CacheReport) parent).getData().size();
            } else if (parent instanceof CacheInfo) {
                return ((CacheInfo) parent).getNodes().size();
            }

            return 0;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            return 0;
        }

        public void fireNewRoot() {
            modelSupport.fireNewRoot();
        }
    }
}
