package com.zh.coherence.viewer.tools.backup;

import com.tangosol.net.NamedCache;

import java.util.HashMap;

public class FlushMap extends HashMap {
    private int bufferSize;
    private NamedCache cache;
    private BackupContext context;

    public FlushMap(int bufferSize, NamedCache cache) {
        this.bufferSize = bufferSize;
        this.cache = cache;
    }

    @Override
    public Object put(Object key, Object value) {
        Object ret = super.put(key, value);
        if(size() == bufferSize){
            flush();
        }
        return ret;
    }

    public void flush(){
        cache.putAll(this);
        if(context != null){
            context.updateCacheProgress(cache.getCacheName() + "/ size: " + cache.size());
        }
        clear();
    }

    public void setContext(BackupContext context) {
        this.context = context;
    }
}
