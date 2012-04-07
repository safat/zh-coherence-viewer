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
import java.util.ArrayList;
import java.util.List;

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
        final String path = context.getPath();
        File file = new File(path);
        if (!file.exists() || file.isFile()) {
            JOptionPane.showMessageDialog(parent, "Directory '" + context.getPath() + "' not found.");
            return;
        }

        List<CacheWrapper> caches = new ArrayList<CacheWrapper>();

        NamedCache nCache;
        for (BackupTableModel.CacheInfo info : context.getBackupTableModel().getCacheInfoList()) {
            if (info.enabled) {
                nCache = CacheFactory.getCache(info.name);
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
//        private BackupContext context;
        private String path;

        private RestoreThread(List<CacheWrapper> caches, String path) {
            this.caches = caches;
//            this.context = context;
            this.path = path;
        }

        @Override
        public void run() {
            for (final CacheWrapper wrapper : caches) {
                restoreCache(wrapper, path);
                wrapper.info.processed = true;
                context.getBackupTableModel().refresh(wrapper.info);
            }
        }
    }

    private void restoreCache(CacheWrapper wrapper, String path) {
        NamedCache cache;
        RandomAccessFile raf;
        File f;
        cache = wrapper.cache;
        context.cacheProgress.setMinimum(0);
        context.cacheProgress.setValue(0);

        f = new File(path + File.separator + wrapper.info.name);
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
