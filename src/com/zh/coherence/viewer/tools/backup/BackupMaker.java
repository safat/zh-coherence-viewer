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
import java.util.*;

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
        List<NamedCache> caches = new ArrayList<NamedCache>();
        NamedCache nCache;
        int maxElements = 0;

        Enumeration<String> cachesEnumeration = (Enumeration<String>) context.getCaches().elements();
        while(cachesEnumeration.hasMoreElements()){
            nCache = CacheFactory.getCache(cachesEnumeration.nextElement());
            //todo check if cache doesn't exist
            caches.add(nCache);
            maxElements += nCache.size();
        }
        context.generalProgress.setMinimum(0);
        context.generalProgress.setMaximum(maxElements);
        context.generalProgress.setValue(0);
        context.updateGeneralProgress();

        for(NamedCache cache : caches){
            context.cacheProgress.setMinimum(0);
            context.cacheProgress.setMaximum(cache.size());
            context.cacheProgress.setValue(0);
            context.updateCacheProgress(cache.getCacheName());
            //store file

            if(cache instanceof SafeNamedCache){
                SafeNamedCache snc = (SafeNamedCache) cache;
                try {
                    Method method = snc.getClass().getDeclaredMethod("getRunningNamedCache");
                    method.setAccessible(true);
                    RemoteNamedCache store = (RemoteNamedCache) method.invoke(snc);
                    store.setPassThrough(true);

                    Set<Map.Entry<Binary,Binary>> entries = store.entrySet();
                    String name = cache.getCacheName();
                    RandomAccessFile file = new RandomAccessFile(new File(context.getPath()
                            + File.separator + name), "rw");
                    WrapperBufferOutput buf = new WrapperBufferOutput(file);
                    buf.writePackedInt(-28);
                    buf.writePackedInt(cache.size());

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
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                throw new RuntimeException("cannot save class: " + cache.getClass());
            }
        }

    }
}
