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
  protected final List<Assumeable<Exception>> wrongListeners = new ArrayList<>();
  protected final List<Assumeable<Assumption<R>>> doneListeners = new ArrayList<>();
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
    synchronized (this) {
      assumptionResult = result;
      assumptionException = e;
      assumptionCorrect = assumptionException == null;
      done = true;
      countDownLatch.countDown();

      if (assumptionCorrect) {
        for (Assumeable<R> correctAssumable : correctListeners) {
          correctAssumable.assume(assumptionResult);
        }
      } else {
        for (Assumeable<Exception> wrongAssumable : wrongListeners) {
          wrongAssumable.assume(assumptionException);
        }
      }

      for (Assumeable<Assumption<R>> doneAssumable : doneListeners) {
        doneAssumable.assume(this);
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
  public Exception getException() throws InterruptedException {
    countDownLatch.await();
    return assumptionException;
  }

  @Override
  public Assumption<R> correct(Assumeable<R> onCorrect) {

    if (onCorrect == null) {
      return this;
    }

    synchronized (this) {
      if (!done) {
        correctListeners.add(onCorrect);
      } else if (assumptionCorrect) {
        onCorrect.assume(assumptionResult);
      }
    }

    return this;
  }

  @Override
  public Assumption<R> wrong(Assumeable<Exception> onWrong) {

    if (onWrong == null) {
      return this;
    }

    synchronized (this) {
      if (!done) {
        wrongListeners.add(onWrong);
      } else if (!assumptionCorrect) {
        onWrong.assume(assumptionException);
      }
    }

    return this;
  }

  @Override
  public Assumption<R> done(Assumeable<Assumption<R>> onDone) {

    if (onDone == null) {
      return this;
    }

    synchronized (this) {
      if (!done) {
        doneListeners.add(onDone);
      } else {
        onDone.assume(this);
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
