package ua.com.zhivko.coherence.viewer.actions.createNew;

import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import layout.TableLayout;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;
import static layout.TableLayoutConstants.*;

/**
 *
 * @author Живко
 */
public class ConfigurationPage extends WizardPage {
    private JTextField projectName = new JTextField();
    private JTextField projectPath = new JTextField();
    private JButton selectPath = new JButton(new ImageIcon("icons/document-open.png"));

    public ConfigurationPage() {
        super("create", "Configure Project");
        setPreferredSize(new Dimension(400, 200));
        double[][] tsize = new double[][]{
            {2, PREFERRED, 2, FILL, 2, PREFERRED, 2},
            {2, 25, 5, 25, 2}
        };
        setLayout(new TableLayout(tsize));

        add(new JLabel("Project name:"),"1,1");
        add(projectName, "3,1,5,1");
        add(new JLabel("Project folder:"),"1,3");
        add(projectPath, "3,3");
        projectPath.setEnabled(false);
        add(selectPath, "5,3");
        selectPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = fc.showOpenDialog(ConfigurationPage.this);
                if(ret == JFileChooser.APPROVE_OPTION){
                    projectPath.setText(fc.getSelectedFile().getPath());
                }
            }
        });
    }

    @Override
    protected String validateContents(Component component, Object event) {
        if(projectName.getText().isEmpty()){
            return "The project name field not might be empty";
        }
        if(projectPath.getText().isEmpty()){
            return "Select place to store project";
        }
        try{
            File dir = new File(projectPath.getText());
            if(dir.isFile()){
                return "Selected path is File, please select path to directory";
            }
            String path = projectPath.getText() + File.pathSeparator
                    + projectName.getText();
            File prDir = new File(path);
            if(prDir.exists()){
                return "directory with name: " + path + " already exist";
            }
        }catch(Exception ex){
            return "Selected path does not exist";
        }
        getWizardDataMap().put("pr_name", projectName.getText());
        getWizardDataMap().put("pr_path", projectPath.getText());
        return null;
    }

    public static String getDescription() {
        return "Configure Project";
    }

    @Override
    public WizardPanelNavResult allowNext(String stepName, Map settings, Wizard wizard) {
        return WizardPanelNavResult.PROCEED;
    }
}
