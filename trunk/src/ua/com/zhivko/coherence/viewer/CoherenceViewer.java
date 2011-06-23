package ua.com.zhivko.coherence.viewer;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
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
        NamedCache cache = CacheFactory.getCache("test");
        cache.put("key" + (new Random().nextInt()), "value");
        System.err.println("size: " + cache.size());
        try {
            System.err.println("caches: " + getCaches("localhost2"));
        } catch (MalformedObjectNameException ex) {
            Logger.getLogger(CoherenceViewer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(CoherenceViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);

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
        try {
            System.err.println("UPDATE");
            navigationPanel.updateData();
        } catch (IOException ex) {
            Logger.getLogger(CoherenceViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Set<String> getCaches(String jmxDomainName) throws MalformedObjectNameException, NullPointerException {
        // find (or create) an MBeanServer for the specified default JMX domain
        // name
        MBeanServer mServer = null;
        for (MBeanServer server : MBeanServerFactory.findMBeanServer(null)) {
            if (jmxDomainName.length() == 0
                    || server.getDefaultDomain().equals(jmxDomainName)) {
                mServer = server;
                break;
            }
        }

        if (mServer == null) {
            mServer = MBeanServerFactory.createMBeanServer(jmxDomainName);
        }

        // get the set of all Coherence Member MBean ObjectNames
        ObjectName objNameMembers = new ObjectName("Coherence:type=Node,*");
        @SuppressWarnings("unused")
        Set<ObjectName> setObjNameMembers = mServer.queryNames(objNameMembers, null);

        // get the set of Coherence Cache names (obtained from the 'name'
        // property on
        // a Cache MBean ObjectName)
        ObjectName objNameCaches = new ObjectName("Coherence:type=Cache,*");
        Set<ObjectName> setObjNameCaches = mServer.queryNames(objNameCaches, null);
        Set<String> cacheNames = new HashSet<String>();

        for (ObjectName objName : setObjNameCaches) {
            cacheNames.add(objName.getKeyProperty("name"));
        }

        return cacheNames;
    }
}
