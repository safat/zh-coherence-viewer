package org.zh.coherence.viewer.gui;

import bibliothek.gui.dock.common.*;
import org.zh.coherence.viewer.gui.menu.ApplicationMenu;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 12.12.12
 * Time: 21:55
 */
public class Application {

    private JFrame frame = new JFrame();

    public Application() { //todo try to run the application
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
}
