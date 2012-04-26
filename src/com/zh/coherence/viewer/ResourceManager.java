package com.zh.coherence.viewer;

import com.zh.coherence.viewer.tableview.user.ObjectViewersContainer;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ResourceManager {
    private final static ResourceManager manager = new ResourceManager();

    private ApplicationPane applicationPane;

    private ObjectViewersContainer viewersContainer;

    public static ResourceManager getInstance(){
        return manager;
    }

    JMenuBar mainMenuBar;

    private ResourceManager() {
        mainMenuBar = new JMenuBar();

        loadViewers();
    }

    private void loadViewers(){
        File file = new File("config/viewers.xml");

        try {
            if (file.exists()) {
                JAXBContext context = JAXBContext.newInstance(ObjectViewersContainer.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                viewersContainer = (ObjectViewersContainer) unmarshaller.unmarshal(file);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        if(viewersContainer == null){
            viewersContainer = new ObjectViewersContainer();
        }
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

    public ObjectViewersContainer getViewersContainer() {
        return viewersContainer;
    }
}
