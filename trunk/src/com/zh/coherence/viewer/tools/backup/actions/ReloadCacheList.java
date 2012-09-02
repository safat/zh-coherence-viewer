package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.tools.backup.CacheInfo;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;

public class ReloadCacheList extends AbstractAction {
    private BackupContext context;
    private AbstractTableModel tableModel;

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
        if (JMXManager.getInstance().isEnabled()) {
            context.getCacheInfoList().clear();
            for (String name : JMXManager.getInstance().getCacheNamesList()) {
                context.getCacheInfoList().add(new CacheInfo(name));
            }
            tableModel.fireTableDataChanged();
        }
    }
}
