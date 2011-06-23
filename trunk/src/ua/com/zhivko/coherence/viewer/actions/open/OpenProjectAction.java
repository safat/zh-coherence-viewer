package ua.com.zhivko.coherence.viewer.actions.open;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import ua.com.zhivko.coherence.viewer.ProjectManager;

/**
 * @author Живко
 */
public class OpenProjectAction extends AbstractAction {

    public OpenProjectAction() {
        this.putValue(Action.NAME, "Open project");
        this.putValue(Action.SMALL_ICON, new ImageIcon("icons/document-open.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = fc.showOpenDialog((Component)e.getSource());
        if (ret == JFileChooser.APPROVE_OPTION) {
            File project = fc.getSelectedFile();
            ProjectManager.getInstance().openProject(project);
        }
    }
}
