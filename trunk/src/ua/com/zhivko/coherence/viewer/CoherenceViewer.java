package ua.com.zhivko.coherence.viewer;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.WindowBar;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.ViewMap;
import net.infonode.gui.laf.InfoNodeLookAndFeel;
import net.infonode.util.Direction;
import ua.com.zhivko.coherence.viewer.navigation.NavigationPanel;

/**
 * main class
 * @author ZHivko
 */
public class CoherenceViewer extends JFrame implements Observer {

    private NavigationPanel navigationPanel;

    public CoherenceViewer() {
        setJMenuBar(new Menu());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        navigationPanel = new NavigationPanel();
        ViewMap viewMap = new ViewMap();
        // The mixed view map makes it easy to mix static and dynamic views inside the same root window
        MixedViewHandler handler = new MixedViewHandler(viewMap, new ViewSerializer() {

            public void writeView(View view, ObjectOutputStream out) throws IOException {
//        out.writeInt(((DynamicView) view).getId());
            }

            public View readView(ObjectInputStream in) throws IOException {
                //return getDynamicView(in.readInt());
                return null;
            }
        });

        View navigationView = new View("Navigation", new ImageIcon("icons/start-here.png"), navigationPanel);
        viewMap.addView(200, navigationView);
        RootWindow rootWindow = DockingUtil.createRootWindow(viewMap, handler, true);

//        TabWindow tabWindow = new TabWindow(navigationView);
//        WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);
//        windowBar.addTab(navigationView);
//        rootWindow.setWindow(new SplitWindow(true,
//                                         0.3f,
//                                         new SplitWindow(false,
//                                                         0.7f,
//                                                         new TabWindow(new View[]{views[0], views[1]}),
//                                                         views[2]),
//                                         tabWindow));

        
        this.getContentPane().add(rootWindow, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
//        UIManager.setLookAndFeel(new InfoNodeLookAndFeel());
        CoherenceViewer coherenceViewer = new CoherenceViewer();
        ProjectManager.getInstance().addObserver(coherenceViewer);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.err.println("UPDATE");
        navigationPanel.updateData();
    }
}
