package com.zh.coherence.viewer.tableview;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.*;

public class CollectionTableModel implements TableModel {

    private Collection data;
    private int limit;
    private List<Object> list;

    public CollectionTableModel(Collection data, int limit) {
        this.data = data;
        this.limit = limit;

        initModel();
    }

    @Override
    public int getRowCount() {
        int size = data.size();
        return size < limit ? size : limit;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "Value";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return list.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }

    private void initModel(){
        if(data == null){
            throw new IllegalStateException("data didn't set up");
        }
        list = new ArrayList<Object>();
        list.addAll(data);
    }

    public Collection getData() {
        return data;
    }

    public void setData(Collection data) {
        this.data = data;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
