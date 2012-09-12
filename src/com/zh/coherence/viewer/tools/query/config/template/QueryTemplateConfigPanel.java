package com.zh.coherence.viewer.tools.query.config.template;

import com.zh.coherence.viewer.config.AbstractConfigPanel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;

public class QueryTemplateConfigPanel extends AbstractConfigPanel {

    private JPanel config;
    private TemplateListModel listModel;

    @Override
    public void applyChanges() {

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
        JXList list = new JXList(listModel);
        config.add(new JScrollPane(list), BorderLayout.CENTER);

// SOUTH (Editor)
        JPanel editor = new JPanel(new VerticalLayout());
        JTextField name = new JTextField();
        name.setBorder(BorderFactory.createTitledBorder("Name"));
        editor.add(name);
        JXTextArea beforeCursor = new JXTextArea();
        beforeCursor.setRows(3);
        beforeCursor.setBorder(BorderFactory.createTitledBorder("Before cursor"));
        editor.add(beforeCursor);
        JXTextArea afterCursor = new JXTextArea();
        afterCursor.setRows(3);
        afterCursor.setBorder(BorderFactory.createTitledBorder("After cursor"));
        editor.add(afterCursor);

        config.add(editor, BorderLayout.SOUTH);

// NORTH (Search Panel)
        JToolBar toolPanel = new JToolBar(JToolBar.HORIZONTAL);
        CreateTemplateAction create = new CreateTemplateAction(listModel, list);
        toolPanel.add(create);
        toolPanel.addSeparator();
        JXSearchField search = new JXSearchField("Search");
        toolPanel.add(search);
        config.add(toolPanel, BorderLayout.NORTH);
    }

    public void reloadData(){

    }
}
