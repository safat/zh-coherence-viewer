package org.zh.coherence.viewer.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 31.01.13
 * Time: 22:35
 */
public class FrameUtil {
    public static JFrame getFrame(Container component) {
        if (component == null) {
            throw new NullPointerException("Component is NULL");
        }
        Component parent = component;
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }

        return (JFrame) parent;
    }
}
