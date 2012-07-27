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
        if (tabContainer != null) {
            if (tabContainer.getType() == TabType.TABLE) {
                return tableHeader[column];
            } else {
                return listHeader[column];
            }
        } else {
            return "?";
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

    public void createItem(String id) {
        TabItem item = new TabItem();
        item.setId(id);
        item.setTitle(id);
        tabContainer.getItems().add(item);
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (tabContainer.getType() == TabType.TABLE && columnIndex == 2) {
            return Integer.class;
        } else if (tabContainer.getType() == TabType.LIST && columnIndex == 2) {
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
        this.fireTableStructureChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        TabItem item = tabContainer.getItems().get(rowIndex);
        if (tabContainer.getType() == TabType.TABLE) {
            switch (columnIndex) {
                case 1:
                    item.setTitle(String.valueOf(aValue));
                    break;
                case 2:
                    item.setGroupIndex((Integer) aValue);
                    break;
                case 3:
                    item.setIcon(String.valueOf(aValue));
                    break;
            }
        } else {
            switch (columnIndex) {
                case 1:
                    item.setTitle(String.valueOf(aValue));
                    break;
                case 2:
                    item.setEnableChart((Boolean) aValue);
            }
        }
    }
}
