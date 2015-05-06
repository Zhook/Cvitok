package net.vc9ufi.cvitok.data;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

public class ThreadFactoryWithPriority implements ThreadFactory {
    private final int threadPriority;

    public ThreadFactoryWithPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread thread = new Thread(r);
        thread.setPriority(threadPriority);
        return thread;
    }
}
