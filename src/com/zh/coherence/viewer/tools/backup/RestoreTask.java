package com.zh.coherence.viewer.tools.backup;

import com.tangosol.coherence.component.net.extend.RemoteNamedCache;
import com.tangosol.coherence.component.util.SafeNamedCache;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Binary;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.*;

public class RestoreTask implements Callable<Integer> {
    private int memoryBufferSize = 2 * 1024;
    private int persistingTaskCount = 1;

    private File file;
    private ConnectionThreadPool persistingTaskExecutor;
    private RemoteNamedCache remoteNamedCache;

    private volatile int readProgress = 0;
    private volatile int size = -1;

    private String cacheName;
    private LinkedBlockingQueue<Map<Binary, Binary>> mapsToPersist;
    private Set<PersistingDataTask> persistingTasks;
    private Set<Future<Integer>> persistingFuture;

    public RestoreTask(File file, ConnectionThreadPool persistingTaskExecutor, String cacheName) {
        this.file = file;
        this.persistingTaskExecutor = persistingTaskExecutor;
        this.cacheName = cacheName;
        this.mapsToPersist = new LinkedBlockingQueue<>(50);
        this.persistingTasks = new HashSet<>();
    }

    public RestoreTask(File file, RemoteNamedCache remoteNamedCache) {
        this.file = file;
        this.remoteNamedCache = remoteNamedCache;
    }

    @Override
    public Integer call() throws Exception {
        //check state
        if ((persistingTaskExecutor == null || cacheName == null) && remoteNamedCache == null) {
            throw new IllegalArgumentException("(RemoteNamedCache) or (ConnectionThreadPool and CacheName) objects mustn't be null");
        }

        if (file == null || !file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("File to restore should exist. File: " + (file == null ? "null" : file.getAbsolutePath()));
        }
        init();
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file.getAbsolutePath())));
            int cEntries = size = in.readInt();
            Map<Binary, Binary> tmpMap = new HashMap<>();
            int readBytes = 0;
            for (int i = 0; i < cEntries; i++) {
                int keySize = in.readInt();
                byte[] keyByte = new byte[keySize];
                in.readFully(keyByte);
                int valueSize = in.readInt();
                byte[] valueByte = new byte[valueSize];
                in.readFully(valueByte);
                Binary key = new Binary(keyByte);
                Binary value = new Binary(valueByte);
                readBytes += keySize + valueSize + 2 * Integer.SIZE;
                if (readBytes >= this.memoryBufferSize) {
                    tmpMap = flush(tmpMap);
                    readBytes = 0;
                }
                tmpMap.put(key, value);
                readProgress++;
            }
            flush(tmpMap);
            waitForBackGroundTasksDone();
        } catch (Exception ex) {
            terminateBackgroundProcesses();
            throw ex;
        } finally {
            cleanUp();
        }

        return getPersistingProgress();
    }

    private void cleanUp() {
        if (remoteNamedCache != null) {
            remoteNamedCache.setPassThrough(false);
        }
    }

    private void terminateBackgroundProcesses() {
        if (persistingFuture != null) {
            for (Future<Integer> future : persistingFuture) {
                if (!future.isDone()) {
                    future.cancel(true);
                } else {
                    try {
                        future.get();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void waitForBackGroundTasksDone() throws Exception {
        Exception exception = null;
        if (persistingFuture == null) {
            return;
        }
        flush(Collections.EMPTY_MAP);
        for (Future<Integer> future : persistingFuture) {
            try {
                future.get();
            } catch (Exception ex) {
                exception = ex;
                ex.printStackTrace();
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    private void init() {
        if (persistingTaskExecutor != null) {
            persistingFuture = new HashSet<>();
            for (int i = 0; i < persistingTaskCount; i++) {
                PersistingDataTask persistingTask = new PersistingDataTask(cacheName);
                persistingTasks.add(persistingTask);
                persistingFuture.add(persistingTaskExecutor.submit(persistingTask));
            }
        } else {
            remoteNamedCache.setPassThrough(true);
        }
    }

    private Map<Binary, Binary> flush(Map<Binary, Binary> map) throws Exception {
        if (mapsToPersist != null) {
            boolean offer = mapsToPersist.offer(map, 30, TimeUnit.SECONDS);
            if (!offer) {
                throw new TimeoutException("Couldn't flush data during 30 seconds. Terminating the job.");
            }
        } else {
            remoteNamedCache.putAll(map);
        }
        map = new HashMap<>();
        return map;
    }

    public int getReadProgress(){
        return readProgress;
    }

    public int getPersistingProgress(){
        if(persistingTasks != null){
            int persistingProgress = 0;
            for(PersistingDataTask task : persistingTasks){
                persistingProgress += task.getProgress();
            }
            return persistingProgress;
        }else{
            return readProgress;
        }
    }

    public int getSize() {
        return size;
    }

    private class PersistingDataTask implements Callable<Integer>{

        private String cacheName;
        private volatile int entitiesSent = 0;

        private PersistingDataTask(String cacheName) {
            this.cacheName = cacheName;
        }

        @Override
        public Integer call() throws Exception {
            Thread currThread = Thread.currentThread();
            NamedCache threadCache = null;
            entitiesSent = 0;
            if(currThread instanceof ConnectionThread){
                ConnectionThread connThread = (ConnectionThread) currThread;
                threadCache = connThread.getCache(cacheName);
                SafeNamedCache safeCache = (SafeNamedCache) threadCache;
                RemoteNamedCache remoteNamedCache = (RemoteNamedCache) safeCache.ensureRunningNamedCache();
                remoteNamedCache.setPassThrough(true);
                try{
                    while(true){
                        Map<Binary, Binary> poll = mapsToPersist.poll(10, TimeUnit.SECONDS);
                        if(poll.isEmpty()){
                            mapsToPersist.put(poll);
                            break;
                        }
                        remoteNamedCache.putAll(poll);
                        entitiesSent += poll.size();
                    }
                }finally {
                    remoteNamedCache.setPassThrough(false);
                }
                return entitiesSent;
            }else{
                return 0;
            }
        }

        public int getProgress(){
            return entitiesSent;
        }
    }

    public void setMemoryBufferSize(int memoryBufferSize) {
        this.memoryBufferSize = memoryBufferSize;
    }

    public void setPersistingTaskCount(int persistingTaskCount) {
        this.persistingTaskCount = persistingTaskCount;
    }
}
