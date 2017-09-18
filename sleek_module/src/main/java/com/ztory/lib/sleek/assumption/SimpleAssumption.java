package com.ztory.lib.sleek.assumption;

import com.ztory.lib.sleek.val.ValPair;
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
  protected final List<ValPair<Assumeable<R>, FailedAssumeable>>
      validatedListeners = new ArrayList<>();
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

  protected void invokeValidatedListeners(Assumeable<R> onCorrect, FailedAssumeable onWrong) {
    if (assumptionCorrect) {
      try {
        if (onCorrect != null) {
          onCorrect.assume(assumptionResult);
        }
      } catch (Exception listenerException) {
        if (onWrong != null) {
          onWrong.failedAssume(listenerException);
        }
      }
    } else {
      if (onWrong != null) {
        onWrong.failedAssume(assumptionException);
      }
    }
  }

  protected void setResult(R result, Exception e) {
    synchronized (this) {
      assumptionResult = result;
      assumptionException = e;
      assumptionCorrect = assumptionException == null;
      done = true;
      countDownLatch.countDown();

      for (ValPair<Assumeable<R>, FailedAssumeable> validateListener : validatedListeners) {
        invokeValidatedListeners(validateListener.valueOne, validateListener.valueTwo);
      }

      for (Assumeable<Assumption<R>> doneAssumable : doneListeners) {
        doneAssumable.assume(this);
      }

      validatedListeners.clear();
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
  public Assumption<R> validated(Assumeable<R> onCorrect, FailedAssumeable onWrong) {
    if (onCorrect == null && onWrong == null) {
      return this;
    }

    if (done) {
      invokeValidatedListeners(onCorrect, onWrong);
    } else {
      synchronized (this) {
        if (done) {
          invokeValidatedListeners(onCorrect, onWrong);
        } else {
          validatedListeners.add(new ValPair<>(onCorrect, onWrong));
        }
      }
    }
    return this;
  }

  @Override
  public Assumption<R> validated(Assumeable<Assumption<R>> onDone) {
    if (onDone == null) {
      return this;
    }

    if (done) {
      onDone.assume(this);
    } else {
      synchronized (this) {
        if (done) {
          onDone.assume(this);
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
