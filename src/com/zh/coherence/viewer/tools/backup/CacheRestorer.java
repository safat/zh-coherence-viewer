package com.zh.coherence.viewer.tools.backup;

import java.awt.geom.IllegalPathStateException;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.tangosol.io.Serializer;
import com.tangosol.io.WrapperBufferInput;
import com.tangosol.io.pof.PofBufferReader;
import com.tangosol.io.pof.PofContext;
import com.tangosol.io.pof.PofInputStream;
import com.tangosol.net.CacheService;
import com.tangosol.net.NamedCache;
import com.tangosol.util.filter.EntryFilter;

public class CacheRestorer {

    private File file;
    private BackupContext context;
    private int extractedMapSize = -1;
    private int processedEntries = 0;
    private int itemBufferSize = -1;
    private ConnectionThreadPool persistingTasksExecutor;
    private CacheWrapper wrapper;
    private Exception exception;

    public CacheRestorer(CacheWrapper wrapper, File file, ConnectionThreadPool persistingTasksExecutor, BackupContext context) {
        this.wrapper = wrapper;
        this.file = file;
        this.persistingTasksExecutor = persistingTasksExecutor;
        this.context = context;
    }

    public void restoreCache() throws IOException {
        init();
        NamedCache cache = wrapper.cache;
        PofInputStream inPof = null;
        FlushMap map = null;

        try {
            CacheService service = cache.getCacheService();
            Serializer serializer = service.getSerializer();
            if ((serializer instanceof PofContext)) {
                EntryFilter filter = getEntryFilter();
                WrapperBufferInput in = new WrapperBufferInput(new DataInputStream(new BufferedInputStream(new FileInputStream(file))));
                inPof = new PofInputStream(new PofBufferReader(in, (PofContext) serializer));
                map = new FlushMap(itemBufferSize, cache.getCacheName(), filter, persistingTasksExecutor, extractedMapSize);
                map.setContext(context);
                inPof.getPofReader().readMap(0, map);
            }
        } catch (Exception ex) {
            exception = ex;
            ex.printStackTrace();
        } finally {
            if (inPof != null) {
                try {
                    inPof.close();
                } catch (IOException e) {
                }
            }
            if (map != null) {
                map.syncFlush();
            }
        }
        afterRestore();

    }

    private void init() throws IOException {
        extractMapSize();
        calcItemBufferSize();
        initCacheProgressBar(extractedMapSize);
        persistingTasksExecutor.resetProgress();
    }

    private void afterRestore() {
        int cacheSize = wrapper.cache.size();
        if (exception != null) {
            wrapper.info.setStatus(CacheInfo.Status.ERROR);
        } else if (extractedMapSize != cacheSize) {
            wrapper.info.setStatus(CacheInfo.Status.WARN);
        } else {
            wrapper.info.setStatus(CacheInfo.Status.PROCESSED);
        }
        this.processedEntries = (int) persistingTasksExecutor.getProgress();
        persistingTasksExecutor.resetProgress();
    }

    private void initCacheProgressBar(int upperBound) {
        context.cacheProgress.setMinimum(0);
        context.cacheProgress.setMaximum(upperBound);
        context.cacheProgress.setValue(0);
        context.updateCacheProgress(wrapper.cache.getCacheName());
    }

    private void extractMapSize() throws IOException {
        WrapperBufferInput in = null;
        try {
            in = new WrapperBufferInput(new DataInputStream(new BufferedInputStream(new FileInputStream(file))));
            in.readPackedInt(); // skip Map's type id
            extractedMapSize = in.readPackedInt();
            System.out.println("[" + wrapper.cache.getCacheName() + "] Extracted from file map size : " + extractedMapSize);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private void calcItemBufferSize() {
        if (extractedMapSize < 0) {
            throw new IllegalPathStateException("Extract size should be first.");
        }
        int memoryBufferSize = context.getBufferSize() * 1024;
        long fileSize = file.length();
        itemBufferSize = (int) ((long) memoryBufferSize * (long) extractedMapSize / fileSize);
        System.out.println("[" + wrapper.cache.getCacheName() + "] Calculated item buffer size: " + itemBufferSize);
    }

    private EntryFilter getEntryFilter() throws Exception {
        EntryFilter filter = null;
        if (wrapper.info.getFilter() != null && wrapper.info.getFilter().isEnabled()) {
            FilterExecutor executor = new FilterExecutor();
            Object res = executor.execute(wrapper.info.getFilter());
            if (res instanceof EntryFilter) {
                filter = (EntryFilter) res;
            }
        }
        return filter;
    }

    public int getExtractedMapSize() {
        return extractedMapSize;
    }

    public int getProcessedEntries() {
        return processedEntries;
    }

}
