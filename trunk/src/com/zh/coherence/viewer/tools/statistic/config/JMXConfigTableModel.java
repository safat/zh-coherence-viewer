package com.zh.coherence.viewer.tools.statistic.config;

import javax.swing.table.AbstractTableModel;

public class JMXConfigTableModel extends AbstractTableModel {

    private String[] tableHeader = new String[]{"ID", "Title", "Group Index", "Group Icon"};
    private String[] listHeader = new String[]{"ID", "Title", "Enable chart"};

    private TabContainer tabContainer;

    @Override
    public int getRowCount() {
        return (tabContainer != null && tabContainer.getItems() != null) ? tabContainer.getItems().size() : 0;
    }

    @Override
    public int getColumnCount() {
        if (tabContainer != null) {
            return tabContainer.getType() == TabType.TABLE ? 4 : 3;
        } else {
            return 0;
        }
    }

    @Override
    public String getColumnName(int column) {
        if (tabContainer.getType() == TabType.TABLE) {
            return tableHeader[column];
        } else {
            return listHeader[column];
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (tabContainer.getType() == TabType.TABLE) {
            switch (columnIndex) {
                case 0:
                    return tabContainer.getItems().get(rowIndex).getId();
                case 1:
                    return tabContainer.getItems().get(rowIndex).getTitle();
                case 2:
                    return tabContainer.getItems().get(rowIndex).getGroupIndex();
                case 3:
                    return tabContainer.getItems().get(rowIndex).getIcon();
            }
        } else {
            switch (columnIndex) {
                case 0:
                    return tabContainer.getItems().get(rowIndex).getId();
                case 1:
                    return tabContainer.getItems().get(rowIndex).getTitle();
                case 2:
                    return tabContainer.getItems().get(rowIndex).getEnableChart();
            }
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (tabContainer.getType() == TabType.LIST && columnIndex == 2) {
            return Boolean.class;
        } else {
            return String.class;
        }
    }

    public TabContainer getTabContainer() {
        return tabContainer;
    }

    public void setTabContainer(TabContainer tabContainer) {
        this.tabContainer = tabContainer;
        this.fireTableDataChanged();
        System.err.println("changed");
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //todo !
    }
}
