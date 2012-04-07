package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.backup.actions.*;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import layout.TableLayout;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXRadioGroup;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.03.12
 * Time: 20:24
 */
public class BackupTool extends JPanel implements CoherenceViewerTool {

    private JRadioButton backupActionRadio, restoreActionRadio;
    private JRadioButton folderRadio, zipRadio;
    private BackupContext context = new BackupContext();
    private JXTable caches;
    private JPanel cacheListPane = new JPanel(new BorderLayout());
    private JTextField pathFiled;

    public BackupTool() {
        super(new TableLayout(new double[][]{
                {2, 300, 2, TableLayout.FILL, 2},
                {2, PREFERRED, 2, PREFERRED, 5, PREFERRED, 5, PREFERRED, 10, PREFERRED}
                //  header        action      source/target   progress       button
        }));

        JXHeader header = new JXHeader("Backup / Restore",
                "Use this tool to write/read a serialized representation of the given caches\n" +
                        "This tool doesn't use standard coherence tool",
                IconHelper.getInstance().getIcon(IconType.BACKUP));
        add(header, "1,1,3,1");

        caches = new JXTable(context.getBackupTableModel());
        JTableHeader tHeader = caches.getTableHeader();
        tHeader.setReorderingAllowed(false);
        tHeader.setResizingAllowed(false);
        TableColumn col = caches.getColumnModel().getColumn(0);
        col.setPreferredWidth(25);
        col = caches.getColumnModel().getColumn(2);
        col.setPreferredWidth(25);
        col = caches.getColumnModel().getColumn(1);
        int total = caches.getColumnModel().getTotalColumnWidth();
        col.setPreferredWidth(total - 10);

        //cache tool bar
        cacheListPane.add(new JScrollPane(caches), BorderLayout.CENTER);
        cacheListPane.setBorder(BorderFactory.createTitledBorder("List of caches"));
        JToolBar cacheListToolBar = new JToolBar();
        cacheListPane.add(cacheListToolBar, BorderLayout.NORTH);
        cacheListToolBar.add(new AddStringToListAction(context));
        cacheListToolBar.add(new RemoveElementsFromListAction(context, caches));
        cacheListToolBar.addSeparator();
        cacheListToolBar.add(new CheckAllCachesAction(context.getBackupTableModel()));
        cacheListToolBar.add(new UnCheckAllCachesAction(context.getBackupTableModel()));

        add(cacheListPane, "1, 3, 1, 9");

        backupActionRadio = new JRadioButton(new BackupAction(context, BackupContext.BackupAction.BACKUP, "Backup"));
        restoreActionRadio = new JRadioButton(new BackupAction(context, BackupContext.BackupAction.RESTORE, "Restore"));

        JXRadioGroup actionRadioGroup = new JXRadioGroup();
        actionRadioGroup.setValues(new JRadioButton[]{backupActionRadio, restoreActionRadio});
        actionRadioGroup.setSelectedValue(backupActionRadio);
        actionRadioGroup.setBorder(BorderFactory.createTitledBorder("Action"));
        add(actionRadioGroup, "3, 3");

        folderRadio = new JRadioButton(new ChangeTargetAction(context, BackupContext.Target.FOLDER, "Folder"));
        zipRadio = new JRadioButton(new ChangeTargetAction(context, BackupContext.Target.FOLDER, "ZIP"));
        zipRadio.setEnabled(false);

        JXRadioGroup targetRadioGroup = new JXRadioGroup();
        targetRadioGroup.setValues(new JRadioButton[]{folderRadio, zipRadio});
        targetRadioGroup.setSelectedValue(folderRadio);
        JPanel targetPanel = new JPanel(new VerticalLayout(2));
        targetPanel.setBorder(BorderFactory.createTitledBorder("Source/Target"));
        targetPanel.add(targetRadioGroup);
        //path
        JPanel targetPath = new JPanel(new TableLayout(new double[][]{
                {PREFERRED,2,PREFERRED,2,FILL},
                {PREFERRED}
        }));
        pathFiled = new JTextField();

        pathFiled.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                context.setPath(pathFiled.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                context.setPath(pathFiled.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                context.setPath(pathFiled.getText());
            }
        });
        targetPath.add(new JLabel("Path:"), "0,0");
        targetPath.add(new JButton(new ChoosePathAction(context, pathFiled)), "2,0");
        targetPath.add(pathFiled, "4,0");

        targetPanel.add(targetPath);
        add(targetPanel, "3,5");

        JPanel progressPanel = new JPanel(new TableLayout(new double[][]{
                {PREFERRED, 2, FILL},{PREFERRED, 2, PREFERRED}
        }));
        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));

        progressPanel.add(new JLabel("General:"), "0,0");
        context.generalProgress = new JProgressBar();
        context.generalProgress.setString("Wait");
        context.generalProgress.setStringPainted(true);
        progressPanel.add(context.generalProgress, "2,0");

        progressPanel.add(new JLabel("Cache:"), "0,2");
        context.cacheProgress = new JProgressBar();
        context.cacheProgress.setString("Wait");
        context.cacheProgress.setStringPainted(true);
        progressPanel.add(context.cacheProgress, "2,2");

        add(progressPanel, "3, 7");

        JButton start = new JButton(new StartAction(context));
        JPanel startPanel = new JPanel(new TableLayout(new double[][]{
                {150},{75}
        }));
        startPanel.add(start, "0,0");
        add(startPanel, "3,9");
    }

    @Override
    public JComponent getPane() {
        return this;
    }

    public String getName(){
        return "Backup/Restore";
    }
}
