package com.zh.coherence.viewer.userclassmanager;

import javax.swing.table.AbstractTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 26.05.12
 * Time: 18:50
 */
public class UserClassViewerTableModel extends AbstractTableModel {
    private Class clazz;

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
