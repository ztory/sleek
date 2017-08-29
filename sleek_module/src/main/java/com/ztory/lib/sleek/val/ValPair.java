package com.ztory.lib.sleek.val;

/**
 * Created by jonruna on 2017-08-29.
 */
public class ValPair<T1, T2> {
  public final T1 valueOne;
  public final T2 valueTwo;
  public ValPair(T1 valOne, T2 valTwo) {
    valueOne = valOne;
    valueTwo = valTwo;
  }
}
