package com.ztory.lib.sleek.contract;

import com.ztory.lib.sleek.Sleek;

/**
 * Interface used to produce data, will pass in the related Sleek instance as the parameter so that
 * it is easy to execute additional actions on the Sleek instance inside of the callback.
 * Created by jonruna on 13/09/16.
 */
public interface ISleekData<T> {
    T getData(Sleek sleek);
}
