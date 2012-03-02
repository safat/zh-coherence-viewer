package com.zh.coherence.viewer.tableview;

import com.tangosol.coherence.dsltools.termtrees.Term;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 15.02.12
 * Time: 22:54
 */
public class OneLineTableModel implements TableModel {
    private Object data;
    private Term term;

    private String header;

    public OneLineTableModel(Object data, Term term) {
        this.data = data;
        this.term = term;

        initModel();
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return header;
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
        return data;
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

    public void initModel(){
        Term fieldsTerms = term.findChild("fieldList");
        header = fieldsTerms.termAt(1).termAt(1).fullFormString();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
