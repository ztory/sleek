package com.ztory.lib.sleek;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jonruna on 2017-06-16.
 */
public class SleekPrioCounter {
  private static final AtomicInteger prioAtomInt = new AtomicInteger(0);
  public static int current() {
    return prioAtomInt.get();
  }
  public static int next() {
    return prioAtomInt.incrementAndGet();
  }
}
