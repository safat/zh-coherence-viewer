package com.zh.coherence.viewer.tools.query.config.template;

import com.zh.coherence.viewer.config.AbstractConfigPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.search.ListSearchable;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueryTemplateConfigPanel extends AbstractConfigPanel {

    private JPanel config;
    private TemplateListModel listModel;

    @Override
    public void applyChanges() {
        RSyntaxTextArea.getCodeTemplateManager().saveTemplates();
    }

    @Override
    public void cancelChanges() {

    }

    @Override
    public void showHelp() {

    }

    @Override
    public boolean isHelpAvailable() {
        return false;
    }

    @Override
    public JComponent getConfigPanel() {
        if(config == null){
            config = new JPanel();
            initUI();
        }

        return config;
    }

    @Override
    public boolean leaveThePage() {
        return true;
    }

    private void initUI(){
        config.setLayout(new BorderLayout());
// CENTER (Templates List)
        listModel = new TemplateListModel();
        final JXList list = new JXList(listModel);
        config.add(new JScrollPane(list), BorderLayout.CENTER);

// SOUTH (Editor)
        JPanel editor = new JPanel(new VerticalLayout());
        final JTextField name = new JTextField();
        name.setBorder(BorderFactory.createTitledBorder("Name"));
        name.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update(){
                TemplateListModel.CodeTemplateWrapper wrapper = (TemplateListModel.CodeTemplateWrapper) list.getSelectedValue();
                if(wrapper != null){
                    wrapper.ct.setID(name.getText());
                }
            }
        });
        editor.add(name);

        Font font = new Font("Dialog", Font.PLAIN, 12);
        final JXTextArea beforeCursor = new JXTextArea();
        beforeCursor.setFont(font);
        beforeCursor.setRows(3);
        beforeCursor.setBorder(BorderFactory.createTitledBorder("Before caret text"));
        beforeCursor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update(){
                TemplateListModel.CodeTemplateWrapper wrapper = (TemplateListModel.CodeTemplateWrapper) list.getSelectedValue();
                if(wrapper != null){
                    wrapper.ct.setBeforeCaretText(beforeCursor.getText());
                }
            }
        });
        editor.add(beforeCursor);
        final JXTextArea afterCursor = new JXTextArea();
        afterCursor.setFont(font);
        afterCursor.setRows(3);
        afterCursor.setBorder(BorderFactory.createTitledBorder("After caret text"));
        afterCursor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update(){
                TemplateListModel.CodeTemplateWrapper wrapper = (TemplateListModel.CodeTemplateWrapper) list.getSelectedValue();
                if(wrapper != null){
                    wrapper.ct.setAfterCaretText(afterCursor.getText());
                }
            }
        });
        editor.add(afterCursor);

        config.add(editor, BorderLayout.SOUTH);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                TemplateListModel.CodeTemplateWrapper wrapper = (TemplateListModel.CodeTemplateWrapper) list.getSelectedValue();
                name.setText(wrapper.ct.getID());
                beforeCursor.setText(wrapper.ct.getBeforeCaretText());
                afterCursor.setText(wrapper.ct.getAfterCaretText());
            }
        });
// NORTH (Search Panel)
        JToolBar toolPanel = new JToolBar(JToolBar.HORIZONTAL);
        toolPanel.add(new CreateTemplateAction(listModel, list));
        toolPanel.add(new RemoveTemplateAction(listModel, list));
        toolPanel.addSeparator();
        final JXSearchField search = new JXSearchField("Search");
        final ListSearchable listSearchable = new ListSearchable(list);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listSearchable.search(search.getText());
            }
        });

        list.setSearchable(new ListSearchable(list));
        toolPanel.add(search);
        config.add(toolPanel, BorderLayout.NORTH);
    }

    public void reloadData(){

    }
}
