package com.zh.coherence.viewer;

import com.zh.coherence.viewer.utils.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class BootLoader {

    public BootLoader() {
        try {
            if (!checkIsCoherenceExist()) {
                askCoherenceJar();
                JOptionPane.showMessageDialog(null, "Class path was updated. Restart application please.",
                        "Reload", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
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
}