package com.zh.coherence.viewer.utils.icons;

import javax.swing.*;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 15.03.12
 * Time: 4:50
 */
public class IconHelper {
    private final static IconHelper HELPER = new IconHelper();
    private Map<IconType, Icon> icons = new EnumMap<IconType, Icon>(IconType.class);


    private IconHelper() {
    }

    public static IconHelper getInstance() {
        return HELPER;
    }

    public Icon getIcon(IconType type) {
        try {
            if (icons.containsKey(type)) {
                return icons.get(type);
            } else {
                URL url = getClass().getResource(type.getType());
                ImageIcon icon = new ImageIcon(url);
                icons.put(type, icon);
                return icon;
            }
        } catch (Exception ex) {
            System.err.println("icon: " + type + " not found");
            ex.printStackTrace();
            return null;
        }
    }
}
