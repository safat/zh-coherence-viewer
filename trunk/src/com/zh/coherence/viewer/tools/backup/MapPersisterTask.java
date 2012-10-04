package com.zh.coherence.viewer.tools.backup;

import java.util.Map;
import java.util.concurrent.Callable;

import com.tangosol.net.NamedCache;

/*
 *  Task for submitting map objects to Coherence
 */
public class MapPersisterTask implements Callable<Integer> {
    private Map<Object, Object> map;
    private String cacheName;

    public MapPersisterTask(Map<Object, Object> map, String cacheName) {
        this.map = map;
        this.cacheName = cacheName;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    @Override
    public Integer call() throws Exception {
        Thread currThread = Thread.currentThread();
        NamedCache threadCache = null;
        if (currThread instanceof ConnectionThread) {
            ConnectionThread connThread = (ConnectionThread) currThread;
            threadCache = connThread.getCache(cacheName);
            threadCache.putAll(map);
            return map.size();
        }
        return 0;
    }
}