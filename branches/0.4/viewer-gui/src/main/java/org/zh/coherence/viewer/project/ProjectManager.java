package org.zh.coherence.viewer.project;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 22.01.13
 * Time: 20:44
 */
public class ProjectManager {

    private static final Log LOGGER = LogFactory.getLog(ProjectManager.class);
    public static final String PROJECT_NAME = "project.name";


    private File projectDirectory;

    private List<Project> projects = new ArrayList<>();

    /**
     * @param data module data directory
     */
    public ProjectManager(File data) {
        projectDirectory = new File(data, "project");
        if (!projectDirectory.exists()) {
            projectDirectory.mkdir();
        }
    }

    private void init() {
        for (File f : projectDirectory.listFiles()) {
            if (f.isFile()) {
                projects.add(loadProject(f));
            } else {
                LOGGER.warn("Viewer doesn't support nested project folders, skip folder: " + f.getAbsolutePath());
            }
        }
    }

    private Project loadProject(File path) {
        Project project = null;
        try (InputStream is = new FileInputStream(path)) {
            project = new Project(path);
            Properties properties = new Properties();
            properties.load(is);
            project.name = properties.getProperty(PROJECT_NAME);
            project.properties = new HashMap<>();
            for (Object key : properties.keySet()) {
                project.properties.put((String) key, (String) properties.get(key));
            }
        } catch (Exception ex) {
            LOGGER.error("Couldn't load project file: " + path.getAbsolutePath(), ex);
        }

        return project;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
