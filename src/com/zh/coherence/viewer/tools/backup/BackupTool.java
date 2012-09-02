package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.eventlog.EventLogPane;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.backup.actions.*;
import com.zh.coherence.viewer.tools.backup.actions.filter.CacheFilterAction;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import com.zh.coherence.viewer.utils.icons.IconType;
import layout.TableLayout;
import org.jdesktop.swingx.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

public class BackupTool extends JPanel implements CoherenceViewerTool {

    private BackupContext context;
    private JXTable caches;
    private BackupTableModel backupTableModel;
    private JPanel cacheListPane = new JPanel(new BorderLayout());
    private JTextField pathFiled;
    //actions
    private ReloadCacheList reloadCacheList;

    public BackupTool(BackupContext.BackupAction action) {
        super(new TableLayout(new double[][]{
                {2, 400, 2, TableLayout.FILL, 2},
                {2, PREFERRED, 2, PREFERRED, 5, PREFERRED, 5, PREFERRED, 10, PREFERRED, 5, FILL}
                //  header        action      source/target   progress       button     event_log
        }));

        String actionTitle = action == BackupContext.BackupAction.BACKUP ? "Backup" : "Restore";
        String headerAction = action == BackupContext.BackupAction.BACKUP ? "write" : "read";
        JXHeader header = new JXHeader(actionTitle,
                "Use this tool to " + headerAction + " a serialized representation of the given caches\n"
                        + "This tool doesn't use standard coherence tool",
                new IconLoader("icons/backup.png"));
        add(header, "1,1,3,1");

        context = new BackupContext(action);
        backupTableModel = new BackupTableModel(context);
        caches = new JXTable(backupTableModel);
        caches.setSortable(false);
        JTableHeader tHeader = caches.getTableHeader();
        tHeader.setReorderingAllowed(false);
        tHeader.setResizingAllowed(false);
        TableColumn col = caches.getColumnModel().getColumn(0);
        col.setPreferredWidth(25);
        col = caches.getColumnModel().getColumn(2);
        col.setPreferredWidth(25);
        col = caches.getColumnModel().getColumn(3);
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
        reloadCacheList = new ReloadCacheList(context, backupTableModel);
        cacheListToolBar.add(reloadCacheList);
        cacheListToolBar.addSeparator();
        cacheListToolBar.add(new SaveCacheList());
        cacheListToolBar.add(new LoadCacheList());
        cacheListToolBar.addSeparator();
        cacheListToolBar.add(new CacheFilterAction(backupTableModel, caches));
        cacheListToolBar.addSeparator();
        cacheListToolBar.add(new CheckAllCachesAction(backupTableModel));
        cacheListToolBar.add(new UnCheckAllCachesAction(backupTableModel));

        add(cacheListPane, "1, 3, 1, 9");

//Threads
        JPanel threadsPanel = new JPanel(new BorderLayout());
        threadsPanel.add(new JLabel(new IconLoader("icons/processor.png")), BorderLayout.WEST);
        final JSpinner threads = new JSpinner(new SpinnerNumberModel(2, 1, Integer.MAX_VALUE, 1));
        threadsPanel.add(threads, BorderLayout.CENTER);
        threadsPanel.setBorder(BorderFactory.createTitledBorder("Threads"));

        JPanel panel1 = new JPanel(new TableLayout(new double[][]{{120, 2, 200, FILL}, {PREFERRED}}));

        panel1.add(threadsPanel, "0,0");

//Buffer size
        JPanel bufferPanel = new JPanel(new BorderLayout(2, 0));
        bufferPanel.setBorder(BorderFactory.createTitledBorder("Buffer"));
        final JSpinner buffer = new JSpinner(new SpinnerNumberModel(context.getBufferSize(), 1, Integer.MAX_VALUE, 5));
        bufferPanel.add(new JLabel(new IconLoader("icons/memory-module.png")), BorderLayout.WEST);
        bufferPanel.add(buffer, BorderLayout.CENTER);
        JComboBox<String> bufferType = new JComboBox<String>(new String[]{"Units", "Megabytes"});
        bufferPanel.add(bufferType, BorderLayout.EAST);

        panel1.add(bufferPanel, "2,0");
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBorder(BorderFactory.createTitledBorder(""));
        panel1.add(emptyPanel, "3,0");

        add(panel1, "3, 3");

        JXRadioGroup targetRadioGroup = new JXRadioGroup();
        JPanel targetPanel = new JPanel(new VerticalLayout(2));
        targetPanel.setBorder(BorderFactory.createTitledBorder("Folder"));
        targetPanel.add(targetRadioGroup);
//path
        JPanel folderPanel = new JPanel(new BorderLayout(2, 0));
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
        folderPanel.add(new JButton(new ChoosePathAction(context, pathFiled)), BorderLayout.EAST);
        folderPanel.add(pathFiled, BorderLayout.CENTER);

        targetPanel.add(folderPanel);
        add(targetPanel, "3,5");

        JPanel progressPanel = new JPanel(new TableLayout(new double[][]{
                {FILL, 2}, {PREFERRED, 2, 35, 5, PREFERRED, 2, 35}
        }));
//        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));

        progressPanel.add(new JXTitledSeparator("General", JSeparator.CENTER), "0,0");
        context.generalProgress = new JProgressBar();
        context.generalProgress.setString("Wait");
        context.generalProgress.setStringPainted(true);
        progressPanel.add(context.generalProgress, "0,2");

        progressPanel.add(new JXTitledSeparator("Cache", JSeparator.CENTER), "0,4");
        context.cacheProgress = new JProgressBar();
        context.cacheProgress.setString("Wait");
        context.cacheProgress.setStringPainted(true);
        progressPanel.add(context.cacheProgress, "0,6");

        add(progressPanel, "3, 7");

        JButton start = new JButton(new StartAction(context));
        JPanel startPanel = new JPanel(new TableLayout(new double[][]{
                {150}, {75}
        }));
        startPanel.add(start, "0,0");
        add(startPanel, "3,9");

        context.logPane = new EventLogPane(new BackupLogRenderer());
        context.logPane.setBorder(BorderFactory.createTitledBorder("Event log"));
        add(context.logPane, "1,11,3,11");
    }

    @Override
    public JComponent getPane() {
        reloadCacheList.reload();

        return this;
    }
}
