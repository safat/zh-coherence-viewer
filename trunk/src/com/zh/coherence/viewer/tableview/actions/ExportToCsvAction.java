package com.zh.coherence.viewer.tableview.actions;

import com.csvreader.CsvWriter;
import com.zh.coherence.viewer.tableview.CoherenceTableView;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 04.04.12
 * Time: 23:10
 */
public class ExportToCsvAction extends AbstractAction {
    private CoherenceTableView tableView;

    public ExportToCsvAction(CoherenceTableView tableView) {
        this.tableView = tableView;

        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.CSV_28));
        putValue(Action.NAME, "Export to CSV");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "csv (Comma-Separated Values)";
            }
        });
        int result = fileChooser.showSaveDialog(SwingUtilities.getWindowAncestor(tableView));
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String name = file.getAbsolutePath();
            if (!name.endsWith(".csv")) {
                name = name + ".csv";
                file = new File(name);
            }
            save(tableView.getTable(), file);
        }
    }

    public void save(JTable table, File file) {
        TableModel model = table.getModel();

        try {
            CsvWriter csvOutput = new CsvWriter(new FileWriter(file, true), ',');
            for (int i = 0; i < model.getColumnCount(); i++) {
                csvOutput.write(model.getColumnName(i));
            }
            csvOutput.endRecord();

            int j;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    csvOutput.write(String.valueOf(model.getValueAt(i, j)));
                }
                csvOutput.endRecord();
            }
            csvOutput.close();
        } catch (Exception ex) {
            JXErrorPane.showFrame(null, new ErrorInfo("Error", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }
}
