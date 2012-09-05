package com.zh.coherence.viewer.tools.backup;

import com.tangosol.io.Serializer;
import com.tangosol.io.WrapperBufferInput;
import com.tangosol.io.pof.PofBufferReader;
import com.tangosol.io.pof.PofContext;
import com.tangosol.io.pof.PofInputStream;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.CacheService;
import com.tangosol.net.NamedCache;
import com.tangosol.util.filter.EntryFilter;

import javax.swing.*;
import java.io.DataInput;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RestoreMaker {
    private BackupContext context;
    private JComponent parent;

    private long globalTime;

    public RestoreMaker(BackupContext context, JComponent parent) {
        this.context = context;
        this.parent = parent;
    }

    public void make() {
        globalTime = System.currentTimeMillis();
        final String path = context.getPath();
        context.logPane.addMessage(new BackupLogEvent(
                System.currentTimeMillis(), "Source folder: " + path, System.currentTimeMillis(), "Start task", "restore"));
        context.logPane.addMessage(new BackupLogEvent(System.currentTimeMillis(), "Info", System.currentTimeMillis(),
                "buffer size: " + context.getBufferSize(), "restore"));
        File file = new File(path);
        if (!file.exists() || file.isFile()) {
            JOptionPane.showMessageDialog(parent, "Directory '" + context.getPath() + "' not found.");
            return;
        }

        List<CacheWrapper> caches = new ArrayList<CacheWrapper>();

        NamedCache nCache;
        for (CacheInfo info : context.getCacheInfoList()) {
            if (info.isEnabled()) {
                nCache = CacheFactory.getCache(info.getName());
                caches.add(new CacheWrapper(nCache, info));
            }
        }

        context.generalProgress.setMinimum(0);
        context.generalProgress.setMaximum(caches.size());
        context.generalProgress.setValue(0);
        context.updateGeneralProgress();

        RestoreThread restoreThread = new RestoreThread(caches, path);
        restoreThread.start();
    }

    private class RestoreThread extends Thread {
        private List<CacheWrapper> caches;
        private String path;

        private RestoreThread(List<CacheWrapper> caches, String path) {
            this.caches = caches;
            this.path = path;
        }

        @Override
        public void run() {
            for (final CacheWrapper wrapper : caches) {
                long startTime = System.currentTimeMillis();
                restoreCache(wrapper, path);
                wrapper.info.setProcessed(true);
//                context.getBackupTableModel().refresh(wrapper.info);
                context.logPane.addMessage(new BackupLogEvent(
                        startTime,wrapper.info.getName() , System.currentTimeMillis(),
                        "Done, cache has been restored.", "restore"));
            }
            context.logPane.addMessage(new BackupLogEvent(
                    globalTime,"" , System.currentTimeMillis(), "Done", "Task has been finished"));
        }
    }

    private void restoreCache(CacheWrapper wrapper, String path) {
        NamedCache cache;
        RandomAccessFile raf;
        File f;
        cache = wrapper.cache;
        context.cacheProgress.setMinimum(0);
        context.cacheProgress.setValue(0);

        f = new File(path + File.separator + wrapper.info.getName());
        try {
            raf = new RandomAccessFile(f, "rw");
            DataInput dInput = raf;
            CacheService service = cache.getCacheService();
            Serializer serializer = service.getSerializer();
            if ((serializer instanceof PofContext)) {
                dInput = new PofInputStream(new PofBufferReader(
                        new WrapperBufferInput(dInput), (PofContext) serializer));
            }
            if ((dInput instanceof PofInputStream)) {
                PofInputStream inPof = (PofInputStream) dInput;
                context.updateCacheProgress(cache.getCacheName());
                EntryFilter filter = null;
                if(wrapper.info.getFilter() != null && wrapper.info.getFilter().isEnabled()){
                    FilterExecutor executor = new FilterExecutor();
                    Object res = executor.execute(wrapper.info.getFilter());
                    if(res instanceof EntryFilter){
                        filter = (EntryFilter) res;
                    }
                }
                FlushMap map = new FlushMap(context.getBufferSize(), cache, filter);
                map.setContext(context);
                inPof.getPofReader().readMap(0, map);
                map.flush();
            }
            raf.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        context.incrementGeneralProgress();
    }
}
