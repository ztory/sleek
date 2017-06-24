package com.ztory.lib.sleek.assumption;

/**
 * Created by jonruna on 2017-06-24.
 */
public interface AssumptionResolver<T> {
  T resolve() throws Exception;
}
