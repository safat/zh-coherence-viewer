package com.zh.coherence.viewer.tools.backup;

import com.tangosol.coherence.component.net.extend.RemoteNamedCache;
import com.tangosol.coherence.component.util.SafeNamedCache;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.aggregator.Count;
import com.zh.coherence.viewer.utils.FileUtils;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BackupMaker {
    private BackupContext context;

    public BackupMaker(BackupContext context, AbstractTableModel tableModel) {
        this.context = context;
    }

    public void make() {
        long globalTime = System.currentTimeMillis();
        context.logPane.addMessage(new BackupLogEvent(
                System.currentTimeMillis(), "Target folder: " + context.getPath(), System.currentTimeMillis(), "Start task", "backup"));
        context.logPane.addMessage(new BackupLogEvent(System.currentTimeMillis(), "Info", System.currentTimeMillis(),
                "buffer size: " + context.getBufferSize(), "backup"));

        List<CacheWrapper> caches = new ArrayList<CacheWrapper>();
        NamedCache nCache;
        int maxElements = 0;

        FilterExecutor executor = new FilterExecutor();
        for (CacheInfo info : context.getCacheInfoList()) {
            if (info.isEnabled()) {
                nCache = CacheFactory.getCache(info.getName());
                caches.add(new CacheWrapper(nCache, info));
                if (info.getFilter() != null && info.getFilter().isEnabled()) {
                    try {
                        Filter filter = executor.execute(info.getFilter());
                        Integer count = (Integer) nCache.aggregate(filter, new Count());
                        maxElements += count;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    maxElements += nCache.size();
                }
            }
        }
        context.generalProgress.setMinimum(0);
        context.generalProgress.setMaximum(maxElements);
        context.generalProgress.setValue(0);
        context.updateGeneralProgress();

        String connectionHost = System.getProperty("viewer.connection.host");
        String connectionPort = System.getProperty("viewer.connection.port");
        final ConnectionThreadPool threadExecutor = new ConnectionThreadPool(context.getThreads(),
                new ConnectionThreadFactory(connectionHost, connectionPort));

        for (final CacheWrapper wrapper : caches) {
            context.cacheProgress.setString("[" + wrapper.cache.getCacheName() + "] - analyzing cache");
            threadExecutor.resetProgress();
            final long startTime = System.currentTimeMillis();
            File file = new File(context.getPath() + File.separator + wrapper.info.getName());
            CacheBackuper backuper = new CacheBackuper(wrapper, file, threadExecutor, context);
            try{
                backuper.backup();
            } catch (Exception ex){
                ex.printStackTrace();
                wrapper.info.setStatus(CacheInfo.Status.ERROR);
            }

            context.logPane.addMessage(new BackupLogEvent(
                    startTime, wrapper.info.getName(), System.currentTimeMillis(),
                    "Done, cache has been saved, size of file: "
                            + FileUtils.convertToStringRepresentation(backuper.getFile().length())
                            + "; Entries written: " + backuper.getPersistedEntriesSize()
                            + "; Entries keys extracted: " + backuper.getExtractedKeysSize(), "backup"));
        }
        threadExecutor.shutdown();
        context.logPane.addMessage(new BackupLogEvent(
                globalTime, "", System.currentTimeMillis(), "Done", "Task has been finished"));
    }

    public static RemoteNamedCache getRemoteCache(NamedCache namedCache) throws Exception{
        if(namedCache instanceof SafeNamedCache){
            SafeNamedCache snc = (SafeNamedCache) namedCache;
            Method method = snc.getClass().getDeclaredMethod("getRunningNamedCache");
            method.setAccessible(true);
            return (RemoteNamedCache) method.invoke(snc);
        }
        return null;
    }


}
