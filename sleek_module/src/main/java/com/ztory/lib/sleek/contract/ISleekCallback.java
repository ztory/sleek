package com.ztory.lib.sleek.contract;

import com.ztory.lib.sleek.Sleek;

/**
 * Callback that passes in a Sleek instance as the parameter. Useful for adding listeners to a
 * Sleek instance.
 * Created by jonruna on 13/09/16.
 */
public interface ISleekCallback<T extends Sleek> {
    void sleekCallback(T sleek);
}
