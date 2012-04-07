package com.zh.coherence.viewer.tools.backup;

import com.tangosol.coherence.component.net.extend.RemoteNamedCache;
import com.tangosol.coherence.component.util.SafeNamedCache;
import com.tangosol.io.WrapperBufferOutput;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Binary;

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

    public void make(){
        long globalTime = System.currentTimeMillis();
        context.logPane.addMessage(new BackupLogEvent(
                System.currentTimeMillis(), "", System.currentTimeMillis(), "Start task", "backup"));
        List<CacheWrapper> caches = new ArrayList<CacheWrapper>();
        NamedCache nCache;
        int maxElements = 0;

        for(BackupTableModel.CacheInfo info : context.getBackupTableModel().getCacheInfoList()){
            if(info.enabled){
                nCache = CacheFactory.getCache(info.name);
                caches.add(new CacheWrapper(nCache, info));
                maxElements += nCache.size();
            }
        }
        context.generalProgress.setMinimum(0);
        context.generalProgress.setMaximum(maxElements);
        context.generalProgress.setValue(0);
        context.updateGeneralProgress();

        for(CacheWrapper wrapper : caches){
            long startTime = System.currentTimeMillis();
            context.cacheProgress.setMinimum(0);
            context.cacheProgress.setMaximum(wrapper.cache.size());
            context.cacheProgress.setValue(0);
            context.updateCacheProgress(wrapper.cache.getCacheName());
            //store file

            if(wrapper.cache instanceof SafeNamedCache){
                SafeNamedCache snc = (SafeNamedCache) wrapper.cache;
                try {
                    Method method = snc.getClass().getDeclaredMethod("getRunningNamedCache");
                    method.setAccessible(true);
                    RemoteNamedCache store = (RemoteNamedCache) method.invoke(snc);
                    store.setPassThrough(true);

                    Set<Map.Entry<Binary,Binary>> entries = store.entrySet();
                    String name = wrapper.cache.getCacheName();
                    RandomAccessFile file = new RandomAccessFile(new File(context.getPath()
                            + File.separator + name), "rw");
                    WrapperBufferOutput buf = new WrapperBufferOutput(file);
                    buf.writePackedInt(-28);
                    buf.writePackedInt(wrapper.cache.size());

                    byte[] array;
                    for(Map.Entry<Binary,Binary> entry : entries){
                        context.incrementCacheProgress(name);
                        context.incrementGeneralProgress();
                        array = entry.getKey().toByteArray();
                        buf.write(array, 1, array.length - 1);
                        array = entry.getValue().toByteArray();
                        buf.write(array, 1, array.length - 1);
                    }
                    store.setPassThrough(false);
                    buf.flush();
                    buf.close();
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                throw new RuntimeException("cannot save class: " + wrapper.cache.getClass());
            }
            wrapper.info.processed = true;
            context.getBackupTableModel().refresh(wrapper.info);

            context.logPane.addMessage(new BackupLogEvent(
                    startTime,wrapper.info.name , System.currentTimeMillis(),
                    "Done, cache has been saved", "backup"));
        }
        context.logPane.addMessage(new BackupLogEvent(
                globalTime,"" , System.currentTimeMillis(), "Done", "Task has been finished"));
    }
}
