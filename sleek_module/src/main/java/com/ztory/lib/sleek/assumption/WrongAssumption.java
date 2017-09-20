package com.ztory.lib.sleek.assumption;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jonruna on 2017-09-20.
 */
public class WrongAssumption<T> implements Assumption<T> {

  protected final Exception exception;

  public WrongAssumption(Exception theException) {
    exception = theException;
  }

  @Override
  public boolean isCorrect() {
    return false;
  }

  @Override
  public boolean isSet() {
    return false;
  }

  @Override
  public T getSafe() {
    return null;
  }

  @Override
  public Exception getException() throws InterruptedException {
    return exception;
  }

  @Override
  public Assumption<T> correct(Assumeable<T> onCorrect) {
    return this;
  }

  @Override
  public Assumption<T> wrong(AssumeableException onWrong) {
    if (onWrong != null) {
      onWrong.assumeException(exception);
    }
    return this;
  }

  @Override
  public Assumption<T> done(Assumed<T> onDone) {
    if (onDone != null) {
      onDone.assumed(this);
    }
    return this;
  }

  @Override
  public boolean cancel(boolean b) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return true;
  }

  @Override
  public T get() throws InterruptedException, ExecutionException {
    throw new ExecutionException(exception);
  }

  @Override
  public T get(long l, TimeUnit timeUnit)
      throws InterruptedException, ExecutionException, TimeoutException {
    throw new ExecutionException(exception);
  }
}
