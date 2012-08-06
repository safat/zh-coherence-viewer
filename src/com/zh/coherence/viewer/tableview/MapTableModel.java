package com.zh.coherence.viewer.tableview;

import com.tangosol.coherence.dsltools.termtrees.Term;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 14.02.12
 * Time: 23:32
 */
public class MapTableModel implements TableModel {
    private Map subject;
    private List keys;
    private Term params;
    private String[] headers;
    private int limit;

    public MapTableModel(Map subject, Term params, int limit) {
        this.subject = subject;
        this.params = params;
        this.limit = limit;
        initModel();
    }

    @Override
    public int getRowCount() {
        int size = subject.size();
        return size < limit ? size : limit;
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object tmp = subject.get(keys.get(rowIndex));
        Object ret;
        if (tmp instanceof List && ((List) tmp).size() == getColumnCount()) {
            ret = ((List) tmp).get(columnIndex);
        } else {
            ret = tmp;
        }

        return ret;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }

    private void initModel() {
        keys = new ArrayList();
        keys.addAll(subject.keySet());
        Term fieldsTerms = params.findChild("fieldList");
        headers = new String[fieldsTerms.length()];
        Term callNode;
        Term identifier;
        for (int i = 1, size = fieldsTerms.length(); i <= size; i++) {
            try {
                callNode = fieldsTerms.termAt(i);
                if (callNode.getFunctor().equals("callNode")) {
                    headers[i - 1] = callNode.termAt(1).fullFormString();
                } else {
                    callNode = callNode.findAttribute("callNode");
                    identifier = fieldsTerms.termAt(i).findAttribute("identifier");
                    headers[i - 1] = callNode.fullFormString()
                            + (identifier != null ? "." + identifier.fullFormString() : "");
                }
            } catch (Exception ex) {
                headers[i - 1] = "Unknown";
            }
        }
    }
}
