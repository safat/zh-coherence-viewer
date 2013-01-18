package org.zh.coherence.viewer;

import org.java.plugin.boot.Application;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.util.ExtendedProperties;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 18.01.13
 * Time: 22:17
 */
public class CorePlugin extends ApplicationPlugin implements Application {
    @Override
    public Application initApplication(ExtendedProperties extendedProperties, String[] strings) throws Exception {
        return this;
    }

    @Override
    public void doStart() throws Exception {
    }

    @Override
    public void doStop() throws Exception {
    }

    @Override
    public void startApplication() throws Exception {
        System.err.println("START");
    }
}
