package com.zh.coherence.viewer.tools.query;

import com.tangosol.coherence.dsltools.termtrees.Term;
import com.zh.coherence.viewer.console.JTextAreaWriter;
import com.zh.coherence.viewer.tableview.CoherenceTableView;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.query.actions.CqlScriptExecutor;
import com.zh.coherence.viewer.tools.query.actions.ExecuteQueryAction;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
import org.fife.ui.rsyntaxtextarea.CodeTemplateManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 1:18
 */
public class QueryTool extends JPanel implements CoherenceViewerTool {
    private RSyntaxTextArea editor;
    private JTextArea console;
    private CoherenceTableView tableView;
    private JTextAreaWriter textAreaWriter;
    private JTabbedPane output;

    public QueryTool() {
        super(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.add(new ExecuteQueryAction(this));
        add(toolBar, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        editor = new RSyntaxTextArea();
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        editor.setCodeFoldingEnabled(true);
        editor.setAntiAliasingEnabled(true);


        editor.addKeyListener(new KeyAdapter() {
            CqlScriptExecutor scriptExecutor = new CqlScriptExecutor(QueryTool.this);
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_F5){
                     scriptExecutor.execute();
                }
            }
        });
        RTextScrollPane editorScrollPane = new RTextScrollPane(editor);
        editorScrollPane.setFoldIndicatorEnabled(true);
        RSyntaxTextArea.setTemplatesEnabled(true);
        CodeTemplateManager ctm = RSyntaxTextArea.getCodeTemplateManager();
        CodeTemplate ct = new StaticCodeTemplate("zc", "select * from 'ch'", null);
        ctm.addTemplate(ct);

        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream("config/cohQL.xml");
        try{
            if(in != null){
                provider.loadFromXML(in);
                in.close();
            } else {
                provider.loadFromXML(new File("cohQL.xml"));
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

        LanguageAwareCompletionProvider lacProvider = new
                LanguageAwareCompletionProvider(provider);
        AutoCompletion ac = new AutoCompletion(lacProvider);
        ac.setParameterAssistanceEnabled(true);
        ac.setShowDescWindow(true);
        ac.setListCellRenderer(new CCellRenderer());
        ac.install(editor);

        editor.setToolTipSupplier(lacProvider);
        ToolTipManager.sharedInstance().registerComponent(editor);

        splitPane.setTopComponent(editorScrollPane);
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(2);

        output = new JTabbedPane(JTabbedPane.BOTTOM);

        tableView = new CoherenceTableView();
        output.add("Table", tableView);
        console = new JTextArea();
        console.setFont(new Font("Dialog", Font.PLAIN, 12));
        output.add("Console", new JScrollPane(console));

        splitPane.setBottomComponent(output);

        add(splitPane, BorderLayout.CENTER);
        //todo add query status? BorderLayout.SOUTH

        textAreaWriter = new JTextAreaWriter(console);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                editor.requestFocusInWindow();
            }
        });
    }

    public String getScript(){
        String selected = editor.getSelectedText();
        if(selected != null){
            return selected;
        }else{
            return editor.getText();
        }
    }

    public void traceText(Object text){
        if(text == null){
            throw new IllegalArgumentException("text");
        }
        try {
            getConsolePrintWriter()
                    .append(DateFormat.getTimeInstance().format(new Date()))
                    .append(" ")
                    .append(text.toString())
                    .append("\n")
                    .flush();
            output.setSelectedIndex(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Writer getConsolePrintWriter(){
        return textAreaWriter;
    }

    @Override
    public JComponent getPane() {
        return this;
    }

    @Override
    public String getName() {
        return "Query";
    }
    
    public void showResult(Object obj, Term tern, int limit){
        tableView.setSubject(obj, tern, limit);
    }
}
