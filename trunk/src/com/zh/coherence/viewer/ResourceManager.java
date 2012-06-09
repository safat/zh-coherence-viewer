package com.zh.coherence.viewer;

import com.zh.coherence.viewer.menubar.FileJMenuBuilder;
import com.zh.coherence.viewer.menubar.HelpJMenuBuilder;
import com.zh.coherence.viewer.tableview.user.ObjectViewersContainer;
import com.zh.coherence.viewer.tools.ToolManager;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ResourceManager {
    private static ResourceManager manager;

    private ToolManager toolManager;

    private ApplicationPane applicationPane;

    private ObjectViewersContainer viewersContainer;

    public static ResourceManager getInstance() {
        return manager;
    }

    JMenuBar mainMenuBar;

    private ResourceManager() {
        applicationPane = new ApplicationPane();

//        viewersContainer = loadJAXBObject("viewers.xml", ObjectViewersContainer.class);
//        toolContainer = loadJAXBObject("tools.xml", ToolContainer.class);
    }

    private void initMenuBar(){
        mainMenuBar = new JMenuBar();

        mainMenuBar.add(new FileJMenuBuilder().buildMenu());
        mainMenuBar.add(getToolManager().getMenu());
        mainMenuBar.add(new HelpJMenuBuilder().buildMenu());
    }


    private <T> T loadJAXBObject(String xml, Class clazz) {
        File file = new File(this.getClass().getClassLoader().getResource(xml).getFile());
        System.err.println("load file: " + file.getAbsolutePath());
        T ret = null;

        try {
            if (file.exists()) {
                JAXBContext context = JAXBContext.newInstance(clazz);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ret = (T) unmarshaller.unmarshal(file);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return ret;
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

    public ObjectViewersContainer getViewersContainer() {
        return viewersContainer;
    }

    public ToolManager getToolManager() {
        return toolManager;
    }

    public void setToolManager(ToolManager toolManager) {
        this.toolManager = toolManager;
    }
}
