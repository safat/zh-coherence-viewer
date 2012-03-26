package com.zh.coherence.viewer.tableview;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 15.02.12
 * Time: 20:05
 */
public class SetTableModel implements TableModel{
    private final String header[] = new String[]{"Key", "Value"};

    private Set data;
    private int limit;
    private List<Map.Entry> list;

    public SetTableModel(Set data, int limit) {
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
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return header[columnIndex];
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
        if(columnIndex == 0){
            return list.get(rowIndex).getKey();

        }else{
            return list.get(rowIndex).getValue();
        }
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
        list = new ArrayList<Map.Entry>();
        Iterator iterator = data.iterator();
        for(int i = 0, size = getRowCount(); i < size && iterator.hasNext(); i++){
            list.add((Map.Entry) iterator.next());
        }
    }

    public Set getData() {
        return data;
    }

    public void setData(Set data) {
        this.data = data;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
