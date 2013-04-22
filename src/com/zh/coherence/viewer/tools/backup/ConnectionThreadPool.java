package com.zh.coherence.viewer.tools.backup;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import com.zh.coherence.viewer.utils.connection.ExtendConnectionBase;

public class ConnectionThreadPool extends ThreadPoolExecutor {
    private final Semaphore semaphore;

    public ConnectionThreadPool(int nThreads, ThreadFactory threadFactory, int queueCapacity) {
        super(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
        this.semaphore = queueCapacity == Integer.MAX_VALUE ? null : new Semaphore(queueCapacity);
        System.out.println("Blocking queue capacity: " + queueCapacity);
    }

    public ConnectionThreadPool(int nThreads, ThreadFactory threadFactory) {
        this(nThreads, threadFactory, Integer.MAX_VALUE);
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        if (semaphore != null) {
            try {
                semaphore.acquire();
                try {
                    return super.submit(new Callable<T>() {
                        @Override
                        public T call() throws Exception {
                            try {
                                return task.call();
                            } finally {
                                semaphore.release();
                            }
                        }
                    });
                } catch (RejectedExecutionException ex) {
                    ex.printStackTrace();
                    semaphore.release();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                semaphore.release();
            }
        } else {
            return super.submit(task);
        }

        return null;
    }

    public void terminated() {
        super.terminated();
        ThreadFactory factory = this.getThreadFactory();
        if (factory instanceof ConnectionThreadFactory) {
            ConnectionThreadFactory connectionThreadFactory = (ConnectionThreadFactory) factory;
            for (ExtendConnectionBase connection : connectionThreadFactory.getThreadsConnections()) {
                connection.disconnect();
            }
        }
    }
}
