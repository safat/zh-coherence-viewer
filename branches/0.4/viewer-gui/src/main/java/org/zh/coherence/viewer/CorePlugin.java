package org.zh.coherence.viewer;

import org.java.plugin.boot.Application;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.boot.Boot;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.util.ExtendedProperties;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 18.01.13
 * Time: 22:17
 */
public class CorePlugin extends ApplicationPlugin implements Application {

    private File dataFolder;
    private JFrame frame;

    @Override
    public Application initApplication(ExtendedProperties config, String[] strings) throws Exception {
        dataFolder = new File(config.getProperty("dataFolder",
                "." + File.separator + "data"));
        dataFolder = dataFolder.getCanonicalFile();
        log.debug("data folder - " + dataFolder);
        if (!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            throw new Exception("data folder " + dataFolder + " not found");
        }
        return this;
    }

    @Override
    public void doStart() throws Exception {
    }

    @Override
    public void doStop() throws Exception {
    }

    @Override
    public void startApplication() throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    protected void createAndShowGUI() {
        frame = new JFrame("ZH Coherence Viewer 0.4");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/database.png")).getImage());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                try {
                    saveState();
                    JOptionPane.getRootFrame().dispose();
                    Boot.stopApplication(CorePlugin.this);
                } catch (Exception ignored) {
                }
                System.exit(0);
            }
        });
        readState();
        frame.add(new JLabel("test"));
        frame.setVisible(true);
    }

    private void readState() {
        Properties props = new Properties();
        try {
            InputStream strm = new FileInputStream(getConfigFile());
            try {
                props.load(strm);
            } finally {
                strm.close();
            }
        } catch (Exception e) {
            log.error("can't load program state", e);
        }
        frame.setBounds(
                Integer.parseInt(props.getProperty("window.X", "10"), 10),
                Integer.parseInt(props.getProperty("window.Y", "10"), 10),
                Integer.parseInt(props.getProperty("window.width", "600"), 10),
                Integer.parseInt(props.getProperty("window.height", "500"), 10));
    }

    private File getConfigFile() throws IOException {
        File result = new File(getDataFolder(getDescriptor()),
                "config.properties");
        if (!result.exists() && !result.createNewFile()) {
            throw new IOException("can't create configuration file " + result);
        }
        return result;
    }

    /**
     * Returns folder where given plug-in can store it's data.
     *
     * @param descr plug-in descriptor
     * @return plug-in data folder
     * @throws IOException if folder doesn't exist and can't be created
     */
    public File getDataFolder(final PluginDescriptor descr) throws IOException {
        File result = new File(dataFolder, descr.getId());
        if (!result.isDirectory() && !result.mkdirs()) {
            throw new IOException("can't create data folder " + result
                    + " for plug-in " + descr.getId());
        }
        return result;
    }

    protected void saveState() {
        Properties props = new Properties();
        props.setProperty("window.X", "" + frame.getX());
        props.setProperty("window.Y", "" + frame.getY());
        props.setProperty("window.width", "" + frame.getWidth());
        props.setProperty("window.height", "" + frame.getHeight());
        try {
            OutputStream strm = new FileOutputStream(getConfigFile(), false);
            try {
                props.store(strm, "This is automatically generated configuration file.");
            } finally {
                strm.close();
            }
        } catch (Exception e) {
            log.error("can't save program state", e);
        }
    }
}
