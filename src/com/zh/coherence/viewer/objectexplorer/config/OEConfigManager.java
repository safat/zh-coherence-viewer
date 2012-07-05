package com.zh.coherence.viewer.objectexplorer.config;

import org.jdesktop.swingx.JXErrorPane;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

public class OEConfigManager {
    private static OEConfigManager ourInstance = new OEConfigManager();

    private OEConfigDataKeeper dataKeeper;

    public static OEConfigManager getInstance() {
        return ourInstance;
    }

    private OEConfigManager() {
        loadConfig();
    }

    public OEConfigDataKeeper getDataKeeper() {
        return dataKeeper;
    }

    public void setDataKeeper(OEConfigDataKeeper dataKeeper) {
        this.dataKeeper = dataKeeper;
    }

    public boolean isStopped(Object obj) {
        if (obj == null) {
            return true;
        }
        Class clazz = obj.getClass();
        if (clazz.isPrimitive() && dataKeeper.isPrimitives()) {
            return true;
        }
        if (clazz.isEnum() && dataKeeper.isEnums()) {
            return true;
        }
        if (clazz.isArray() && dataKeeper.isArray()) {
            return true;
        }
        if (clazz.isAnonymousClass() && dataKeeper.isAnonymous()) {
            return true;
        }
        if (clazz.isSynthetic() && dataKeeper.isSynthetic()) {
            return true;
        }

        return dataKeeper.getClasses().contains(clazz);
    }

    protected void loadConfig() {
        try {
            File file = getFile();
            if (file == null || !file.exists()) {
                //load default properties
                dataKeeper = new OEConfigDataKeeper();
                dataKeeper.setAnonymous(true);
                dataKeeper.setEnums(true);
                dataKeeper.setPrimitives(true);
                dataKeeper.setSynthetic(true);
                Set<Class> classes = new HashSet<Class>();
                classes.add(Number.class);
                classes.add(String.class);
                classes.add(Boolean.class);
                classes.add(Timestamp.class);
                classes.add(Date.class);
                classes.add(java.sql.Date.class);
                classes.add(Calendar.class);
                classes.add(Character.class);
                classes.add(Integer.class);
                classes.add(Long.class);
                classes.add(Integer.class);
                classes.add(Float.class);
                classes.add(Double.class);
                dataKeeper.setClasses(classes);
                convertClassesListToString();
                return;
            }
            JAXBContext context = JAXBContext.newInstance(OEConfigDataKeeper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            dataKeeper = (OEConfigDataKeeper) unmarshaller.unmarshal(getFile());
            convertStringListToClasses();
        } catch (Exception ex) {
            JXErrorPane.showDialog(ex);
        }
    }

    protected void saveConfig() {
        if (dataKeeper == null) {
            return;
        }
        //convert list of classes to list of strings
        convertClassesListToString();
        try {
            JAXBContext context = JAXBContext.newInstance(OEConfigDataKeeper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            System.err.println("file saved to: " + getFile());
            m.marshal(dataKeeper, getFile());
        } catch (Exception ex) {
            JXErrorPane.showDialog(ex);
        }
    }

    public void convertStringListToClasses() {
        Set<Class> classes = new HashSet<Class>();
        for (String str : dataKeeper.getLocalClasses()) {
            try {
                Class clazz = Class.forName(str, false, ClassLoader.getSystemClassLoader());
                classes.add(clazz);
            } catch (Exception ex) {
                JXErrorPane.showDialog(ex);
            }
        }
        dataKeeper.setClasses(classes);
    }

    public void convertClassesListToString() {
        List<String> list = new ArrayList<String>();
        for (Class clazz : dataKeeper.getClasses()) {
            list.add(clazz.getName());
        }
        dataKeeper.setLocalClasses(list);
    }

    private File getFile() {
        File home = new File(System.getProperty("user.home") + File.separatorChar + ".zhcv");
        if (!home.exists()) {
            home.mkdir();
        }
        return new File(home, "object-explorer.xml");
    }
}
