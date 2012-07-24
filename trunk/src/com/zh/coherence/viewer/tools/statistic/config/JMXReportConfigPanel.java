package com.zh.coherence.viewer.tools.statistic.config;

import com.zh.coherence.viewer.config.AbstractConfigPanel;
import com.zh.coherence.viewer.tools.statistic.config.actions.*;
import layout.TableLayout;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JMXReportConfigPanel extends AbstractConfigPanel {

    private JXList tabsList;
    private JXTable tabProperties;
    private TabConfigListModel tabConfigListModel;
    private JMXConfigTableModel jmxConfigTableModel;

    private JPanel root;

    public JMXReportConfigPanel() {
        initUI();
    }

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
        updateUI();
        return root;
    }

    @Override
    public boolean leaveThePage() {
        return true;
    }

    private void initUI(){
        tabConfigListModel = new TabConfigListModel();
        tabsList = new JXList(tabConfigListModel);
        jmxConfigTableModel = new JMXConfigTableModel();
        tabProperties = new JXTable(jmxConfigTableModel);
        root = new JPanel(new TableLayout(new double[][]{
                {4, 250, 5, TableLayout.FILL, 4},
                {2, TableLayout.PREFERRED, 2, TableLayout.FILL, 2}
        }));

        JToolBar tabsBar = new JToolBar(JToolBar.HORIZONTAL);
        tabsBar.setFloatable(false);
        tabsBar.add(new AddTabConfigAction(tabConfigListModel));
        tabsBar.add(new RemoveTabConfigAction(tabConfigListModel, tabsList));
        tabsBar.addSeparator();
        tabsBar.add(new MoveListItemUpAction(tabConfigListModel, tabsList));
        tabsBar.add(new MoveListItemDownAction(tabConfigListModel, tabsList));

        tabsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object value = tabsList.getSelectedValue();
                if(value != null && value instanceof TabContainer){
                    jmxConfigTableModel.setTabContainer((TabContainer) value);
                }
            }
        });

        JToolBar tableBar = new JToolBar(JToolBar.HORIZONTAL);
        tableBar.setFloatable(false);
        tableBar.add(new AddTableItemAction(jmxConfigTableModel));
        tableBar.add(new RemoveTableItemAction(jmxConfigTableModel));
        tableBar.addSeparator();
        tableBar.add(new MoveTableItemUpAction(jmxConfigTableModel));
        tableBar.add(new MoveTableItemDownAction(jmxConfigTableModel));

        root.add(tabsBar, "1, 1");
        root.add(new JScrollPane(tabsList), "1, 3");

        root.add(tableBar, "3, 1");
        root.add(new JScrollPane(tabProperties), "3, 3");


    }

    private void updateUI(){

    }
}
