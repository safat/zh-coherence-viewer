package com.zh.coherence.viewer.tools.backup;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.zh.coherence.viewer.utils.connection.ExtendConnectionBase;

public class ConnectionThreadPool extends ThreadPoolExecutor {
    private AtomicLong progress;

    public ConnectionThreadPool(int nThreads, ThreadFactory threadFactory) {
        super(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
        this.progress = new AtomicLong(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (r instanceof FutureTask) {
            FutureTask<Integer> futureTask = (FutureTask<Integer>) r;
            try {
                Integer progressUpdate = futureTask.get();
                this.progress.addAndGet(progressUpdate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void terminated() {
        super.terminated();
        ThreadFactory factory = this.getThreadFactory();
        if (factory instanceof ConnectionThreadFactory) {
            ConnectionThreadFactory connFactory = (ConnectionThreadFactory) factory;
            for (ExtendConnectionBase connection : connFactory.getThreadsConnections()) {
                connection.disconnect();
            }
        }
    }

    public long getProgress() {
        return progress.get();
    }

    public void resetProgress() {
        progress.set(0);
    }

}
