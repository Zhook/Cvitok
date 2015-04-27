package net.vc9ufi.cvitok.data2.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorServiceExt extends ThreadPoolExecutor {

    private AtomicInteger count = new AtomicInteger(0);

    public ExecutorServiceExt(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ExecutorServiceExt(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    @Override
    public void execute(final Runnable command) {
        count.incrementAndGet();
        super.execute(new Runnable() {
            @Override
            public void run() {
                command.run();
                postExecute();
            }
        });
    }

    private void postExecute() {
        if (count.decrementAndGet() == 0) {
            allFinished();
        }
    }

    public void allFinished() {

    }

}
