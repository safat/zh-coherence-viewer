package org.zh.coherence.viewer.forms.connection;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 02.10.13
 * Time: 21:45
 */
public class CreateConnectionForm extends JXPanel {

    private JXList connectionTypeList;
    private ConnectionPluginListModel connectionPluginListModel;

    private JXPanel connectionInformationPanel;

    public CreateConnectionForm() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        connectionTypeList = new JXList();
        connectionTypeList.setBorder(BorderFactory.createTitledBorder("Connection type"));
        connectionTypeList.setCellRenderer(new IconListRenderer());
        connectionPluginListModel = new ConnectionPluginListModel();
        connectionTypeList.setModel(connectionPluginListModel);

        connectionInformationPanel = new JXPanel(new BorderLayout());
        connectionInformationPanel.setBorder(BorderFactory.createTitledBorder("Connection properties"));

        add(new JScrollPane(connectionTypeList), BorderLayout.WEST);
        add(connectionInformationPanel, BorderLayout.CENTER);
    }
}
