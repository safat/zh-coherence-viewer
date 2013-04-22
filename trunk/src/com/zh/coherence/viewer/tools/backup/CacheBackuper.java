package com.zh.coherence.viewer.tools.backup;

import com.tangosol.coherence.component.net.extend.RemoteNamedCache;
import com.tangosol.io.WrapperBufferOutput;
import com.tangosol.util.Binary;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.ProviderException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class CacheBackuper {
    private CacheWrapper wrapper;
    private BackupContext context;
    private File file;
    private ConnectionThreadPool taskExecutor;
    private boolean gatherStatistic = true;

    private SingleUseQueue keys;
    private int unitsBuffer = -1;
    private int backupedSize = 0;
    private RemoteNamedCache store;
    private DataOutputStream outputStream;

    private Timer timer;
    private volatile long networkStatistic = 0;

    public CacheBackuper(CacheWrapper wrapper, File file, ConnectionThreadPool taskExecutor, BackupContext context) {
        this.wrapper = wrapper;
        this.file = file;
        this.taskExecutor = taskExecutor;
        this.context = context;

        Runtime.getRuntime().gc();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        int capacity = (int) (heapFreeSize / ((long) context.getBufferSize() * 1024 * 10));
        System.out.println("Queue capacity: " + capacity);
    }

    public int getExtractedKeysSize(){
        if(keys != null){
            return keys.size();
        }
        return 0;
    }

    public int getPersistedEntriesSize(){
        return backupedSize;
    }

    public int getUnitsBufferSize(){
        return unitsBuffer;
    }

    public File getFile() {
        return file;
    }

    public void backup() throws Exception{
        try{
            initState();
            List<Callable<Integer>> callableList = new ArrayList<Callable<Integer>>();
            for(int i = 0; i < taskExecutor.getCorePoolSize(); i++){
                callableList.add(new BackupTask());
            }
            List<Future<Integer>> tasksFutures = taskExecutor.invokeAll(callableList);
            analyzeResults(tasksFutures);
        }finally {
            try{
                release();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void setGatherStatistic(boolean gatherStatistic) {
        this.gatherStatistic = gatherStatistic;
    }

    private void analyzeResults(List<Future<Integer>> tasksFutures){
        boolean exception = false;
        for(Future<Integer> future : tasksFutures){
            try{
                backupedSize += future.get();
            }catch (Exception ex){
                ex.printStackTrace();
                exception = true;
            }
        }
        if(exception){
            wrapper.info.setStatus(CacheInfo.Status.ERROR);
        }else if(backupedSize < keys.size()){
            wrapper.info.setStatus(CacheInfo.Status.WARN);
        }else{
            wrapper.info.setStatus(CacheInfo.Status.PROCESSED);
        }
    }

    private void initState() throws Exception{
        getStorageCache();
        extractKeySet();
        calcUnitBufferSize();
        initOutputStream();
        initCacheProgressBar();
    }

    private void initOutputStream() throws IOException{
        if(keys == null){
            throw new IllegalArgumentException("Keys object should be initialized first.");
        }
        outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath())));
        outputStream.writeInt(keys.size());
    }

    private void release() throws IOException{
        if(store != null){
            store.setPassThrough(false);
            store = null;
        }
        if(keys != null){
            keys.release();
        }
        if(outputStream != null){
            outputStream.close();
        }
        if(timer != null){
            timer.stop();
        }
    }

    private void initCacheProgressBar(){
        if(keys == null){
            throw new IllegalArgumentException("Keys object should be initialized first.");
        }
        if(!gatherStatistic){
            return;
        }
        context.cacheProgress.setMinimum(0);
        context.cacheProgress.setMinimum(keys.size());
        context.cacheProgress.setValue(0);
        context.updateCacheProgress(wrapper.cache.getCacheName());
        timer = new Timer(2000, new ActionListener() {
            private long oldNetworkStatistic = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                long tmp = networkStatistic;
                context.getNetworkChartModel().addValue((int)(tmp - oldNetworkStatistic));
                context.getNetworkChartModel().fireChartChanges();
                oldNetworkStatistic = tmp;
            }
        });
        timer.start();
    }

    private synchronized void updateStatistic(int dataLength, int objectNumber){
        if(!gatherStatistic){
            return;
        }
        context.incrementCacheProgress(wrapper.info.getName(), objectNumber);
        context.incrementGeneralProgress(objectNumber);
        networkStatistic += dataLength;
    }

    private void calcUnitBufferSize() throws IOException{
        if(keys == null && store == null){
            throw new IllegalArgumentException("Keys object should be initialized first.");
        }
        int unitsBuffer = 0;
        int sampleCount = 50;
        int totalBytes = 0;

        Set<Binary> keySubset = new HashSet<Binary>(keys.poll(sampleCount));
        keys.reset();

        Map<Binary, Binary> all = store.getAll(keySubset);
        for(Map.Entry<Binary, Binary> entry : all.entrySet()){
            totalBytes += entry.getKey().toByteArray().length;
            totalBytes += entry.getValue().toByteArray().length;
        }
        if(all.size() > 0){
            unitsBuffer = context.getBufferSize() * 1024 * 1024 / (totalBytes / all.size());
        }else{
            unitsBuffer = 0;
        }
        System.out.println("[" + store.getName() + "] calculated units buffer size: " + unitsBuffer);
        this.unitsBuffer = unitsBuffer;
    }

    private void extractKeySet() throws Exception{
        if(store == null){
            throw new IllegalArgumentException("Keys object should be initialized first.");
        }
        Set<Binary> cacheKeySet;
        if(wrapper.info.getFilter() != null && wrapper.info.getFilter().isEnabled()){
            FilterExecutor executor = new FilterExecutor();
            cacheKeySet = store.keySet(executor.execute(wrapper.info.getFilter()));
        }else{
            cacheKeySet = store.keySet();
        }
        keys = new SingleUseQueue();
        keys.populate(cacheKeySet);
    }

    private void getStorageCache() throws Exception{
        store = BackupMaker.getRemoteCache(wrapper.cache);
        if(store != null){
            store.setPassThrough(true);
        }else{
            throw new ProviderException("Sorry, cache: " + wrapper.info.getName() + " has unsupported type.");
        }
    }

    private List<Binary> next() throws IOException{
        if(keys != null){
            return keys.poll(unitsBuffer);
        }
        return null;
    }

    private synchronized int write(Map<Binary, Binary> map) throws IOException{
        int dataLength = 0;
        for(Map.Entry<Binary, Binary> entry : map.entrySet()){
            byte[] keyArray = entry.getKey().toByteArray();
            dataLength += keyArray.length;
            byte[] valueArray = entry.getValue().toByteArray();
            dataLength += valueArray.length;
            outputStream.writeInt(keyArray.length);
            outputStream.write(21);
            outputStream.write(keyArray, 1, keyArray.length - 1);
            outputStream.writeInt(valueArray.length);
            outputStream.write(21);
            outputStream.write(valueArray, 1, valueArray.length - 1);
        }
        updateStatistic(dataLength, map.size());
        return dataLength;
    }

    private class BackupTask implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            int progress = 0;
            List<Binary> list;
            RemoteNamedCache store = null;
            int maxDataSize = 0;
            if(Thread.currentThread() instanceof ConnectionThread){
                ConnectionThread thread = (ConnectionThread) Thread.currentThread();
                store = BackupMaker.getRemoteCache(thread.getCache(CacheBackuper.this.wrapper.cache.getCacheName()));
                store.setPassThrough(true);
            }
            try{
                do{
                    list = CacheBackuper.this.next();
                    if(list != null && store != null){
                        Map<Binary, Binary> all = store.getAll(list);
                        int write = CacheBackuper.this.write(all);
                        if(write > maxDataSize){
                            maxDataSize = write;
                        }
                        progress += all.size();
                    }
                }while (list != null && !list.isEmpty());
            }finally {
                store.setPassThrough(false);
            }
            return progress;
        }
    }
}
