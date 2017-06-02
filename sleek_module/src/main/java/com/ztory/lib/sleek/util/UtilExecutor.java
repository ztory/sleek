package com.ztory.lib.sleek.util;

import android.os.Build;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class that provides standard Executor instances.
 * This class also contains a factory method for creating custom ThreadPoolExecutor instances.
 * Created by jonruna on 2017-06-02.
 */
public class UtilExecutor {

  public static final Executor
      NETWORK_EXECUTOR = createExecutor(UtilExecutor.class.getName() + "_NETWORK_EXECUTOR", 8),
      CPU_EXECUTOR_MULTI = createExecutor(UtilExecutor.class.getName() + "_CPU_EXECUTOR_MULTI", 4),
      CPU_EXECUTOR_SINGLE = createExecutor(UtilExecutor.class.getName() + "_CPU_EXECUTOR_SINGLE", 1);

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
