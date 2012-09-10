package com.zh.coherence.viewer.tools.query.config.template;

import com.zh.coherence.viewer.config.AbstractConfigPanel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;

import javax.swing.*;
import java.awt.*;

public class QueryTemplateConfigPanel extends AbstractConfigPanel {

    private JPanel config;

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
        return false;
    }

    private void initUI(){
        config.setLayout(new BorderLayout());
// NORTH (Search Panel)
        JToolBar toolPanel = new JToolBar(JToolBar.HORIZONTAL);
        CreateTemplateAction create = new CreateTemplateAction(null);
        toolPanel.add(create);

        toolPanel.addSeparator();
        JXSearchField search = new JXSearchField("Search");
        toolPanel.add(search);

        config.add(toolPanel, BorderLayout.NORTH);
// CENTER (Templates List)
        JXList list = new JXList();
        config.add(new JScrollPane(list), BorderLayout.CENTER);

// SOUTH (Editor)
        JPanel editor = new JPanel();

        config.add(editor, BorderLayout.SOUTH);
    }
}
