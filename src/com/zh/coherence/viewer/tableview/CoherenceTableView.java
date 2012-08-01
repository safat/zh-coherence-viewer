package com.zh.coherence.viewer.tableview;

import com.tangosol.coherence.dsltools.termtrees.Term;
import com.zh.coherence.viewer.tableview.actions.ExportDownDropAction;
import com.zh.coherence.viewer.tableview.actions.TableHighlighterAction;
import com.zh.coherence.viewer.tableview.user.UserObjectViewer;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.search.TableSearchable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 13.02.12
 * Time: 23:03
 */
public class CoherenceTableView extends JPanel {
    private JXTable table;
    private TableModel tableModel;
    private RightButtonMenuBuilder rightButtonMenuBuilder;
    private JXSearchPanelForJXTable searchPanel;
    private List<UserObjectViewer> viewers;

    private Object subject;

    public CoherenceTableView(final List<UserObjectViewer> viewers, RightButtonMenuBuilder viewerMenuBuilder) {
        super(new BorderLayout());

        this.viewers = viewers;
        this.rightButtonMenuBuilder = viewerMenuBuilder;
        table = new JXTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setColumnControlVisible(true);
        table.setRolloverEnabled(true);
        table.setColumnSelectionAllowed(true);

        add(new JScrollPane(table), BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Point p = e.getPoint();
                    int rowNumber = table.rowAtPoint(p);
                    ListSelectionModel model = table.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);

                    Object value = tableModel.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
                    JPopupMenu menu = rightButtonMenuBuilder.buildMenu(value, viewers);
                    if (menu.getSubElements().length > 0) {
                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
        table.setSearchable(new TableSearchable(table));
        JToolBar toolBar = new JToolBar();
        searchPanel = new JXSearchPanelForJXTable(table);

        toolBar.add(searchPanel);
        toolBar.addSeparator();

        JButton export = new JXButton(new ExportDownDropAction(this));
        toolBar.add(export);

        JToggleButton highlightTable = new JToggleButton();
        highlightTable.setAction(new TableHighlighterAction(highlightTable, table));
        highlightTable.getModel().setSelected(true);
        highlightTable.getAction().actionPerformed(null);

        toolBar.add(highlightTable);
        add(toolBar, BorderLayout.NORTH);
    }

    @SuppressWarnings("unused")
    public Object getSubject() {
        return subject;
    }

    public void setSubject(Object subject, Object params, int limit) {
        this.subject = subject;
        //choose and setup new TableModel
        if (subject instanceof Map && params instanceof Term) {
            tableModel = new MapTableModel((Map) subject, (Term) params, limit);
        } else if (subject instanceof Set) {
            tableModel = new SetTableModel((Set) subject, limit);
        } else if ((subject instanceof Number || subject instanceof String) && params instanceof Term) {
            tableModel = new OneLineTableModel(subject, (Term) params);
        } else if(subject instanceof Collection){
            tableModel = new CollectionTableModel((Collection) subject, limit);
        }

        if (tableModel != null) {
            table.setModel(tableModel);
            searchPanel.refreshFieldsList();
        }
        TableColumnModel cm = table.getTableHeader().getColumnModel();
        int width = this.getWidth();
        for (int i = 0, size = cm.getColumnCount(); i < size; i++) {
            cm.getColumn(i).setPreferredWidth((width / size) - 10);
        }
    }

    public JXTable getTable() {
        return table;
    }

    public void setTable(JXTable table) {
        this.table = table;
    }
}
