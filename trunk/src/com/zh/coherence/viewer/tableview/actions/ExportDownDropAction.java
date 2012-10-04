package com.zh.coherence.viewer.tableview.actions;

import com.zh.coherence.viewer.tableview.CoherenceTableView;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExportDownDropAction extends AbstractAction{
    private CoherenceTableView tableView;
    private JPopupMenu menu;

    public ExportDownDropAction(CoherenceTableView tableView) {
        this.tableView = tableView;

        menu = new JPopupMenu();
        menu.add(new ExportToExcelAction(tableView));
        menu.add(new ExportToCsvAction(tableView));

        putValue(Action.SMALL_ICON, new IconLoader("icons/save.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();
        menu.show(component, 0, 20);
    }
}
