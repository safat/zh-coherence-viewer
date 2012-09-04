package com.zh.coherence.viewer.tools.backup;

import com.tangosol.coherence.component.net.extend.RemoteNamedCache;
import com.tangosol.coherence.component.util.SafeNamedCache;
import com.tangosol.io.WrapperBufferOutput;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Binary;
import com.tangosol.util.Filter;
import com.tangosol.util.aggregator.Count;
import com.zh.coherence.viewer.utils.FileUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.*;

public class BackupMaker {
    private BackupContext context;
    private AbstractTableModel tableModel;

    public BackupMaker(BackupContext context, AbstractTableModel tableModel) {
        this.context = context;
        this.tableModel = tableModel;
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

        ExecutorService threadExecutor = Executors.newFixedThreadPool(context.getThreads());
        for (final CacheWrapper wrapper : caches) {
            final long startTime = System.currentTimeMillis();
            final CacheHolder cacheHolder = new CacheHolder(wrapper);
            //multi threading

            List<Callable<Boolean>> callableList = new ArrayList<Callable<Boolean>>();
            for (int i = 0; i < context.getThreads(); i++) {
                callableList.add(new Task(cacheHolder));
            }

            try {
                threadExecutor.invokeAll(callableList);
                new SwingWorker(){
                    @Override
                    protected Object doInBackground() throws Exception {
                        cacheHolder.close();
                        wrapper.info.setProcessed(true);
                        tableModel.fireTableDataChanged();
                        return null;
                    }
                }.execute();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.logPane.addMessage(new BackupLogEvent(
                    startTime, wrapper.info.getName(), System.currentTimeMillis(),
                    "Done, cache has been saved, size of file: "
                            + FileUtils.convertToStringRepresentation(cacheHolder.getFile().length()), "backup"));
        }
        threadExecutor.shutdown();
        context.logPane.addMessage(new BackupLogEvent(
                globalTime, "", System.currentTimeMillis(), "Done", "Task has been finished"));
    }

    private class CacheHolder {
        private File file;
        private List<Object> keys;
        private RandomAccessFile raf;
        private WrapperBufferOutput buf;
        private int cursor = 0;
        private int units;
        private RemoteNamedCache store;
        private BlockingQueue queue;

        private CacheWrapper wrapper;
        private boolean closed = false;

        private CacheHolder(CacheWrapper wrapper) {
            try {
                this.wrapper = wrapper;
                file = new File(context.getPath() + File.separator + wrapper.info.getName());
                raf = new RandomAccessFile(file, "rw");
                buf = new WrapperBufferOutput(raf);
                queue = new LinkedBlockingQueue();

                if (wrapper.cache instanceof SafeNamedCache) {
                    SafeNamedCache snc = (SafeNamedCache) wrapper.cache;
                    Method method = snc.getClass().getDeclaredMethod("getRunningNamedCache");
                    method.setAccessible(true);
                    store = (RemoteNamedCache) method.invoke(snc);
                    store.setPassThrough(true);
                    //get keys
                    if (wrapper.info.getFilter() != null && wrapper.info.getFilter().isEnabled()) {
                        FilterExecutor executor = new FilterExecutor();
                        keys = new ArrayList<Object>(store.keySet(executor.execute(wrapper.info.getFilter())));
                    } else {
                        keys = new ArrayList<Object>(store.keySet());
                    }

                    //create header
                    buf.writePackedInt(-28);
                    buf.writePackedInt(keys.size());
                    context.updateCacheProgress("loading the keys of cache [" + wrapper.info.getName() + "]...");
                    units = context.getBufferSize();
                    context.cacheProgress.setMinimum(0);
                    context.cacheProgress.setMaximum(keys.size());
                    context.cacheProgress.setValue(0);
                    context.updateCacheProgress(wrapper.cache.getCacheName());
                } else {
                    JOptionPane.showMessageDialog(null, "Sorry, cache: " + wrapper.info.getName() + "have unsupported type");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public synchronized List<Object> next() {
            if (cursor == keys.size()) {
                return null;
            }

            int end = cursor + units;
            if (end > keys.size()) {
                end = keys.size();
            }

            List<Object> ret = keys.subList(cursor, end);

            cursor = end;

            return ret;
        }

        public synchronized void write(Map map) {
            try {
                for (Object entryObj : map.entrySet()) {
                    Map.Entry<Binary, Binary> entry = (Map.Entry<Binary, Binary>) entryObj;
                    byte[] array = entry.getKey().toByteArray();
                    buf.write(array, 1, array.length - 1);
                    array = entry.getValue().toByteArray();
                    buf.write(array, 1, array.length - 1);
                }
                buf.flush();
                context.incrementCacheProgress(wrapper.info.getName(), map.size());
                context.incrementGeneralProgress(map.size());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void close() {
            try {
                store.setPassThrough(false);
                buf.close();
                raf.close();
                closed = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void finalize() {
            if (!closed) {
                close();
            }
        }

        public CacheWrapper getWrapper() {
            return wrapper;
        }

        public File getFile() {
            return file;
        }

        public RemoteNamedCache getStore() {
            return store;
        }
    }

    private class Task implements Callable<Boolean> {
        private CacheHolder cacheHolder;

        private Task(CacheHolder cacheHolder) {
            this.cacheHolder = cacheHolder;
        }

        @Override
        public Boolean call() throws Exception {
            List<Object> list;
            do {
                list = cacheHolder.next();
                if (list != null) {
                    cacheHolder.write(cacheHolder.getStore().getAll(list));
                }
            } while (list != null);

            return Boolean.TRUE;
        }
    }
}
