package com.zh.coherence.viewer;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 0:10
 */
public class Application {

    public Application() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }
        ApplicationMainFrame frame = new ApplicationMainFrame();
        frame.showFrame();
    }
}
