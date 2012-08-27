package com.zh.coherence.viewer.tools.report.jmx.cluster;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClusterTableModel extends AbstractTableModel {
    private ClusterReportDataProvider dataProvider;
    private List<String> keys;
    private Map<String, Object> data;

    public ClusterTableModel(ClusterReportDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "Key" : "Value";
    }

    @Override
    public int getRowCount() {
        return keys != null ? keys.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            return keys.get(rowIndex);
        }else{
            return data.get(keys.get(rowIndex));
        }
    }



    @Override
    public void fireTableDataChanged() {
        data = dataProvider.getClusterAttributes();
        keys = new ArrayList<String>(data.keySet());
        super.fireTableDataChanged();
    }
}
