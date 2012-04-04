package com.zh.coherence.viewer.tableview.actions;

import com.zh.coherence.viewer.tableview.CoherenceTableView;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 04.04.12
 * Time: 22:50
 */
public class ExportDownDropAction extends AbstractAction{
    private CoherenceTableView tableView;
    private JPopupMenu menu;

    public ExportDownDropAction(CoherenceTableView tableView) {
        this.tableView = tableView;

        menu = new JPopupMenu();
        menu.add(new ExportToExcelAction(tableView));
        menu.add(new ExportToCsvAction(tableView));

        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.SAVE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();
        menu.show(component, 0, 20);
    }
}
