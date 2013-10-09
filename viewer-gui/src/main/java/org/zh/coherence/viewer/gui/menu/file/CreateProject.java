package org.zh.coherence.viewer.gui.menu.file;

import org.jdesktop.swingx.util.WindowUtils;
import org.zh.coherence.viewer.forms.connection.CreateConnectionForm;
import org.zh.utils.gui.icons.IconProvider;

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
        super("New connection...", IconProvider.getIcon("database_add.png"));
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComponent component = (JComponent) e.getSource();
                Window window = WindowUtils.findWindow(component);

                JDialog dialog = new JDialog(window, "Create new connection", Dialog.ModalityType.APPLICATION_MODAL);
                CreateConnectionForm createConnectionForm = new CreateConnectionForm();
                dialog.getContentPane().setLayout(new BorderLayout());
                dialog.getContentPane().add(createConnectionForm);
                dialog.setSize(600, 250);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setLocationRelativeTo(window);
                dialog.setVisible(true);

                //show connection editor

            }
        });
    }


}
