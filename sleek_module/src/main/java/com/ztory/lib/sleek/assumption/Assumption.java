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
   * Non-blocking call for getting the result of this Assumption, returns null if Assumption is not
   * yet validated, not correct or if the correct value is set to null.
   * @return the same value that get() returns, but in cases where get() would throw an
   * Exception this method returns null instead, also returns null if Assumption is not validated.
   */
  T getSafe();

  /**
   * Blocking call for getting the Exception of this Assumption, returns null if no exception was
   * thrown during validation of this Assumption.
   * @return an Exception if one was thrown during validation, null otherwise.
   * @throws InterruptedException if the current thread was interrupted while waiting.
   */
  Exception getException() throws InterruptedException;

  /**
   * Register a listener that will be executed if the Assumption is correct after validating.
   * @param onCorrect callback that will be called if the Assumption is correct after validating.
   * @return this Assumption instance.
   */
  Assumption<T> correct(Assumeable<T> onCorrect);

  /**
   * Register a listener that will be executed if the Assumption is wrong after validating.
   * @param onWrong callback that will be called if the Assumption is wrong after validating.
   * @return this Assumption instance.
   */
  Assumption<T> wrong(AssumeableException onWrong);

  /**
   * Register a listener that will be executed when the Assumption is done validating and have
   * called all its onCorrect and onWrong listeners. The Assumption may be correct or wrong when
   * this callback is called.
   * @param onDone callback that will be called when the Assumption is done validating.
   * @return this Assumption instance.
   */
  Assumption<T> done(Assumed<T> onDone);

}
