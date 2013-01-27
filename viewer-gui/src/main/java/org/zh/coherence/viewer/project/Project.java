package org.zh.coherence.viewer.project;

import java.io.File;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 22.01.13
 * Time: 20:51
 */
public class Project {
    public File path;

    public String name;

    public String coherenceConfig;

    public String[] pathToLibraries;

    public Project(File path) {
        this.path = path;
    }
}
