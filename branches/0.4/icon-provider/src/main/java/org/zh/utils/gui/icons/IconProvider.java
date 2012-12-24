package org.zh.utils.gui.icons;

import javax.swing.*;
import java.io.File;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 24.12.12
 * Time: 23:36
 */
public class IconProvider {
    public static final String APPLICATION_ICONS_DIRECTORY = "application.icons";

    private static File systemDirectory = null;
    private static boolean existDirectory = true;

    public static Icon getIcon(String name) throws Exception {
        Icon icon = null;

        //First. try to search in the home directory: icons
        if (existDirectory) {
            if (systemDirectory == null) {
                initializeSystemDirectory();
            }
            File iconFile = new File(systemDirectory, name);
            if (iconFile.exists()) {
                icon = new ImageIcon(iconFile.toURI().toURL());
            }
        }
        //Not found? try to search right there
        if (icon == null) {
            URL iconUrl = ClassLoader.getSystemResource(name);
            if (iconUrl != null) {
                icon = new ImageIcon(iconUrl);
            }
        }
        //or return null, sorry
        return icon;
    }

    private static void initializeSystemDirectory() {
        String home = System.getProperty(APPLICATION_ICONS_DIRECTORY);
        if (home == null) {
            home = File.pathSeparator;
        }

        systemDirectory = new File(home);
        existDirectory = systemDirectory.exists();
    }
}
