package com.zh.coherence.viewer.hexPanel;

import com.zh.coherence.viewer.pof.ValueContainer;
import com.zh.coherence.viewer.tableview.user.UserObjectViewer;
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
public class HexPanel extends JPanel implements UserObjectViewer{
    private HexEditor editor;

    public HexPanel() {
        super(new BorderLayout());

        editor = new HexEditor();
        editor.setCellEditable(false);

        add(editor, BorderLayout.CENTER);
    }

    @Override
    public JComponent getPane(Object value) {
        if(value instanceof ValueContainer){
            ByteArrayInputStream bis = new ByteArrayInputStream(((ValueContainer) value).getBinary());
            try {
                editor.open(bis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }
}
