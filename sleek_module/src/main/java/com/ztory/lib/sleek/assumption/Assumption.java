package com.ztory.lib.sleek.assumption;

import java.util.concurrent.Future;

/**
 * Created by jonruna on 2017-06-24.
 */
public interface Assumption<T> extends Future<T> {

  //Assumption.correct().wrong()
  Assumption<T> validate();
  Exception getException() throws InterruptedException;
  void correct(Assumeable<T> onResult);
  void wrong(Assumeable<Exception> onError);
  void done(Assumeable<Assumption<T>> onDone);
  void more(Assumption assumption);

}
