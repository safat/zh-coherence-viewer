package com.zh.coherence.viewer;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 0:36
 */
public class ResourceManager {
    private final static ResourceManager manager = new ResourceManager();

    private ApplicationPane applicationPane;

    public static ResourceManager getInstance(){
        return manager;
    }

    JMenuBar mainMenuBar;

    private ResourceManager() {
        mainMenuBar = new JMenuBar();
    }

    public void addMenu(JMenu menu){
        mainMenuBar.add(menu);
    }
    
    public JMenu getMenu(String name){
        if(name == null){
            throw new IllegalArgumentException("name");
        }
        String tmp;
        for(int i = 0, size = mainMenuBar.getMenuCount(); i < size; i++){
            tmp = mainMenuBar.getMenu(i).getName();
            if(name.equals(tmp)){
                return mainMenuBar.getMenu(i);
            }
        }

        return null;
    }

    public JMenuBar getMenuBar() {
        return mainMenuBar;
    }

    public ApplicationPane getApplicationPane() {
        return applicationPane;
    }

    public void setApplicationPane(ApplicationPane applicationPane) {
        this.applicationPane = applicationPane;
    }
}
