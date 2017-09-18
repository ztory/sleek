package com.ztory.lib.sleek.assumption;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jonruna on 2017-06-24.
 */
public class SimpleAssumption<P, R> implements Assumption<R>, Runnable {

  protected final Future<P> paramFuture;
  protected final List<Assumeable<R>> correctListeners = new ArrayList<>();
  protected final List<AssumeableException> wrongListeners = new ArrayList<>();
  protected final List<Assumed<R>> doneListeners = new ArrayList<>();
  protected final CountDownLatch countDownLatch = new CountDownLatch(1);
  protected final Executor executor;
  protected final Func<P, R> function;
  protected volatile boolean runCalled = false, done = false, assumptionCorrect = false;
  protected volatile R assumptionResult = null;
  protected volatile Exception assumptionException = null;

  public SimpleAssumption(
      Executor theExecutor,
      Future<P> theParamFuture,
      Func<P, R> theFunction
  ) {
    if (theFunction == null) {
      throw new IllegalArgumentException("theFunction == null");
    }
    executor = theExecutor;
    paramFuture = theParamFuture;
    function = theFunction;
    validate();
  }

  protected void validate() {
    if (executor != null) {
      executor.execute(this);
    } else {
      run();
    }
  }

  @Override
  public void run() {
    if (runCalled) {
      return;
    }
    runCalled = true;
    R result = null;
    Exception exception = null;
    try {
      if (paramFuture != null) {
        result = function.invoke(paramFuture.get());
      } else {
        result = function.invoke(null);
      }
    } catch (Exception e) {
      exception = e;
    }
    setResult(result, exception);
  }

  protected void setResult(R result, Exception e) {
    if (done) {
      return;
    }
    synchronized (this) {
      assumptionResult = result;
      assumptionException = e;
      assumptionCorrect = assumptionException == null;
      done = true;
      countDownLatch.countDown();

      if (assumptionCorrect) {
        for (Assumeable<R> correctListener : correctListeners) {
          correctListener.assume(assumptionResult);
        }
      } else {
        for (AssumeableException wrongListener : wrongListeners) {
          wrongListener.assumeException(assumptionException);
        }
      }

      for (Assumed<R> doneAssumable : doneListeners) {
        doneAssumable.assumed(this);
      }

      correctListeners.clear();
      wrongListeners.clear();
      doneListeners.clear();
    }
  }

  @Override
  public boolean isCorrect() {
    return assumptionCorrect;
  }

  @Override
  public boolean isSet() {
    return assumptionResult != null;
  }

  @Override
  public R getSafe() {
    return assumptionResult;
  }

  @Override
  public Exception getException() throws InterruptedException {
    countDownLatch.await();
    return assumptionException;
  }

  @Override
  public Assumption<R> correct(Assumeable<R> onCorrect) {
    if (onCorrect == null) {
      return this;
    }

    if (done) {
      if (assumptionCorrect) {
        onCorrect.assume(assumptionResult);
      }
    } else {
      synchronized (this) {
        if (done) {
          if (assumptionCorrect) {
            onCorrect.assume(assumptionResult);
          }
        } else {
          correctListeners.add(onCorrect);
        }
      }
    }
    return this;
  }

  @Override
  public Assumption<R> wrong(AssumeableException onWrong) {
    if (onWrong == null) {
      return this;
    }

    if (done) {
      if (!assumptionCorrect) {
        onWrong.assumeException(assumptionException);
      }
    } else {
      synchronized (this) {
        if (done) {
          if (!assumptionCorrect) {
            onWrong.assumeException(assumptionException);
          }
        } else {
          wrongListeners.add(onWrong);
        }
      }
    }
    return this;
  }

  @Override
  public Assumption<R> done(Assumed<R> onDone) {
    if (onDone == null) {
      return this;
    }

    if (done) {
      onDone.assumed(this);
    } else {
      synchronized (this) {
        if (done) {
          onDone.assumed(this);
        } else {
          doneListeners.add(onDone);
        }
      }
    }
    return this;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return done;
  }

  @Override
  public R get() throws InterruptedException, ExecutionException {

    countDownLatch.await();

    if (assumptionException != null) {
      throw new ExecutionException(assumptionException);
    } else {
      return assumptionResult;
    }
  }

  @Override
  public R get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {

    countDownLatch.await(timeout, unit);

    if (assumptionException != null) {
      throw new ExecutionException(assumptionException);
    } else {
      return assumptionResult;
    }
  }

}
