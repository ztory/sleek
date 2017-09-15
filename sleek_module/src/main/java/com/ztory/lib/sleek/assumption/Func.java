package com.ztory.lib.sleek.assumption;

/**
 * Created by jonruna on 2017-06-24.
 */
public interface Func<P, R> {
  R invoke(P param) throws Exception;
}
