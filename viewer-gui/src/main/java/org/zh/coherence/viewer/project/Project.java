package org.zh.coherence.viewer.project;

import java.io.File;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 22.01.13
 * Time: 20:51
 */
public class Project {
    public static final String PROJECT_PATH_TO_LIBRARY_DIRECTORIES = "path.to.libraries";

    public File path;

    public String name;

    public String template = null;

    public Map<String, String> properties;

    public Project(File path) {
        this.path = path;
    }
}
