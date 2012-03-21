package com.zh.coherence.viewer.tools.backup;

import com.tangosol.io.Serializer;
import com.tangosol.io.WrapperBufferInput;
import com.tangosol.io.pof.PofBufferReader;
import com.tangosol.io.pof.PofContext;
import com.tangosol.io.pof.PofInputStream;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.CacheService;
import com.tangosol.net.NamedCache;

import javax.swing.*;
import java.io.DataInput;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 21.03.12
 * Time: 20:58
 */
public class RestoreMaker {
    private BackupContext context;
    private JComponent parent;

    public RestoreMaker(BackupContext context, JComponent parent) {
        this.context = context;
        this.parent = parent;
    }

    public void make() {
        File file = new File(context.getPath());
        if (!file.exists() || file.isFile()) {
            JOptionPane.showMessageDialog(parent, "File not found.");
            return;
        }

        File[] fileList = file.listFiles();

        NamedCache cache;
        RandomAccessFile raf;
        context.generalProgress.setMinimum(0);
        context.generalProgress.setMaximum(fileList.length);
        context.generalProgress.setValue(0);
        context.updateGeneralProgress();

        String cacheName;
        for (File f : fileList) {
            cacheName = f.getName();
            cache = CacheFactory.getCache(cacheName);
            context.cacheProgress.setMinimum(0);
            context.cacheProgress.setValue(0);

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
                    inPof.getPofReader().readMap(0, cache);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            context.incrementGeneralProgress();
        }
    }
}
