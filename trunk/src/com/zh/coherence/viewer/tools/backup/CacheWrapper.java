package com.zh.coherence.viewer.tools.backup;

import com.tangosol.net.NamedCache;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 07.04.12
 * Time: 15:27
 */
public class CacheWrapper {
    public NamedCache cache;
    public BackupTableModel.CacheInfo info;

    public CacheWrapper(NamedCache cache, BackupTableModel.CacheInfo info) {
        this.cache = cache;
        this.info = info;
    }
}
