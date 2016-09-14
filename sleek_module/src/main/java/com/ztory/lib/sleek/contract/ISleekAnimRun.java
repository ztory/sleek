package com.ztory.lib.sleek.contract;

import com.ztory.lib.sleek.SleekCanvasInfo;

/**
 * A run interface that passes in a SleekCanvasInfo as the parameter. Useful for callbacks from
 * SleekCanvas or Sleek onDraw methods.
 * Created by jonruna on 09/10/14.
 */
public interface ISleekAnimRun {
    void run(SleekCanvasInfo info);
}
