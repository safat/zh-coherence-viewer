package ua.com.zhivko.coherence.viewer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import ua.com.zhivko.coherence.viewer.actions.createNew.CreateConnection;
import ua.com.zhivko.coherence.viewer.actions.open.OpenProjectAction;

/**
 *
 * @author ZHivko
 */
public class Menu extends JMenuBar{

    public Menu() {
        //file
        JMenu file = new JMenu("File");
        add(file);
        JMenuItem createNew = new JMenuItem(new CreateConnection());
        file.add(createNew);
        JMenuItem openExist = new JMenuItem(new OpenProjectAction());
        file.add(openExist);
        //help
        JMenu help = new JMenu("Help");
        add(help);
    }
}
