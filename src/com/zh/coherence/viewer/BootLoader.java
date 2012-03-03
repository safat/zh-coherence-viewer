package com.zh.coherence.viewer;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 03.03.12
 * Time: 0:51
 */
public class BootLoader {
    /**
     * Holds the child process
     */
    private Process child;

    public BootLoader() {
        try {
            Properties properties = getProperties();
            String javaPath = properties.getProperty("java.path");
            String classpath = properties.getProperty("classpath");
            String vmOptions = properties.getProperty("vm.options");
            String mainClass = properties.getProperty("main.class");
            String splash = properties.getProperty("splash.image");

            String coherenceHome = properties.getProperty("coherence.home");
            if (coherenceHome == null) {
                File coherenceHomeFile = askCoherenceHome();
                properties.setProperty("coherence.home", coherenceHomeFile.getAbsolutePath());
                saveProperties(properties);
                coherenceHome = coherenceHomeFile.getAbsolutePath();
            }
            File coherenceJar = new File(coherenceHome, "lib/coherence.jar");
            coherenceHome = coherenceJar.getAbsolutePath();

            classpath += File.pathSeparatorChar;
            classpath += coherenceHome;

            StringBuilder sb = new StringBuilder();
            sb.append(javaPath).append("\" ");
            sb.append(vmOptions).append(" ");
            sb.append("-splash:").append(splash).append(" ");
            sb.append("-cp ").append(classpath).append(" ");
            sb.append(mainClass);
            sb.append("\"");

            System.err.println("\n");
            System.err.println(sb.toString());
            System.err.println("\n");

            ProcessBuilder pb = new ProcessBuilder(sb.toString());
            pb.redirectErrorStream(true);
            child = pb.start();
//            SplashScreen splashScreen = SplashScreen.getSplashScreen();
//            if (splash != null) {
//                splashScreen.close();
//            }
            new FollowerThread().start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveProperties(Properties properties) throws IOException {
        File propFile = new File("config/bootLoader.properties");
        FileOutputStream fos = new FileOutputStream(propFile);
        properties.store(fos, "zh coherence viewer config file");
        fos.flush();
        fos.close();
    }

    /**
     * @param args the command line arguments
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception {
        new BootLoader();
    }

    private static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        File propFile = new File("config/bootLoader.properties");
        if (propFile.isFile()) {
            InputStream in = new FileInputStream(propFile);
            properties.load(in);
            in.close();

            return properties;
        } else {
            throw new FileNotFoundException("file: '" + propFile + "' not found");
        }
    }

    private File askCoherenceHome() {
        File selected = null;
        int ret = JOptionPane.showConfirmDialog(null, "<html>Coherence home directory was not found.<br>" +
                "Can you please choose it?</html>", "Coherence Home", JOptionPane.OK_CANCEL_OPTION);
        if (ret == JOptionPane.OK_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.showOpenDialog(null);
            selected = fileChooser.getSelectedFile();
        } else {
            System.exit(0);
        }
        //check selected directory
        if (!checkCoherenceHome(selected)) {
            JOptionPane.showMessageDialog(null, "Selected folder isn't Coherence home.");
            System.exit(-1);
        }

        return selected;
    }

    private boolean checkCoherenceHome(File file) {
        boolean ret;
        try{
            File lib = new File(file, "lib");
            String[] files = lib.list();
            ret = false;
            for (String name : files) {
                if("coherence.jar".equalsIgnoreCase(name)){
                    ret = true;
                    break;
                }
            }
        }catch (Exception ex){
            ret = false;
        }
        return ret;
    }

    /**
     * Writes on std out the output of the child thread
     */
    class FollowerThread extends Thread {

        public FollowerThread() {
            setPriority(MIN_PRIORITY);
        }

        @Override
        public void run() {
            InputStream stream = child.getInputStream();
            byte[] buf = new byte[1024];
            try {
                int read = stream.read(buf);
                while (read >= 0) {
                    System.out.write(buf, 0, read);
                    read = stream.read(buf);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}