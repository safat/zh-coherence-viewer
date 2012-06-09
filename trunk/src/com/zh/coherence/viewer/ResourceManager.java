package com.zh.coherence.viewer;

import com.zh.coherence.viewer.menubar.FileJMenuBuilder;
import com.zh.coherence.viewer.menubar.HelpJMenuBuilder;
import com.zh.coherence.viewer.tools.ToolManager;

import javax.swing.*;

public class ResourceManager {
    private ToolManager toolManager;

    private ApplicationPane applicationPane;

    JMenuBar mainMenuBar;

    private ResourceManager() {
        applicationPane = new ApplicationPane();
    }

    private void initMenuBar(){
        mainMenuBar = new JMenuBar();

        mainMenuBar.add(new FileJMenuBuilder().buildMenu());
        mainMenuBar.add(getToolManager().getMenu());
        mainMenuBar.add(new HelpJMenuBuilder().buildMenu());
    }

    public JMenuBar getMenuBar() {
        if(mainMenuBar == null){
            initMenuBar();
        }
        return mainMenuBar;
    }

    public ApplicationPane getApplicationPane() {
        return applicationPane;
    }

    public void setApplicationPane(ApplicationPane applicationPane) {
        this.applicationPane = applicationPane;
    }

    public ToolManager getToolManager() {
        return toolManager;
    }

    public void setToolManager(ToolManager toolManager) {
        this.toolManager = toolManager;
    }
}
