package com.ztory.lib.sleek.touch;

import android.view.MotionEvent;

import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.Sleek;

/**
 * Interface that represents an onTouch action, passing in all the relevant parameters.
 * Created by jonruna on 09/10/14.
 */
public interface ISleekTouchRun {

    int RETURN_TRUE = 0, RETURN_FALSE = 1, DONT_RETURN = 2;

    int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info);
}
