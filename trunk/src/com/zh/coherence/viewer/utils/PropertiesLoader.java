package com.zh.coherence.viewer.utils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 07.03.12
 * Time: 0:41
 */
public class PropertiesLoader {
    private String path;

    public PropertiesLoader(String path) {
        this.path = path;
    }

    public Properties load(){
        try{
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(path);
            properties.load(fis);
            fis.close();
            return properties;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
