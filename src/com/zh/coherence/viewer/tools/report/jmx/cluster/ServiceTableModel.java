package com.zh.coherence.viewer.tools.report.jmx.cluster;

import com.zh.coherence.viewer.tools.report.jmx.cluster.transfer.ServiceInfo;
import com.zh.coherence.viewer.utils.FileUtils;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ServiceTableModel extends AbstractTableModel {
    private ClusterReportDataProvider dataProvider;

    List<ServiceInfo> info = null;

    private String[] headers = new String[]{
            "Service name", "Total nodes", "Storage nodes", "Caches", "Objects",
            "Units", "Senior", "Status", "Type"
    };

    public ServiceTableModel(ClusterReportDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public int getRowCount() {
        return info != null ? info.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return info.get(rowIndex).name;
            case 1:
                return info.get(rowIndex).totalNodes;
            case 2:
                return info.get(rowIndex).storageNodes;
            case 3:
                return info.get(rowIndex).caches;
            case 4:
                return info.get(rowIndex).objects;
            case 5:
                if(info.get(rowIndex).units != info.get(rowIndex).objects){
                    return FileUtils.convertToStringRepresentation(info.get(rowIndex).units);
                }else{
                    return "";
                }
            case 6:
                return info.get(rowIndex).senior;
            case 7:
                return info.get(rowIndex).status;
            case 8:
                return info.get(rowIndex).type;
        }

        return null;
    }

    @Override
    public void fireTableDataChanged() {
        info = dataProvider.collectServiceData();
        super.fireTableDataChanged();
    }
}
