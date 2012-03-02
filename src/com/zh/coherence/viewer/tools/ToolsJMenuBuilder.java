package com.zh.coherence.viewer.tools;

import com.zh.coherence.viewer.ApplicationMainPane;
import com.zh.coherence.viewer.menubar.CreateToolAction;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 0:51
 */
public class ToolsJMenuBuilder {
    public JMenu buildMenu(ApplicationMainPane mainPane){
        JMenu menu = new JMenu("Tools");
        //load tools list from config/tools.list file
        for(CoherenceViewerToolCreator creator : getToolCreatorsList()){
            menu.add(new CreateToolAction(creator));
        }
        
        return menu;
    }

    private List<CoherenceViewerToolCreator> getToolCreatorsList(){
        List<CoherenceViewerToolCreator> list = new ArrayList<CoherenceViewerToolCreator>();
        File toolsListFile = new File("config/tools.list");
        if(toolsListFile == null || !toolsListFile.exists()){
            throw new IllegalStateException("property file 'config/tools.list' doesn't exist");
        }
        try{
            FileInputStream fis = new FileInputStream(toolsListFile);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            Class clazz;
            Object className;
            Object toolName;
            CoherenceViewerToolCreator creator;
            for(Map.Entry entry : properties.entrySet()){
                className = entry.getValue();
                if(className == null || className.toString().isEmpty()){
                    //todo log error message
                    System.err.println("tool [name: " + entry.getKey() + ", value: "
                            + entry.getValue() + "] couldn't be loaded");
                }
                toolName = entry.getKey();
                if(toolName == null || toolName.toString().isEmpty()){
                    //todo log error message
                    System.err.println("tool [name: " + entry.getKey() + ", value: "
                            + entry.getValue() + "] couldn't be loaded");
                }
                try{
                    clazz = Class.forName(className.toString());
                    creator = (CoherenceViewerToolCreator) clazz.newInstance();
                    creator.setToolName(toolName.toString());
                    list.add(creator);
                }catch(Exception ex){
                    ex.printStackTrace();
                    //todo log error message
                    System.err.println("tool [name: " + entry.getKey() + ", value: "
                            + entry.getValue() + "] couldn't be loaded, class not found");
                }
            }

        }catch(IOException ex){
            ex.printStackTrace();
            throw new IllegalStateException("I couldn't load work tools");
        }
        return list;
    }
}
