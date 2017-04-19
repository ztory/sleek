package com.ztory.lib.sleek.base.element.image;

import android.os.Build;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jonruna on 2017-04-19.
 */
public class ImageUtil {

    public static final Executor
            EXECUTOR = createExecutor(ImageUtil.class.getName() + "_EXECUTOR", 8);

    public static ThreadPoolExecutor createExecutor(
            final String threadNamePrefix,
            int poolSizeMax
    ) {
        ThreadPoolExecutor returnPool = new ThreadPoolExecutor(
                poolSizeMax,
                poolSizeMax,
                4L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    private final AtomicInteger mCount = new AtomicInteger(1);
                    public Thread newThread(Runnable runnable) {
                        return new Thread(
                                runnable,
                                threadNamePrefix + " #" + mCount.getAndIncrement()
                        );
                    }
                }
        );
        if (Build.VERSION.SDK_INT >= 9) {
            returnPool.allowCoreThreadTimeOut(true);
        }
        return returnPool;
    }

}
