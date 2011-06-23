package ua.com.zhivko.coherence.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.util.Observable;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * @author Живко
 */
public class ProjectManager extends Observable{
    public static ProjectManager manager = null;

    public static ProjectManager getInstance(){
        if(manager == null){
            manager = new ProjectManager();
        }
        return manager;
    }

    public Properties currentProj = null;
    public String pathToProject = null;

    public void openProject(File path){
        try{
            Properties prop = new Properties();
            prop.load(new FileInputStream(path));

            if(currentProj != null){
                int ret = JOptionPane.showConfirmDialog(null,
                        "Are you really want to close current project: "
                        .concat((String) currentProj.get("project.name"))
                        .concat("?"));
                if(ret != JOptionPane.OK_OPTION){
                    return;
                }else{
                    //@todo close current project
                }
            }

            currentProj = prop;
            pathToProject = path.getPath();
            System.err.println("notify");
            this.setChanged();
            this.notifyObservers();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void createProject(){
        //@todo implement it
    }
}
