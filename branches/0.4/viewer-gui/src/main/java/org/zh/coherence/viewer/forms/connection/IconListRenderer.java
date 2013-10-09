package org.zh.coherence.viewer.forms.connection;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 09.10.13
 * Time: 23:34
 */
public class IconListRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        if (value instanceof ConnectionProviderInfo) {
            ConnectionProviderInfo info = (ConnectionProviderInfo) value;
            ImageIcon icon = info.getIcon();
            label.setIcon(icon);
            label.setText(info.getName());
        }
        return (label);
    }
}
