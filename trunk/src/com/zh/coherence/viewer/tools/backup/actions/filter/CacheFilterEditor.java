package com.zh.coherence.viewer.tools.backup.actions.filter;

import com.zh.coherence.viewer.console.JTextAreaWriter;
import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.tools.backup.BackupFilter;
import com.zh.coherence.viewer.tools.backup.FilterExecutor;
import com.zh.coherence.viewer.utils.icons.IconLoader;
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

public class CacheFilterEditor extends JPanel {
    private JTable table;
    private BackupFilter filter;
    private JRadioButton queryRadio, shellRadio;
    private RSyntaxTextArea editor;
    private JCheckBox enable;

    public CacheFilterEditor(JTable table, BackupContext context) {
        super(new BorderLayout());
        this.table = table;

        int row = table.getSelectedRow();
        if(row >= 0){
            filter = context.getCacheInfoList().get(row).getFilter();
        }

        initUI();
    }

    private void initUI() {
//north panel
        JToolBar north = new JToolBar(JToolBar.HORIZONTAL);
        north.setFloatable(false);
        //enable check box
        enable = new JCheckBox("Enable filter");
        north.setBorder(BorderFactory.createEtchedBorder());
        north.add(enable);
        north.addSeparator();

        ButtonGroup buttonGroup = new ButtonGroup();
        //type query
        queryRadio = new JRadioButton("Query");
        buttonGroup.add(queryRadio);

        north.add(queryRadio);
        //type bean shell
        shellRadio = new JRadioButton("Bean Shell");
        buttonGroup.add(shellRadio);

        north.add(shellRadio);
        //compile
        north.addSeparator();
        JButton execute = new JButton(new IconLoader("icons/start-icon.png"));
        north.add(execute);

//editor
        editor = new RSyntaxTextArea();
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
        add(north, BorderLayout.NORTH);
        add(editorScrollPane, BorderLayout.CENTER);
        add(consolePane, BorderLayout.SOUTH);

//actions
        queryRadio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (queryRadio.isSelected()) {
                    editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
                    console.setText("The Query it's string-oriented way to filter a result set without having to remember details of the Coherence API");
                }
            }
        });
        shellRadio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (shellRadio.isSelected()) {
                    editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                    if (editor.getText().isEmpty()) {
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
                console.setText("");
                executor.checkScript(cache, getFilter(), consoleWriter);
            }
        });

        if(filter != null){
            if(filter.getFilterType() == BackupFilter.FilterType.QUERY){
                queryRadio.setSelected(true);
            }else{
                shellRadio.setSelected(true);
            }
            editor.setText(filter.getSource());
            enable.setSelected(filter.isEnabled());
        }else{
            queryRadio.setSelected(true);
        }
    }

    public BackupFilter getFilter(){
        BackupFilter bf = new BackupFilter();
        bf.setEnabled(enable.isSelected());
        bf.setFilterType(queryRadio.isSelected() ? BackupFilter.FilterType.QUERY : BackupFilter.FilterType.SHELL);
        bf.setSource(editor.getText());

        return bf;
    }
}
