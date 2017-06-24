package com.ztory.lib.sleek.assumption;

import java.util.concurrent.Future;

/**
 * Created by jonruna on 2017-06-24.
 */
public interface Assumption<T> extends Future<T> {

  //Assumption.correct().wrong()
  boolean isCorrect();
  Exception getException() throws InterruptedException;
  Assumption<T> validate();
  Assumption<T> correct(Assumeable<T> onResult);
  Assumption<T> wrong(Assumeable<Exception> onError);
  Assumption<T> done(Assumeable<Assumption<T>> onDone);
  Assumption<T> next(Assumption assumption);

}
