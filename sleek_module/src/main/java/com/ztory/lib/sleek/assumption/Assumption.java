package com.ztory.lib.sleek.assumption;

import java.util.concurrent.Future;

/**
 * Created by jonruna on 2017-06-24.
 */
public interface Assumption<T> extends Future<T> {

  /**
   * @return true if no exception was thrown during validation of this Assumption, false otherwise.
   */
  boolean isCorrect();

  /**
   * @return true if calling get() returns a non-null value, false otherwise.
   */
  boolean isSet();

  /**
   * @return true if calling get() returns null, false otherwise.
   */
  boolean isNull();

  /**
   * Blocking call for getting the Exception of this Assumption, returns null if no exception was
   * thrown during validation of this Assumption.
   * @return an Exception if one was thrown during validation, null otherwise.
   * @throws InterruptedException if the current thread was interrupted while waiting
   */
  Exception getException() throws InterruptedException;

  /**
   * Register a listener that will be executed if the Assumption is correct after validating.
   * @param onResult callback that will be called if the Assumption is correct after validating.
   * @return this Assumption instance
   */
  Assumption<T> correct(Assumeable<T> onResult);

  /**
   * Register a listener that will be executed if the Assumption is wrong after validating.
   * @param onError callback that will be called if the Assumption is wrong after validating.
   * @return this Assumption instance
   */
  Assumption<T> wrong(Assumeable<Exception> onError);

  /**
   * Register a listener that will be executed when the Assumption is done validating.
   * @param onDone callback that will be called when the Assumption is done validating.
   * @return this Assumption instance
   */
  Assumption<T> done(Assumeable<Assumption<T>> onDone);

}
