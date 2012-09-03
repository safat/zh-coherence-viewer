package com.zh.coherence.viewer.tools.backup.actions.filter;

import com.zh.coherence.viewer.console.JTextAreaWriter;
import com.zh.coherence.viewer.tools.backup.BackupFilter;
import com.zh.coherence.viewer.tools.backup.BackupTableModel;
import com.zh.coherence.viewer.tools.backup.CacheInfo;
import com.zh.coherence.viewer.tools.backup.FilterExecutor;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import com.zh.coherence.viewer.utils.ui.ZHDialog;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class CacheFilterAction extends AbstractAction {
    private BackupTableModel backupTableModel;
    private JTable table;

    public CacheFilterAction(BackupTableModel backupTableModel, JTable table) {
        putValue(Action.SMALL_ICON, new IconLoader("icons/filter.png"));
        putValue(Action.SHORT_DESCRIPTION, "Edit filter for selected cache");

        this.backupTableModel = backupTableModel;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent parent = (JComponent) e.getSource();
        final CacheFilterEditor cacheFilterEditor = new CacheFilterEditor(table, backupTableModel.getContext());

        final ZHDialog dialog = new ZHDialog(cacheFilterEditor, "Filter editor", new Runnable() {
            @Override
            public void run() {
                int row = table.getSelectedRow();
                CacheInfo info = backupTableModel.getContext().getCacheInfoList().get(row);
                info.setFilter(cacheFilterEditor.getFilter());
                backupTableModel.fireTableDataChanged();
            }
        }, "OK");

        dialog.setModal(true);
        dialog.show(parent, 600, 450);
    }

}