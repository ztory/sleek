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
   * Blocking call for getting the Exception of this Assumption, returns null if no exception was
   * thrown during validation of this Assumption.
   * @return an Exception if one was thrown during validation, null otherwise.
   * @throws InterruptedException if the current thread was interrupted while waiting.
   */
  Exception getException() throws InterruptedException;

  /**
   * Register a listeners that will execute onCorrect if the Assumption is correct or onWrong if
   * the Assumption is wrong of if the onCorrect-listener registered with this method threw an
   * Exception.
   * @param onCorrect callback that will be called if the Assumption is correct after validating.
   * @param onWrong callback that will be called if the Assumption is wrong after validating,
   * or if the onResult-callback threw an Exception.
   * @return this Assumption instance.
   */
  Assumption<T> validated(Assumeable<T> onCorrect, FailedAssumeable onWrong);

  /**
   * Register a listener that will be executed when the Assumption is done validating and have
   * called all its onCorrect and onWrong listeners. The Assumption may be correct or wrong when
   * this callback is called.
   * @param onDone callback that will be called when the Assumption is done validating.
   * @return this Assumption instance.
   */
  Assumption<T> validated(Assumeable<Assumption<T>> onDone);

}
