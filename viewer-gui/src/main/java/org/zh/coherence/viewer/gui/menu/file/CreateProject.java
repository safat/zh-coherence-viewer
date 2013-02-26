package org.zh.coherence.viewer.gui.menu.file;

import com.sun.java.swing.plaf.windows.WindowsGraphicsUtils;
import org.zh.coherence.viewer.project.editor.ProjectEditor;
import org.zh.coherence.viewer.utils.FrameUtil;
import org.zh.utils.gui.icons.IconProvider;
import sun.awt.SunToolkit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 24.12.12
 * Time: 23:25
 */
public class CreateProject extends JMenuItem {
    public CreateProject() {
        super("Create project...", IconProvider.getIcon("database_add.png"));
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComponent component = (JComponent) e.getSource();
                JFrame frame = FrameUtil.getFrame(component);
                ProjectEditor projectEditor = new ProjectEditor(frame, null);
                projectEditor.showEditor();
            }
        });
    }


}
