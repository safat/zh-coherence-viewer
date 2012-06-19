package com.zh.coherence.viewer.menubar.action;

import com.zh.coherence.viewer.pof.printer.Printer;
import com.zh.coherence.viewer.pof.printer.ValueContainerPrinterFactory;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import com.zh.coherence.viewer.utils.icons.IconType;
import com.zh.coherence.viewer.utils.ui.ZHDialog;
import layout.TableLayout;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 22.03.12
 * Time: 0:37
 */
public class ShowSettingsAction extends AbstractAction {
    private Printer lastEdited;

    public ShowSettingsAction() {

        putValue(Action.NAME, "Settings");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.SETTINGS));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel configPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        JPanel toStringConfig = new JPanel(new TableLayout(new double[][]{
                {2, TableLayout.FILL, 2},
                {2, TableLayout.PREFERRED, 180, TableLayout.FILL, 2}
        }));
        tabbedPane.addTab("To String", new IconLoader("icons/write.png"), toStringConfig);

        final JTextArea text = new JTextArea();
        ValueContainerPrinterFactory factory = ValueContainerPrinterFactory.getInstance();
        MyTableModel tableModel = new MyTableModel();
        tableModel.refresh();
        final JXTable table = new JXTable(tableModel);
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.add(new SaveAction(text));
        toolBar.add(new AddPrinterAction(tableModel));
        toStringConfig.add(toolBar, "1,1");

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (lastEdited != null) {
                    lastEdited.setFormat(text.getText());
                }
                List<Printer> printers = ValueContainerPrinterFactory.getInstance().getPrinters();
                int row = table.getSelectedRow();
                if (row >= 0) {
                    lastEdited = printers.get(row);
                    text.setText(lastEdited.getFormat());
                }
            }
        });

        toStringConfig.add(new JScrollPane(table), "1,2");

        toStringConfig.add(new JScrollPane(text), "1,3");

        configPanel.add(tabbedPane, BorderLayout.CENTER);

        ZHDialog dialog = new ZHDialog(configPanel, "Config");

        dialog.setModal(true);
        dialog.show(600, 450);
    }

    private class AddPrinterAction extends AbstractAction {
        MyTableModel tableModel;

        private AddPrinterAction(MyTableModel tableModel) {
            this.tableModel = tableModel;
            putValue(Action.SMALL_ICON, new IconLoader("icons/plus.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String id = JOptionPane.showInputDialog("input POF ID, please");
            Integer pofId = Integer.parseInt(id);
            Printer printer = new Printer();
            printer.setPofId(pofId);
            ValueContainerPrinterFactory.getInstance().registerPrinter(printer);
            tableModel.refresh();
            tableModel.fireTableDataChanged();
        }
    }

    private class SaveAction extends AbstractAction {
        JTextArea text;

        private SaveAction(JTextArea text) {
            this.text = text;
            putValue(Action.SMALL_ICON, new IconLoader("icons/save.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (lastEdited != null) {
                lastEdited.setFormat(text.getText());
                ValueContainerPrinterFactory.getInstance().save();
            }
        }
    }

    private class MyTableModel extends AbstractTableModel {

        List<Printer> printers;

        private void refresh() {
            printers = ValueContainerPrinterFactory.getInstance().getPrinters();
        }

        @Override
        public int getRowCount() {
            return printers.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return printers.get(rowIndex).getPofId();
            } else {
                return printers.get(rowIndex).getFormat();
            }
        }

        @Override
        public String getColumnName(int column) {
            return column == 0 ? "POF ID" : "Format";
        }
    }
}
