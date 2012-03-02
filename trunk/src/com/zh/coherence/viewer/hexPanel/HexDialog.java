package com.zh.coherence.viewer.hexPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 29.02.12
 * Time: 20:08
 */
public class HexDialog extends JDialog{
    
    public HexDialog(Window owner, byte[] data) {
        super(owner, "Hex viewer");
        HexPanel hexPanel = new HexPanel(data);
        getContentPane().add(hexPanel);

        pack();
        setVisible(true);
    }
}
