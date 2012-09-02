package com.zh.coherence.viewer.tools.backup;

import com.tangosol.net.NamedCache;

public class CacheWrapper {
    public NamedCache cache;
    public CacheInfo info;

    public CacheWrapper(NamedCache cache, CacheInfo info) {
        this.cache = cache;
        this.info = info;
    }
}
