package ua.com.zhivko.coherence.viewer.actions.createNew;

import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Component;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import layout.TableLayout;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;
import static layout.TableLayoutConstants.*;

/**
 *
 * @author Живко
 */
public class ConnectionPage extends WizardPage{
    private JRadioButton xmlRadio = new JRadioButton();
    private JRadioButton confRadio = new JRadioButton();

    private JTextField confPath = new JTextField();
    private JButton confBut = new JButton(new ImageIcon("icons/document-open.png"));
    private JTextField pofPath = new JTextField();
    private JButton pofBut = new JButton(new ImageIcon("icons/document-open.png"));
    private JTextField host = new JTextField("localhost");
    private JTextField port = new JTextField("8200");

    public ConnectionPage() {
        super("create", "Configure connection");
        setPreferredSize(new Dimension(400, 200));
        double[][] tsize=new double[][]{
            {2,PREFERRED,2,PREFERRED,2,FILL,2,PREFERRED,2},
            {2,PREFERRED,2,25,4,PREFERRED,2,25,2,25,5,PREFERRED,2,25,2}
        };
        setLayout(new TableLayout(tsize));

        //PATH
        JPanel rp1 = new JPanel(new BorderLayout());
        rp1.add(xmlRadio, BorderLayout.NORTH);
        xmlRadio.setSelected(true);
        add(rp1, "1,1,1,3");

        JLabel xmlInfo = new JLabel("Select exist XML config");
        xmlInfo.setBorder(new EtchedBorder());
        add(xmlInfo,"3,1,7,1");

        add(confPath, "3,3,5,3");
        add(confBut, "7,3");
        confPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int ret = fc.showOpenDialog(ConnectionPage.this);
                if(ret == JFileChooser.APPROVE_OPTION){
                    confPath.setText(fc.getSelectedFile().getPath());
                }
            }
        });

        //CONF
        JPanel rp2 = new JPanel(new BorderLayout());
        rp2.add(confRadio, BorderLayout.NORTH);
        add(rp2, "1,5");
        JLabel confInfo = new JLabel("Create new config");
        confInfo.setBorder(new EtchedBorder());
        add(confInfo,"3,5,7,5");

        add(new JLabel("Server host:"),"3,7");
        add(host, "5,7,7,7");

        add(new JLabel("Server port:"),"3,9");
        add(port,"5,9,7,9");

        //POF
        JPanel rp3 = new JPanel(new BorderLayout());
        rp3.setBorder(new EtchedBorder());
        add(rp3, "1,11");
        JLabel pofInfo = new JLabel("Select POF config if exist");
        pofInfo.setBorder(new EtchedBorder());
        add(pofInfo,"3,11,7,11");
        
        add(pofPath, "3,13,5,13");
        add(pofBut, "7,13");
         pofPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int ret = fc.showOpenDialog(ConnectionPage.this);
                if(ret == JFileChooser.APPROVE_OPTION){
                    pofPath.setText(fc.getSelectedFile().getPath());
                }
            }
        });

        selectPathRadio();
    }

    private void selectPathRadio(){
        host.setEnabled(false);
        port.setEnabled(false);
    }

    private void selectConfRadio(){

    }

    protected String validateContents(Component component, Object event) {
        getWizardDataMap().put("conn_conf_path", confPath.getText());
        getWizardDataMap().put("conn_host", host.getText());
        getWizardDataMap().put("conn_port", port.getText());
        getWizardDataMap().put("conn_pof_config", pofPath.getText());
        return null;
    }

    public static String getDescription() {
        return "Configure connection";
    }

    public WizardPanelNavResult allowNext(String stepName, Map settings, Wizard wizard) {
        return WizardPanelNavResult.PROCEED;
    }
}
