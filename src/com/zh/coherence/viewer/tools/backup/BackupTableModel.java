package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.File;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 06.04.12
 * Time: 22:20
 */
public class BackupTableModel implements TableModel {
    private Map<String, CacheInfo> index = new HashMap<String, CacheInfo>();
    private List<CacheInfo> cacheInfoList = new ArrayList<CacheInfo>();
    private String directory = null;
    private Icon clock = IconHelper.getInstance().getIcon(IconType.CLOCK);
    private Icon processed = IconHelper.getInstance().getIcon(IconType.TICK_WHITE);
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();

    public void updateCachesFromJMX(){
        clear();
        if(JMXManager.getInstance().isEnabled()){
            for(String name : JMXManager.getInstance().getCacheNamesList()){
                addValue(name);
            }
        }
        sendEvent(null);
    }

    public void updateCacheFromDir(String dir){
        if(dir == null){
            clear();
            directory = null;
            return;
        }
        if(dir.equals(directory)){
            return;
        }
        File file = new File(dir);
        clear();
        if(file.isDirectory()){
            for(File f : file.listFiles()){
                if(f.isFile()){
                    addValue(f.getName());
                }
            }
        }
        directory = dir;
        sendEvent(null);
    }

    public void clear(){
        cacheInfoList.clear();
        index.clear();
        sendEvent(null);
    }

    public BackupTableModel() {
        updateCachesFromJMX();
    }

    @Override
    public int getRowCount() {
        return cacheInfoList.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnIndex == 1? "name" : null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 0: return Boolean.class;
            case 1: return String.class;
            case 2: return Icon.class;
            default: return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CacheInfo cacheInfo = cacheInfoList.get(rowIndex);
        switch (columnIndex){
            case 0: return cacheInfo.enabled;
            case 1: return cacheInfo.name;
            case 2:{
                Icon icon;
                if(cacheInfo.enabled){
                    icon = cacheInfo.processed ? processed : clock;
                }else{
                    icon = null;
                }
                return icon;
            }
            default: return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            cacheInfoList.get(rowIndex).enabled = (Boolean) aValue;
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    public void addValue(String name){
        if(!index.containsKey(name)){
            CacheInfo cacheInfo = new CacheInfo(name);
            index.put(name, cacheInfo);
            cacheInfoList.add(cacheInfo);
        }
        sendEvent(null);
    }

    public void removeValue(CacheInfo cacheInfo){
        cacheInfoList.remove(cacheInfo);
        index.remove(cacheInfo.name);
        sendEvent(null);
    }

    public void removeValue(String name){
        removeValue(index.get(name));
    }

    public void changeCheck(boolean value){
        for(CacheInfo info : cacheInfoList){
            info.enabled = value;
        }
        sendEvent(null);
    }

    private void sendEvent(Integer row){
        for(TableModelListener l : listeners){
            if(row == null){
                l.tableChanged(new TableModelEvent(this));
            }else{
                l.tableChanged(new TableModelEvent(this, row));
            }
        }
    }

    public void refresh(CacheInfo info){
        int row = cacheInfoList.indexOf(info);
        if(row >= 0){
            sendEvent(row);
        }else{
            //todo log
            System.err.println("info: " + info + " not found");
            throw new IllegalArgumentException(info.name);
        }
    }

    public class CacheInfo{
        public String name;
        public boolean enabled = true;
        public boolean processed = false;

        private CacheInfo(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheInfo cacheInfo = (CacheInfo) o;

            return !(name != null ? !name.equals(cacheInfo.name) : cacheInfo.name != null);
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }

    public List<CacheInfo> getCacheInfoList() {
        return cacheInfoList;
    }
}
