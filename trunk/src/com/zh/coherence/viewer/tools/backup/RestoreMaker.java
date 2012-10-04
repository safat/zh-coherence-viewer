package com.zh.coherence.viewer.tools.backup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

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
        context.logPane.addMessage(new BackupLogEvent(System.currentTimeMillis(), "Source folder: " + path, System.currentTimeMillis(), "Start task", "restore"));
        context.logPane.addMessage(new BackupLogEvent(System.currentTimeMillis(), "Info", System.currentTimeMillis(), "buffer size: " + context.getBufferSize(), "restore"));
        File file = new File(path);
        if (!file.exists() || file.isFile()) {
            JOptionPane.showMessageDialog(parent, "Directory '" + context.getPath() + "' not found.");
            return;
        }

        List<CacheWrapper> caches = getCachesToRestore();


        context.generalProgress.setMinimum(0);
        context.generalProgress.setMaximum(caches.size() * 100);
        context.generalProgress.setValue(0);
        context.updateGeneralProgress();

        RestoreThread restoreThread = new RestoreThread(caches, path);
        restoreThread.start();
    }

    private List<CacheWrapper> getCachesToRestore() {
        List<CacheWrapper> caches = new ArrayList<CacheWrapper>();
        NamedCache nCache;
        for (CacheInfo info : context.getCacheInfoList()) {
            if (info.isEnabled()) {
                nCache = CacheFactory.getCache(info.getName());
                caches.add(new CacheWrapper(nCache, info));
            }
        }
        return caches;
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
            String connectionHost = System.getProperty("viewer.connection.host");
            String connectionPort = System.getProperty("viewer.connection.port");
            ConnectionThreadPool executor = new ConnectionThreadPool(context.getThreads(), new ConnectionThreadFactory(connectionHost, connectionPort));

            for (final CacheWrapper wrapper : caches) {
                int generalProgress = context.generalProgress.getValue();
                File cacheFile = new File(path + File.separator + wrapper.info.getName());
                CacheRestorer restorer = new CacheRestorer(wrapper, cacheFile, executor, context);
                long startTime = System.currentTimeMillis();
                try {
                    restorer.restoreCache();
                    int cacheSize = wrapper.cache.size();
                    context.logPane.addMessage(new BackupLogEvent(startTime, wrapper.info.getName(), System.currentTimeMillis(), "Done, cache has been restored. Number of entries from file: " + restorer.getExtractedMapSize() + "; Submitted entries: " + restorer.getProcessedEntries() + "; Cache size: " + cacheSize, "restore"));
                } catch (IOException e) {
                    wrapper.info.setStatus(CacheInfo.Status.ERROR);
                    e.printStackTrace();
                }
                context.generalProgress.setValue(generalProgress + 100);
                context.updateGeneralProgress();
//		context.getBackupTableModel().refresh(wrapper.info);
            }

            closeThreadPoolExecutor(executor);
            context.logPane.addMessage(new BackupLogEvent(globalTime, "", System.currentTimeMillis(), "Done", "Task has been finished"));
        }
    }

    private void closeThreadPoolExecutor(ExecutorService executor) {
        System.out.println("Terminating backgound threads...");
        executor.shutdown();
        try {
            long time = System.nanoTime();
            boolean success = executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            System.out.println("Terminated background threads : " + success + ". Time waited:" + (System.nanoTime() - time) + " ns");
            System.out.println("Global time:" + (System.currentTimeMillis() - globalTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
