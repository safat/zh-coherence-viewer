package com.zh.coherence.viewer.tools.backup.actions;

import com.tangosol.coherence.component.net.extend.RemoteNamedCache;
import com.tangosol.coherence.component.util.SafeNamedCache;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.ReadonlyNamedCache;
import com.zh.coherence.viewer.tools.backup.*;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class StartAction extends AbstractAction {
    private BackupContext context;
    private AbstractTableModel tableModel;
    private ExecutorService executor;

    public StartAction(BackupContext context, AbstractTableModel tableModel) {
        this.context = context;
        this.tableModel = tableModel;

        putValue(Action.NAME, "Start");
        putValue(Action.SMALL_ICON, new IconLoader("icons/start-icon.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JComponent parent = (JComponent) e.getSource();
        if (context.getPath() == null || context.getPath().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Path to folder cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (parent != null && parent instanceof JButton) {
            parent.setEnabled(false);
        }
        final long time = System.currentTimeMillis();
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    if (context.getAction() == BackupContext.BackupAction.BACKUP) {
                        BackupMaker maker = new BackupMaker(context, tableModel);
                        maker.make();
                    } else {
                        executor = getExecutor();
                        makeRestore();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                if (executor != null) {
                    executor.shutdown();
                    try {
                        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                System.err.println("Time: " + (System.currentTimeMillis() - time));
                if (parent != null && parent instanceof JButton) {
                    parent.setEnabled(true);
                }
            }
        }.execute();
    }

    //make restore
    private void makeRestore() {
        List<CacheWrapper> caches = this.getCachesToRestore();
        context.resetProgress(caches.size() * 100, null);

        for (final CacheWrapper wrapper : caches) {
            RestoreTask restoreTask;
            ExecutorService executorService = this.getExecutor();
            if (executorService instanceof ConnectionThreadPool) {
                restoreTask = new RestoreTask(wrapper.cacheFile, (ConnectionThreadPool) executorService, wrapper.info.getName());
            } else {
                SafeNamedCache safeCache = (SafeNamedCache) wrapper.cache;
                RemoteNamedCache remoteNamedCache = (RemoteNamedCache) safeCache.ensureRunningNamedCache();
                restoreTask = new RestoreTask(wrapper.cacheFile, remoteNamedCache);
            }
            restoreTask.setMemoryBufferSize(context.getBufferSize() * 1024);
            restoreTask.setPersistingTaskCount(context.getThreads());
            long startTime = System.currentTimeMillis();
            try {
                Future<Integer> restoreFuture = executorService.submit(restoreTask);
                int needToRestoreSize = -1;
                for (int i = 1; i >= 0; i = (restoreFuture.isDone() ? i - 1 : i)) {
                    Thread.sleep(1000);
                    int persistingProgress = restoreTask.getPersistingProgress();
                    //update progress bar
                    if (needToRestoreSize < 0 && restoreTask.getSize() >= 0) {
                        needToRestoreSize = restoreTask.getSize();
                        context.resetProgress(restoreTask.getSize(), wrapper.cache.getCacheName());
                    } else if (needToRestoreSize > 0) {
                        context.cacheProgress.setValue(persistingProgress);
                        context.updateCacheProgress(wrapper.cache.getCacheName() + " size: " + needToRestoreSize + "/" + persistingProgress);
                        int value = context.generalProgress.getValue() / 100;
                        context.generalProgress.setValue(value * 100 + (int) Math.rint(100.0 * context.cacheProgress.getPercentComplete()));
                        context.updateGeneralProgress();
                    } else if (needToRestoreSize == 0) {
                        context.resetProgress(1, 1, wrapper.cache.getCacheName());
                        context.updateCacheProgress(wrapper.cache.getCacheName() + " size: " + needToRestoreSize + "/" + persistingProgress);
                        context.incrementGeneralProgress(100);
                        break;
                    }
                }

                int cacheSize = wrapper.cache.size();
                int restoredSize = restoreFuture.get();
                if (restoredSize != cacheSize) {
                    wrapper.info.setStatus(CacheInfo.Status.WARN);
                } else {
                    wrapper.info.setStatus(CacheInfo.Status.PROCESSED);
                }

            } catch (Exception ex) {
                wrapper.info.setStatus(CacheInfo.Status.ERROR);
                ex.printStackTrace();
            }
            context.logPane.addMessage(new BackupLogEvent(startTime, wrapper.info.getName(), System.currentTimeMillis(),
                    "Done, cache has been restored. Number of entries from file: " + restoreTask.getSize() +
                            "; Submitted entries: " + restoreTask.getPersistingProgress() + "; Cache size: " +
                            wrapper.cache.size(), "restore"));
            context.generalProgress.setValue(((context.generalProgress.getValue() - 1) / 100) * 100 + 100);
            context.updateGeneralProgress();
        }
    }

    private List<CacheWrapper> getCachesToRestore() {
        List<CacheWrapper> caches = new ArrayList<>();
        String path = context.getPath();

        NamedCache nCache;
        for (CacheInfo info : context.getCacheInfoList()) {
            if (info.isEnabled()) {
                try {
                    nCache = CacheFactory.getCache(info.getName());
                    File cacheFile = new File(path + File.separator + info.getName() + CacheWrapper.FILE_EXT);
                    caches.add(new CacheWrapper(nCache, info, cacheFile));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    info.setStatus(CacheInfo.Status.ERROR);
                }
            }
        }
        return caches;
    }

    public ExecutorService getExecutor() {
        if (executor == null) {
            String connectionHost = System.getProperty("viewer.connection.host");
            String connectionPort = System.getProperty("viewer.connection.port");
            executor = new ConnectionThreadPool(context.getThreads() + 1,
                    new ConnectionThreadFactory(connectionHost, connectionPort));
        } else {
            executor = Executors.newSingleThreadExecutor();
        }
        return executor;
    }
}
