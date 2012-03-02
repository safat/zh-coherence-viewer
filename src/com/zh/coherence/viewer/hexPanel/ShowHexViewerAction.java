package com.zh.coherence.viewer.hexPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 29.02.12
 * Time: 20:21
 */
public class ShowHexViewerAction extends AbstractAction {
    private byte[] data;

    public ShowHexViewerAction(byte[] data) {
        this.data = data;

        this.putValue(Action.NAME, "Hex viewer");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        HexDialog dialog = new HexDialog(null, data);
    }
}
