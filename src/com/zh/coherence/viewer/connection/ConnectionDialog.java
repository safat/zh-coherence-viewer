package com.zh.coherence.viewer.connection;

import layout.TableLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 19:38
 */
public class ConnectionDialog extends JDialog {
    public ConnectionDialog(JFrame owner) {
        super(owner, "Connection", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 260);

        setContentPane(generateView());
    }

    public JPanel generateView() {
        double[][] layout = new double[][]{
                {5, PREFERRED, 10, FILL, 5},
                {5, 100, PREFERRED, 7, PREFERRED, 4, PREFERRED, 5, PREFERRED, 5}
        };
        JPanel pane = new JPanel(new TableLayout(layout));

        pane.add(new JLabel("saved addresses"), "1, 2");
        JComboBox hostList = new JComboBox();

        pane.add(hostList, "3,2");

        pane.add(new JLabel("Coherence host:"), "1, 4");
        final JTextField host = new JTextField();
        pane.add(host, "3, 4");
        pane.add(new JLabel("Port (default is 8089)"), "1, 6");
        final JTextField port = new JTextField();
        pane.add(port, "3, 6");
        hostList.addItemListener(new ServerItemListener(host, port));

        JPanel buttons = new JPanel(new TableLayout(new double[][]{
                {FILL, 100, 5, 100, FILL},
                {PREFERRED}
        }));
        JButton ok = new JButton("Connect");
        ok.setContentAreaFilled(false);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CoherenceConfigGenerator generator = new CoherenceConfigGenerator();
                //todo check if data correct
                generator.setupExtendConfig(host.getText(), port.getText());
                ConnectionDialog.this.dispose();
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.setContentAreaFilled(false);
        buttons.add(ok, "1,0");
        buttons.add(cancel, "3,0");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pane.add(buttons, "1,8,3,8");

        //load saved servers
        for(CoherenceServer server : getSavedServers()){
            hostList.addItem(server);
        }

        return pane;
    }

    private List<CoherenceServer> getSavedServers(){
        List<CoherenceServer> list = new ArrayList<CoherenceServer>();
        try{
            FileInputStream fis = new FileInputStream(new File("config/server.list"));
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            
            String name;
            String value;
            String[] data;
            
            for(Map.Entry entry : properties.entrySet()){
                name = (String) entry.getKey();
                value = (String) entry.getValue();
                if(value == null){
                    //todo log problem
                    continue;
                }
                data = value.split(":");
                if(data.length != 2){
                    //todo Log problem
                    continue;
                }
                list.add(new CoherenceServer(name, data));
            }

        }catch(IOException ex){
            ex.printStackTrace();
            throw new IllegalStateException("I couldn't load properties file: config/server.list");
        }
        
        return list;
    }
    
    private class CoherenceServer{
        public String name;

        public String[] data;

        private CoherenceServer(String name, String[] data) {
            this.name = name;
            this.data = data;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private class ServerItemListener implements ItemListener{
        JTextField host, port;

        private ServerItemListener(JTextField host, JTextField port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED){
                Object item = e.getItem();
                if(item instanceof  CoherenceServer){
                    host.setText(((CoherenceServer) item).data[0]);
                    port.setText(((CoherenceServer) item).data[1]);
                }else{
                    host.setText("");
                    port.setText("");
                }
            }
        }
    }
}
