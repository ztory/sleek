package com.ztory.lib.sleek.assumption;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jonruna on 2017-06-24.
 */
public class SimpleAssumption<T> implements Assumption<T> {

  protected final List<Assumeable<T>> correctListeners = new ArrayList<>();
  protected final List<Assumeable<Exception>> wrongListeners = new ArrayList<>();
  protected final List<Assumeable<Assumption<T>>> doneListeners = new ArrayList<>();
  protected final List<Assumption> chainAssumptionList = new ArrayList<>();
  protected final CountDownLatch countDownLatch = new CountDownLatch(1);
  protected final Executor executor;
  protected final AssumptionResolver<T> assumptionResolver;
  protected volatile boolean validateStarted = false, done = false, assumptionCorrect = false;
  protected volatile T assumptionResult = null;
  protected volatile Exception assumptionException = null;

  public SimpleAssumption(Executor theExecutor, final AssumptionResolver<T> theAssumptionResolver) {

    if (theAssumptionResolver == null) {
      throw new IllegalArgumentException("theAssumptionResolver == null");
    }

    executor = theExecutor;
    assumptionResolver = theAssumptionResolver;
  }

  protected void setResult(T result, Exception e) {
    synchronized (this) {
      assumptionResult = result;
      assumptionException = e;
      assumptionCorrect = assumptionException == null;
      done = true;
      countDownLatch.countDown();

      if (assumptionCorrect) {
        for (Assumeable<T> correctAssumable : correctListeners) {
          correctAssumable.assume(assumptionResult);
        }
      } else {
        for (Assumeable<Exception> wrongAssumable : wrongListeners) {
          wrongAssumable.assume(assumptionException);
        }
      }

      for (Assumeable<Assumption<T>> doneAssumable : doneListeners) {
        doneAssumable.assume(this);
      }

      for (Assumption chainAssumption : chainAssumptionList) {
        chainAssumption.validate();
      }

      correctListeners.clear();
      wrongListeners.clear();
      doneListeners.clear();
      chainAssumptionList.clear();
    }
  }

  @Override
  public Assumption<T> validate() {

    if (validateStarted) {
      return this;
    }

    synchronized (this) {
      if (validateStarted) {
        return this;
      }
      validateStarted = true;
    }

    Runnable resolveRun = new Runnable() {
      @Override
      public void run() {
        T result = null;
        Exception exception = null;
        try {
          result = assumptionResolver.resolve();
        } catch (Exception e) {
          exception = e;
        }
        setResult(result, exception);
      }
    };
    if (executor != null) {
      executor.execute(resolveRun);
    } else {
      resolveRun.run();
    }
    return this;
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
  public boolean isNull() {
    return assumptionResult == null;
  }

  @Override
  public Exception getException() throws InterruptedException {

    countDownLatch.await();

    return assumptionException;
  }

  @Override
  public Assumption<T> correct(Assumeable<T> onCorrect) {

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
  public Assumption<T> wrong(Assumeable<Exception> onWrong) {

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
  public Assumption<T> done(Assumeable<Assumption<T>> onDone) {

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
  public Assumption<T> next(Assumption assumption) {

    if (assumption == null) {
      return this;
    }

    synchronized (this) {
      if (!done) {
        chainAssumptionList.add(assumption);
      } else {
        assumption.validate();
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
  public T get() throws InterruptedException, ExecutionException {

    countDownLatch.await();

    if (assumptionException != null) {
      throw new ExecutionException(assumptionException);
    } else {
      return assumptionResult;
    }
  }

  @Override
  public T get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {

    countDownLatch.await(timeout, unit);

    if (assumptionException != null) {
      throw new ExecutionException(assumptionException);
    } else {
      return assumptionResult;
    }
  }
}
