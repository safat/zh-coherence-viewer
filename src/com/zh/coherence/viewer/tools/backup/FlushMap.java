package com.zh.coherence.viewer.tools.backup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.tangosol.util.filter.EntryFilter;

public class FlushMap extends HashMap<Object, Object> {
    private static final long serialVersionUID = 1L;
    private int bufferSize;
    private String cacheName;
    private BackupContext context;
    private EntryFilter filter;
    private ConnectionThreadPool executor;
    private Long fullSize;
    private long flushTime = 0;

    public FlushMap(int bufferSize, String cacheName, EntryFilter filter, ConnectionThreadPool persistingTaskExecutor, long size) {
        this.bufferSize = bufferSize;
        this.cacheName = cacheName;
        this.filter = filter;
        this.executor = persistingTaskExecutor;
        this.fullSize = size;
    }

    @Override
    public Object put(Object key, Object value) {
        boolean accept = true;
        if (filter != null) {
            Map.Entry<Object, Object> entry = new SimpleEntry<Object, Object>(key, value);
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
        Map<Object, Object> map = new HashMap<Object, Object>(this);
        executor.submit(new MapPersisterTask(map, cacheName));
        if (System.currentTimeMillis() - flushTime > 1000) {
            updateProgressBar();
            flushTime = System.currentTimeMillis();
        }
        clear();
    }

    public void syncFlush() {
        flush();
        awaitAllTasksDone();
    }

    public void awaitAllTasksDone() {
        long time = System.currentTimeMillis();
        BlockingQueue<Runnable> queue = executor.getQueue();
        System.out.println("[" + cacheName + "] Waiting for all submitted tasks are done... (pending tasks: " + queue.size() + ")");
        while (queue.size() != 0 || executor.getActiveCount() > 0) {
            try {
                Thread.sleep(1000);
                updateProgressBar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + cacheName + "] All sumbited tasks are done. Time waited: " + (System.currentTimeMillis() - time) + " ms.");
    }

    public void setContext(BackupContext context) {
        this.context = context;
    }

    public void updateProgressBar() {
        if (context != null && executor instanceof ConnectionThreadPool) {
            int cacheProgress = (int) ((ConnectionThreadPool) executor).getProgress();
            context.cacheProgress.setValue(cacheProgress);
            context.updateCacheProgress(cacheName + " size: " + this.fullSize + "/" + cacheProgress);
            int value = context.generalProgress.getValue() / 100;
            context.generalProgress.setValue(value * 100 + (int) Math.rint(100.0 * context.cacheProgress.getPercentComplete()));
            context.updateGeneralProgress();
        }
    }
}
