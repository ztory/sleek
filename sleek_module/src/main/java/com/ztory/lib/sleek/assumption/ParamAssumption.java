package com.ztory.lib.sleek.assumption;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jonruna on 2017-09-16.
 */
public class ParamAssumption<T> implements Assumption<T> {

  protected final T param;

  public ParamAssumption(T theParam) {
    param = theParam;
  }

  @Override
  public boolean isCorrect() {
    return true;
  }

  @Override
  public boolean isSet() {
    return param != null;
  }

  @Override
  public Exception getException() throws InterruptedException {
    return null;
  }

  @Override
  public Assumption<T> validated(Assumeable<T> onCorrect, FailedAssumeable onWrong) {
    if (onCorrect != null) {
      onCorrect.assume(param);
    }
    return this;
  }

  @Override
  public Assumption<T> validated(Assumeable<Assumption<T>> onDone) {
    if (onDone != null) {
      onDone.assume(this);
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
    return param;
  }

  @Override
  public T get(long l, TimeUnit timeUnit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return param;
  }
}
