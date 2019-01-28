package pers.like.framework.main;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Like
 */
public class BaseExecutor {

    private Executor diskIO;
    private Executor networkIO;
    private Executor mainThread;

    public BaseExecutor() {
        diskIO = new DiskThreadExecutor();
        networkIO = new NetworkThreadExecutor();
        mainThread = new MainThreadExecutor();
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private class MainThreadExecutor implements Executor {

        private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mMainThreadHandler.post(runnable);
        }
    }

    private class NetworkThreadExecutor implements Executor {
        private final ThreadPoolExecutor mNetworkThreadExecutor = new ThreadPoolExecutor(8, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new NetworkThreadFactory());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mNetworkThreadExecutor.execute(runnable);
        }

        private class NetworkThreadFactory implements ThreadFactory {
            private static final String THREAD_NAME = "kly_networkIO_thread_";
            private int counter;

            private NetworkThreadFactory() {
                this.counter = 0;
            }

            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                StringBuilder append = new StringBuilder().append(THREAD_NAME);
                int i = this.counter;
                this.counter = i + 1;
                return new Thread(runnable, append.append(i).toString());
            }
        }
    }

    private class DiskThreadExecutor implements Executor {

        private Executor mIOThreadExecutor = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new DiskThreadFactory());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mIOThreadExecutor.execute(runnable);
        }

        private class DiskThreadFactory implements ThreadFactory {
            private static final String THREAD_NAME = "kly_diskIO_thread_";
            private int counter;

            private DiskThreadFactory() {
                this.counter = 0;
            }

            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                StringBuilder append = new StringBuilder().append(THREAD_NAME);
                int i = this.counter;
                this.counter = i + 1;
                return new Thread(runnable, append.append(i).toString());
            }
        }
    }
}
