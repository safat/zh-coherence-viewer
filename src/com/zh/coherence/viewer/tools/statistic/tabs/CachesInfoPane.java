package com.zh.coherence.viewer.tools.statistic.tabs;

import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class CachesInfoPane extends JXPanel{
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
        treeTableModel = new TreeTableModelImpl(); //todo
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

        private String[] headers = new String[]{"Tree", "Size", "TotalPuts", "TotalGets", "AveragePutMillis",
                "AverageGetMillis"};

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
            return null;
        }

        @Override
        public Object getChild(Object parent, int index) {
            return null;
        }

        @Override
        public int getChildCount(Object parent) {
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
