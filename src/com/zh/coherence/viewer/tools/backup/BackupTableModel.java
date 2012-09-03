package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;

public class BackupTableModel extends AbstractTableModel {
    private String directory = null;
    private BackupContext context;

    //icons
    private Icon clock = new IconLoader("icons/clock.png");
    private Icon processed = new IconLoader("icons/tick-white.png");
    private Icon filterIcon = new IconLoader("icons/filter.png");

    public void updateCacheFromDir(String dir) {
        if (dir == null) {
//            clear();
            directory = null;
            return;
        }
        if (dir.equals(directory)) {
            return;
        }
        File file = new File(dir);
//        clear();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isFile()) {
//                    addValue(f.getName());
                }
            }
        }
        directory = dir;
//        sendEvent(null);
    }

    public BackupTableModel(BackupContext context) {
        this.context = context;
    }

    @Override
    public int getRowCount() {
        return context != null ? context.getCacheInfoList().size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnIndex == 1 ? "name" : null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return Icon.class;
            case 3:
                return Icon.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CacheInfo cacheInfo = context.getCacheInfoList().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return cacheInfo.isEnabled();
            case 1:
                return cacheInfo.getName();
            case 2: {
                Icon icon;
                if (cacheInfo.isEnabled()) {
                    icon = cacheInfo.isProcessed() ? processed : clock;
                } else {
                    icon = null;
                }
                return icon;
            }
            case 3: {
                return cacheInfo.getFilter().isEnabled() ? filterIcon : null;
            }
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            context.getCacheInfoList().get(rowIndex).setEnabled((Boolean) aValue);
        }
    }

    public BackupContext getContext() {
        return context;
    }

    public void setContext(BackupContext context) {
        this.context = context;
    }
}
