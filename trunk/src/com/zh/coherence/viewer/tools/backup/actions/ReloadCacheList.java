package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.tools.backup.CacheInfo;
import com.zh.coherence.viewer.tools.backup.CacheWrapper;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;

public class ReloadCacheList extends AbstractAction {
    private BackupContext context;
    private AbstractTableModel tableModel;
    private String oldDir = null;

    public ReloadCacheList(BackupContext context, AbstractTableModel tableModel) {
        this.context = context;
        this.tableModel = tableModel;

        putValue(Action.SMALL_ICON, new IconLoader("icons/refresh_gray.png"));
        putValue(Action.SHORT_DESCRIPTION, "Reload cache list from JMX");
        setEnabled(JMXManager.getInstance().isEnabled());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        reload();
    }

    public void reload() {
        if (context.getAction() == BackupContext.BackupAction.BACKUP) {
            if (JMXManager.getInstance().isEnabled()) {
                context.getCacheInfoList().clear();
                for (String name : JMXManager.getInstance().getCacheNamesList()) {
                    context.getCacheInfoList().add(new CacheInfo(name));
                }
                tableModel.fireTableDataChanged();
            }
        } else {
            String path = context.getPath();
            if (path == null || path.isEmpty()) {
                context.getCacheInfoList().clear();
                oldDir = null;
                tableModel.fireTableDataChanged();
                return;
            }
            if (path.equals(oldDir)) {
                return;
            }
            File file = new File(path);
            context.getCacheInfoList().clear();

            if (file.isDirectory()) {
                FileFilter filter = new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(CacheWrapper.FILE_EXT);
                    }
                };
                for (File f : file.listFiles(filter)) {
                    if (f.isFile()) {
                        context.getCacheInfoList().add(new CacheInfo(f.getName().replace(CacheWrapper.FILE_EXT, "")));
                    }
                }
            }
            oldDir = path;
            tableModel.fireTableDataChanged();
        }
    }
}
