package com.zh.coherence.viewer;

import com.zh.coherence.viewer.config.PofConfigPane;
import com.zh.coherence.viewer.utils.FileUtils;
import org.jdesktop.swingx.JXHeader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 03.03.12
 * Time: 0:51
 */
public class BootLoader {

    public BootLoader() {
        try {
            if (!checkIsCoherenceExist()) {
                askCoherenceJar();
                JOptionPane.showMessageDialog(null, "Class path was updated. Restart application please.",
                        "Reload", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            //load properties
//            Properties config = new PropertiesLoader("config/viewer.properties").load();
//            if(config != null){
//                String path = (String) config.get("pof.config");
//                if(path == null){
//                    //update pof config
//                    String result = askPofConfig();
//                }
//            }else{
//                System.err.println("file: config/viewer.properties not found"); //todo this check is stupid
//                System.exit(-1);
//            }

            new Application();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception {
        new BootLoader();
    }

    private File askCoherenceJar() {
        File selected = null;
        int ret = JOptionPane.showConfirmDialog(null, "<html>Coherence library was not found.<br>" +
                "Can you please choose it?</html>", "Coherence Library", JOptionPane.OK_CANCEL_OPTION);
        if (ret == JOptionPane.OK_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".jar");
                }

                @Override
                public String getDescription() {
                    return "*.jar";
                }
            });
            fileChooser.showOpenDialog(null);
            selected = fileChooser.getSelectedFile();
        } else {
            System.exit(0);
        }
        //copy coherence into directory
        File out = null;
        if (selected != null) {
            out = new File("coherence-lib/coherence.jar");
            try {
                FileUtils.copyFile(selected, out);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.exit(0);
        }

        return out;
    }

    private boolean checkIsCoherenceExist() {
        boolean ret = false;
        try {
            Class cls = Class.forName("com.tangosol.net.NamedCache");
            if (cls != null) {
                ret = true;
            }
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    private String askPofConfig(){
        String files = null;
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Pof config");
        Container cont = dialog.getContentPane();
        cont.setLayout(new BorderLayout());
        PofConfigPane configPane = new PofConfigPane();
        cont.add(configPane, BorderLayout.CENTER);

        JXHeader header = new JXHeader("Pof Config", "Add your pof config files if need.");
        header.setIcon(new ImageIcon("icons/Customization-icon.png"));
        cont.add(header, BorderLayout.NORTH);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));

        cont.add(buttons, BorderLayout.SOUTH);

        dialog.setSize(500,300);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return files;
    }
}