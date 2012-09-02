package com.zh.coherence.viewer.tools.backup.actions.filter;

import com.zh.coherence.viewer.console.JTextAreaWriter;
import com.zh.coherence.viewer.tools.backup.BackupTableModel;
import com.zh.coherence.viewer.tools.backup.FilterExecutor;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import com.zh.coherence.viewer.utils.ui.ZHDialog;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class CacheFilterAction extends AbstractAction {
    private BackupTableModel backupTableModel;
    private JTable table;

    public CacheFilterAction(BackupTableModel backupTableModel, JTable table) {
        putValue(Action.SMALL_ICON, new IconLoader("icons/filter.png"));
        putValue(Action.SHORT_DESCRIPTION, "Edit filter for selected cache");

        this.backupTableModel = backupTableModel;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent parent = (JComponent) e.getSource();
        JPanel content = new JPanel(new BorderLayout());
//north panel
        JToolBar north = new JToolBar(JToolBar.HORIZONTAL);
        north.setFloatable(false);
        //enable check box
        final JCheckBox enable = new JCheckBox("Enable filter");
        north.setBorder(BorderFactory.createEtchedBorder());
        north.add(enable);
        north.addSeparator();

        ButtonGroup buttonGroup = new ButtonGroup();
        //type query
        final JRadioButton queryRadio = new JRadioButton("Query");
        buttonGroup.add(queryRadio);

        north.add(queryRadio);
        //type bean shell
        final JRadioButton shellRadio = new JRadioButton("Bean Shell");
        buttonGroup.add(shellRadio);

        north.add(shellRadio);
        //compile
        north.addSeparator();
        JButton execute = new JButton(new IconLoader("icons/start-icon.png"));
        north.add(execute);

//editor
        final RSyntaxTextArea editor = new RSyntaxTextArea();
        editor.setAntiAliasingEnabled(true);
        RTextScrollPane editorScrollPane = new RTextScrollPane(editor);
        editorScrollPane.setFoldIndicatorEnabled(true);

//console
        final JTextArea console = new JTextArea();
        console.setRows(7);
        console.setFont(new Font("Dialog", Font.PLAIN, 12));
        console.setWrapStyleWord(true);
        console.setLineWrap(true);
        JPanel consolePane = new JPanel(new BorderLayout());
        consolePane.add(new JScrollPane(console), BorderLayout.CENTER);
        consolePane.setBorder(BorderFactory.createTitledBorder("Console"));
        //writer
        final PrintWriter consoleWriter = new PrintWriter(new JTextAreaWriter(console));


//assemble
        content.add(north, BorderLayout.NORTH);
        content.add(editorScrollPane, BorderLayout.CENTER);
        content.add(consolePane, BorderLayout.SOUTH);

//actions
        queryRadio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(queryRadio.isSelected()){
                    editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
                    console.setText("The Query it's string-oriented way to filter a result set without having to remember details of the Coherence API");
                }
            }
        });
        shellRadio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(shellRadio.isSelected()){
                    editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                    if(editor.getText().isEmpty()){
                        editor.setText("import com.tangosol.util.filter.*;\nimport com.tangosol.util.*;\n" +
                                "import com.tangosol.util.extractor.*;\nimport com.tangosol.io.pof.reflect.SimplePofPath;" +
                                "\n\nFilter filter = /*Your code*/\n\nreturn filter;");
                    }
                    console.setText("The BeanShell it's a java interpreter and simple way to create complex coherence filters");
                }
            }
        });
        execute.addActionListener(new ActionListener() {
            FilterExecutor executor = new FilterExecutor();

            @Override
            public void actionPerformed(ActionEvent e) {
                String cache = String.valueOf(table.getValueAt(table.getSelectedRow(), 1));
                String script = editor.getText();
                FilterExecutor.Executor exec = queryRadio.isSelected() ? FilterExecutor.Executor.QUERY : FilterExecutor.Executor.SHELL;
                console.setText("");
                executor.checkScript(cache, script, exec, consoleWriter);
            }
        });

        queryRadio.setSelected(true);

        final ZHDialog dialog = new ZHDialog(content, "Filter editor", new Runnable() {
            @Override
            public void run() {
                int row = table.getSelectedRow();
                BackupTableModel.CacheInfo info = backupTableModel.getCacheInfoList().get(row);
                info.enableFilter = enable.isSelected();
                backupTableModel.sendEvent(row);
            }
        }, "OK");

        dialog.setModal(true);
        dialog.show(parent, 600, 450);
    }

}