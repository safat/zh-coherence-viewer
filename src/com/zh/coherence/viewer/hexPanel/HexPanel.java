package com.zh.coherence.viewer.hexPanel;

import org.fife.ui.hex.swing.HexEditor;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 29.02.12
 * Time: 20:07
 */
public class HexPanel extends JPanel {
    private HexEditor editor;
    private byte[] data;

    public HexPanel(byte[] data) {
        super(new BorderLayout());
        this.data = data;

        editor = new HexEditor();
        editor.setCellEditable(false);

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try {
            editor.open(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(editor, BorderLayout.CENTER);
    }

    public byte[] getData() {
        return data;
    }
}
