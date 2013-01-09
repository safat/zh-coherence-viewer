package org.zh.coherence.viewer.gui;

import bibliothek.gui.dock.common.*;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.zh.coherence.viewer.gui.menu.ApplicationMenu;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 12.12.12
 * Time: 21:55
 */
public class Application implements BundleActivator, ServiceListener {

    private JFrame frame = new JFrame();

    public Application() {
        initFrame();
    }

    public void initFrame() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ApplicationMenu menu = new ApplicationMenu();
        frame.setJMenuBar(menu);

        frame.getContentPane().add(getApplicationContainer());
        frame.setSize(1024, 768);
        frame.setVisible(true);
    }

    public JComponent getApplicationContainer() {
        return new JPanel();
    }

    public static void main(String[] args) {
        new Application();

//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//
//        CControl control = new CControl(frame);
//        control.setTheme(new EclipseTheme());
//        frame.setLayout(new GridLayout(1, 1));
//
//        frame.add(control.getContentArea());
//
//        SingleCDockable red = create("Red", Color.RED);
//        SingleCDockable green = create("Green", Color.GREEN);
//        SingleCDockable blue = create("Blue", Color.BLUE);
//
//        control.addDockable(red);
//        control.addDockable(green);
//        control.addDockable(blue);
//
//        red.setVisible(true);
//
//        green.setLocation(CLocation.base().normalSouth(0.4));
//        green.setVisible(true);
//
//        blue.setLocation(CLocation.base().normalEast(0.3));
//        blue.setVisible(true);
//
//        frame.setBounds(20, 20, 400, 400);
//        frame.setVisible(true);
    }

    public static SingleCDockable create(String title, Color color) {
        JPanel bg = new JPanel();
        bg.setOpaque(true);
        bg.setBackground(color);

        return new DefaultSingleCDockable(title, title, bg);
    }

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Starting to listen for service events.");
        context.addServiceListener(this);
        initFrame();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context.removeServiceListener(this);
        System.out.println("Stopped listening for service events.");
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        String[] objectClass = (String[])
                event.getServiceReference().getProperty("objectClass");

        if (event.getType() == ServiceEvent.REGISTERED) {
            System.out.println(
                    "Ex1: Service of type " + objectClass[0] + " registered.");
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
            System.out.println(
                    "Ex1: Service of type " + objectClass[0] + " unregistered.");
        } else if (event.getType() == ServiceEvent.MODIFIED) {
            System.out.println(
                    "Ex1: Service of type " + objectClass[0] + " modified.");
        }
    }
}
