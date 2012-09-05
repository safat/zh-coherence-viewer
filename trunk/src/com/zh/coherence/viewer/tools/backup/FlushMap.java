package com.zh.coherence.viewer.tools.backup;

import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.EntryFilter;

import java.util.HashMap;
import java.util.Map;

public class FlushMap extends HashMap {
    private int bufferSize;
    private NamedCache cache;
    private BackupContext context;
    private EntryFilter filter;

    public FlushMap(int bufferSize, NamedCache cache, EntryFilter filter) {
        this.bufferSize = bufferSize;
        this.cache = cache;
        this.filter = filter;
    }

    @Override
    public Object put(Object key, Object value) {
        boolean accept = true;
        if (filter != null) {
            Map.Entry entry = new SimpleEntry(key, value);
            accept = filter.evaluateEntry(entry);
        }
        if (accept) {
            Object ret = super.put(key, value);
            if (size() == bufferSize) {
                flush();
            }
            return ret;
        } else {
            return null;
        }
    }

    public void flush() {
        cache.putAll(this);
        if (context != null) {
            context.updateCacheProgress(cache.getCacheName() + "/ size: " + cache.size());
        }
        clear();
    }

    public void setContext(BackupContext context) {
        this.context = context;
    }
}
