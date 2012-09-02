package com.zh.coherence.viewer.tools.backup;

import com.tangosol.coherence.component.net.extend.RemoteNamedCache;
import com.tangosol.coherence.component.util.SafeNamedCache;
import com.tangosol.io.WrapperBufferOutput;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Binary;
import com.zh.coherence.viewer.utils.FileUtils;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.03.12
 * Time: 23:36
 */
public class BackupMaker {
    BackupContext context;


    public BackupMaker(BackupContext context) {
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

        for (CacheInfo info : context.getCacheInfoList()) {
            if (info.isEnabled()) {
                nCache = CacheFactory.getCache(info.getName());
                caches.add(new CacheWrapper(nCache, info));
                maxElements += nCache.size();
            }
        }
        context.generalProgress.setMinimum(0);
        context.generalProgress.setMaximum(maxElements);
        context.generalProgress.setValue(0);
        context.updateGeneralProgress();

        for (CacheWrapper wrapper : caches) {
            long startTime = System.currentTimeMillis();
            context.cacheProgress.setMinimum(0);
            context.cacheProgress.setMaximum(wrapper.cache.size());
            context.cacheProgress.setValue(0);
            context.updateCacheProgress(wrapper.cache.getCacheName());
            //store file
            File target = new File(context.getPath() + File.separator + wrapper.info.getName());
            if (wrapper.cache instanceof SafeNamedCache) {
                SafeNamedCache snc = (SafeNamedCache) wrapper.cache;
                try {
                    Method method = snc.getClass().getDeclaredMethod("getRunningNamedCache");
                    method.setAccessible(true);
                    RemoteNamedCache store = (RemoteNamedCache) method.invoke(snc);
                    store.setPassThrough(true);

                    int bufferSize = context.getBufferSize();
                    RandomAccessFile file = new RandomAccessFile(target, "rw");
                    WrapperBufferOutput buf = new WrapperBufferOutput(file);
                    buf.writePackedInt(-28);
                    buf.writePackedInt(wrapper.cache.size());
                    context.updateCacheProgress("loading the keys of cache [" + wrapper.info.getName() + "]...");
                    Set keys = store.keySet();
                    List keysPack = new ArrayList();
                    for(Object key : keys){
                        keysPack.add(key);
                        if(keysPack.size() == bufferSize){
                            flushData(wrapper, store.getAll(keysPack), buf);
                            keysPack.clear();
                        }
                    }
                    flushData(wrapper, store.getAll(keysPack), buf);
                    store.setPassThrough(false);
                    buf.close();
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("cannot save class: " + wrapper.cache.getClass());
            }
            wrapper.info.setProcessed(true);
//            context.getBackupTableModel().refresh(wrapper.info);

            context.logPane.addMessage(new BackupLogEvent(
                    startTime, wrapper.info.getName(), System.currentTimeMillis(),
                    "Done, cache has been saved, size of file: "
                            + FileUtils.convertToStringRepresentation(target.length()), "backup"));
        }
        context.logPane.addMessage(new BackupLogEvent(
                globalTime, "", System.currentTimeMillis(), "Done", "Task has been finished"));
    }

    private void flushData(CacheWrapper wrapper, Map map, WrapperBufferOutput buf) throws Exception {
        context.incrementCacheProgress(wrapper.info.getName(), map.size());
        context.incrementGeneralProgress(map.size());
        for(Object entryObj : map.entrySet()){
            Map.Entry<Binary, Binary> entry = (Map.Entry<Binary, Binary>) entryObj;
            byte[] array = entry.getKey().toByteArray();
            buf.write(array, 1, array.length - 1);
            array = entry.getValue().toByteArray();
            buf.write(array, 1, array.length - 1);
        }
        buf.flush();
    }
}
