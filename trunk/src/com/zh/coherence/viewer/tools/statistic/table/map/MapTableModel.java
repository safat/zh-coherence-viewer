package com.zh.coherence.viewer.tools.statistic.table.map;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapTableModel extends AbstractTableModel{

    List<Map.Entry> data;

    private String[] header = new String[]{"Name", "Value"};

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

    @Override
    public int getRowCount() {
        if(data != null){
            return data.size();
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return data.get(rowIndex).getKey();
            case 1:
                return data.get(rowIndex).getValue();
            default:
                return "err";
        }
    }

    public void setData(Map data) {
        this.data = new ArrayList<Map.Entry>(data.entrySet());
        fireTableDataChanged();
    }
}
