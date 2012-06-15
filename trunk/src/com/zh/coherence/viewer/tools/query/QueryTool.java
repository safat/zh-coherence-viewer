package com.zh.coherence.viewer.tools.query;

import com.tangosol.coherence.dsltools.termtrees.Term;
import com.zh.coherence.viewer.eventlog.EventLogPane;
import com.zh.coherence.viewer.tableview.CoherenceTableView;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.query.actions.*;
import com.zh.coherence.viewer.utils.LRUList;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
import org.fife.ui.rsyntaxtextarea.CodeTemplateManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class QueryTool extends JXPanel implements CoherenceViewerTool {
    private RSyntaxTextArea editor;
    private CoherenceTableView tableView;
    private JPanel output;
    private CardLayout outputCardLayout = new CardLayout(2,2);
    private QueryContext context;
    private QueryStatusBar statusBar;
    private EventLogPane eventLogPane;

    private QueryEngine queryEngine;

    private LRUList<String> history = new LRUList<String>(10);

    public QueryTool(CoherenceTableView tableView, QueryEngine queryEngine) {
        super(new BorderLayout());

        this.tableView = tableView;
        this.queryEngine = queryEngine;

        context = new QueryContext(this);
        eventLogPane = new EventLogPane(new QueryEventLogRenderer());
        eventLogPane.addHyperlinkListener(new EventLogHyperlinkListener());

        final ExecuteQueryAction executeQueryAction = new ExecuteQueryAction(context, queryEngine);

        JToolBar toolBar = new JToolBar();
        toolBar.add(executeQueryAction);
        toolBar.add(new HistoryAction(this));
        toolBar.add(new InsertCacheNameAction(this));
        toolBar.addSeparator();
        toolBar.add(new SaveCohQlAction(this));
        toolBar.add(new OpenCohQlAction(this));

        add(toolBar, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        editor = new RSyntaxTextArea();
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        editor.setCodeFoldingEnabled(true);
        editor.setAntiAliasingEnabled(true);

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5 ||
                        (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown())) {
                    executeQueryAction.actionPerformed(new ActionEvent(e.getSource(), e.getID(), null));
                }
            }
        });
        RTextScrollPane editorScrollPane = new RTextScrollPane(editor);
        editorScrollPane.setFoldIndicatorEnabled(true);
        RSyntaxTextArea.setTemplatesEnabled(true);
        CodeTemplateManager ctm = RSyntaxTextArea.getCodeTemplateManager();
        CodeTemplate ct = new StaticCodeTemplate("zc", "select * from 'ch'", null); //todo
        ctm.addTemplate(ct);

        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream("config/cohQL.xml");
        try {
            if (in != null) {
                provider.loadFromXML(in);
                in.close();
            } else {
                provider.loadFromXML(new File("config/cohQL.xml"));
            }
        } catch (IOException ex) {
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

        output = new JPanel(outputCardLayout);

        output.add(createTextPanel("No data"), QueryContext.NO_DATA);
        output.add(createTextPanel("Error"), QueryContext.ERROR);
        output.add(eventLogPane, QueryContext.EVENT_LOG);
        output.add(tableView, QueryContext.TABLE_VIEW);

        splitPane.setBottomComponent(output);

        add(splitPane, BorderLayout.CENTER);

        statusBar = new QueryStatusBar(context);

        add(statusBar, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                editor.requestFocusInWindow();
            }
        });
        outputCardLayout.show(output, QueryContext.TABLE_VIEW);
    }

    public String getScript() {
        String selected = editor.getSelectedText();
        if (selected != null) {
            return selected;
        } else {
            return editor.getText();
        }
    }

    private JPanel createTextPanel(String text){
        JPanel panel = new JPanel();
        panel.add(new JXLabel(text, JXLabel.CENTER));

        return panel;
    }

    @Override
    public JComponent getPane() {
        return this;
    }

    @Override
    public String getName() {
        return "Query";
    }

    public void showResult(Object obj, Term tern, int limit) {
        tableView.setSubject(obj, tern, limit);
    }

    public LRUList<String> getHistory() {
        return history;
    }

    public void setHistory(LRUList<String> history) {
        this.history = history;
    }

    public RSyntaxTextArea getEditor() {
        return editor;
    }

    public QueryStatusBar getStatusBar() {
        return statusBar;
    }

    public void setStatusBar(QueryStatusBar statusBar) {
        this.statusBar = statusBar;
    }

    public CoherenceTableView getTableView() {
        return tableView;
    }

    public void setTableView(CoherenceTableView tableView) {
        this.tableView = tableView;
    }

    public JPanel getOutput() {
        return output;
    }

    public void setOutput(JPanel output) {
        this.output = output;
    }

    public QueryContext getContext() {
        return context;
    }

    public EventLogPane getEventLogPane() {
        return eventLogPane;
    }

    public CardLayout getOutputCardLayout() {
        return outputCardLayout;
    }
}
