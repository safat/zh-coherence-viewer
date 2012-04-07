package com.zh.coherence.viewer.connection;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import layout.TableLayout;
import org.jdesktop.swingx.JXHeader;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.event.*;
import java.io.File;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 19:38
 */
public class ConnectionDialog extends JDialog {
    private ServerList serverList = null;
    private JTextField jmxUrl, host, port;
    private JComboBox hostList;
    private JCheckBox ignoreUserConfig;

    public ConnectionDialog(JFrame owner) {
        super(owner, "Connection", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(520, 285);

        setContentPane(generateView());
    }

    public JPanel generateView() {
        double[][] layout = new double[][]{
                {5, PREFERRED, 10, FILL, 5},
                {5, 85, PREFERRED, 7, PREFERRED, 4, PREFERRED, 5, PREFERRED, 5, PREFERRED, 5, PREFERRED, 5}
        };
        JPanel pane = new JPanel(new TableLayout(layout));

        JXHeader loginHeader = new JXHeader("Login",
                "Login to the Coherence server", IconHelper.getInstance().getIcon(IconType.LOGIN));

        pane.add(loginHeader, "1,1,3,1");
        pane.add(new JLabel("Connection's name"), "1, 2");
        hostList = new JComboBox();
        hostList.setEditable(true);

        pane.add(hostList, "3,2");

        pane.add(new JLabel("Coherence host:"), "1, 4");
        host = new JTextField();
        host.addKeyListener(new LoginKeyListener());
        pane.add(host, "3, 4");
        pane.add(new JLabel("Port:"), "1, 6");
        port = new JTextField();
        port.addKeyListener(new LoginKeyListener());
        pane.add(port, "3, 6");
        jmxUrl = new JTextField();
        jmxUrl.addKeyListener(new LoginKeyListener());
        pane.add(new JLabel("JMX URL:"), "1,8");
        pane.add(jmxUrl, "3,8");
        ignoreUserConfig = new JCheckBox("Ignore user POF config");
        pane.add(ignoreUserConfig, "3,10");
        ignoreUserConfig.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.setProperty("zh.coherence.viewer.ignoreUserPof", String.valueOf(ignoreUserConfig.isSelected()));
            }
        });

        hostList.addItemListener(new ServerItemListener(host, port, jmxUrl, ignoreUserConfig));

        JPanel buttons = new JPanel(new TableLayout(new double[][]{
                {FILL, 100, 5, 100, FILL},
                {PREFERRED}
        }));
        JButton ok = new JButton("Connect");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        JButton cancel = new JButton("Cancel");
        buttons.add(ok, "1,0");
        buttons.add(cancel, "3,0");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pane.add(buttons, "1,12,3,12");

        //load saved servers
        serverList = readServerList();
        for (ServerConfig config : serverList.getList()) {
            hostList.addItem(config);
        }

        return pane;
    }

    private class LoginKeyListener extends KeyAdapter{
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                login();
            }
        }
    }

    private void login(){
        CoherenceConfigGenerator generator = new CoherenceConfigGenerator();
        generator.setupExtendConfig(host.getText(), port.getText());
        if(serverList == null){
            serverList = new ServerList();
        }
        //save data
        ServerConfig config;
        Object obj = hostList.getSelectedItem();
        if(obj instanceof ServerConfig){
            config = (ServerConfig) obj;
        }else{
            config = new ServerConfig();
            serverList.addServerConfig(config);
        }
        config.setName(obj.toString());
        config.setHost(host.getText());
        config.setPort(Integer.parseInt(port.getText()));
        String jmxUrlString = jmxUrl.getText().trim();
        config.setJmxUrl(jmxUrlString);
        if(!jmxUrlString.isEmpty()){
            JMXManager.getInstance().connect(jmxUrlString);
        }

        config.setIgnoreUserPof(ignoreUserConfig.isSelected());
        writeServerList(serverList);
        ConnectionDialog.this.dispose();
    }

    private ServerList readServerList() {
        ServerList list = null;
        File file = new File("config/server-list.xml");
        try {
            if (file.exists()) {
                JAXBContext context = JAXBContext.newInstance(ServerList.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                list = (ServerList) unmarshaller.unmarshal(file);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        if (list == null) {
            list = new ServerList();
        }
        return list;
    }

    private void writeServerList(ServerList serverList) {
        try {
            JAXBContext context = JAXBContext.newInstance(ServerList.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(serverList, new File("config/server-list.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private class ServerItemListener implements ItemListener {
        private JTextField host, port, jmxUrl;
        private JCheckBox ignoreUserConfig;

        private ServerItemListener(JTextField host, JTextField port,JTextField jmxUrl, JCheckBox ignoreUserConfig) {
            this.host = host;
            this.port = port;
            this.jmxUrl = jmxUrl;
            this.ignoreUserConfig = ignoreUserConfig;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                if(item instanceof  ServerConfig){
                    ServerConfig config = (ServerConfig) item;
                    host.setText(config.getHost());
                    port.setText(String.valueOf(config.getPort()));
                    jmxUrl.setText(config.getJmxUrl());
                    ignoreUserConfig.setSelected(config.isIgnoreUserPof());
                }
            }
        }
    }
}
