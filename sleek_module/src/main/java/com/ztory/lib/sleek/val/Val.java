package com.ztory.lib.sleek.val;

/**
 * Created by jonruna on 2017-06-22.
 */
public class Val<T> {

  public static <T> Val<T> with(T theValue) {
    return new Val<>(theValue);
  }

  private final T value;
  private final boolean nullValue;

  public Val(T theValue) {
    value = theValue;
    nullValue = value == null;
  }

  @Override
  public boolean equals(Object compareObject) {
    if (nullValue) {
      return compareObject == null;
    } else {
      return compareObject instanceof Val && value.equals(((Val) compareObject).get());
    }
  }

  @Override
  public int hashCode() {
    if (nullValue) {
      return 0;
    } else {
      return value.hashCode();
    }
  }

  public final boolean isNull() {
    return nullValue;
  }

  public final boolean isSet() {
    return !nullValue;
  }

  public final T get() {
    return value;
  }

  public final Val<T> ifNull(Runnable action) {
    if (nullValue && action != null) {
      action.run();
    }
    return this;
  }

  public final Val<T> ifSet(ValAction<T> action) {
    if (!nullValue && action != null) {
      action.action(value);
    }
    return this;
  }

}
