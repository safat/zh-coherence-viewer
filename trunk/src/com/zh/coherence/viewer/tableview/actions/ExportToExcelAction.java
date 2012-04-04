package com.zh.coherence.viewer.tableview.actions;

import com.zh.coherence.viewer.tableview.CoherenceTableView;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 04.04.12
 * Time: 23:04
 */
public class ExportToExcelAction extends AbstractAction {
    private CoherenceTableView tableView;

    public ExportToExcelAction(CoherenceTableView tableView) {
        this.tableView = tableView;

        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.EXCEL_28));
        putValue(Action.NAME, "Export to Excel");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "xls (Excel 2000)";
            }
        });
        int result = fileChooser.showSaveDialog(SwingUtilities.getWindowAncestor(tableView));
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String name = file.getAbsolutePath();
            if(!name.endsWith(".xls")){
                name = name + ".xls";
                file = new File(name);
            }
            save(tableView.getTable(), file);
        }
    }

    public void save(JTable table, File file) {
        try {
            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("Select result", 0);
            TableModel model = table.getModel();

            WritableCellFormat headFormat = new WritableCellFormat();
            headFormat.setShrinkToFit(true);
            headFormat.setBackground(Colour.GREY_25_PERCENT);
            headFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            for (int i = 0; i < model.getColumnCount(); i++) {
                Label column = new Label(i, 0, model.getColumnName(i));

                column.setCellFormat(headFormat);
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    Label row = new Label(j, i + 1,
                            model.getValueAt(i, j).toString());
                    sheet1.addCell(row);
                }
            }
            for(int x=0;x<sheet1.getColumns();x++)
            {
                CellView cell= sheet1.getColumnView(x);
                cell.setAutosize(true);
                sheet1.setColumnView(x, cell);
            }
            workbook1.write();
            workbook1.close();
        } catch (Exception ex) {
            JXErrorPane.showFrame(null, new ErrorInfo("Error", ex.getMessage(), null, null, ex, Level.SEVERE, null));
        }
    }
}
