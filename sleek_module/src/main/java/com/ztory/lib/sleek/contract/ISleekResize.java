package com.ztory.lib.sleek.contract;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;

/**
 * Interface used for callbacks regarding resize of a Sleek instance.
 * Created by jonruna on 09/10/14.
 */
public interface ISleekResize {
    void onResize(Sleek sleek, SleekCanvasInfo info);
}
