package com.ztory.lib.sleek;

import com.ztory.lib.sleek.base.SleekFrameRate;
import com.ztory.lib.sleek.layout.SL;

/**
 * Created by jonruna on 2017-04-06.
 */
public class SleekTestActivityUtil {

    public static void addUIframeRate(SleekCanvas sleekCanvas) {
        SleekFrameRate frameRate = new SleekFrameRate(0xff38B0DE);
        frameRate.getLayout()
                .x(SL.X.POS_CENTER, 0, null)
                .y(SL.Y.PERCENT_CANVAS, -100, null, 1.0f)
                .w(SL.W.ABSOLUTE, 120, null)
                .h(SL.H.ABSOLUTE, 60, null);
        sleekCanvas.addSleek(frameRate);
    }

    public static void removeAllUIfromSleekCanvas(SleekCanvas sleekCanvas) {
        sleekCanvas.removeAllSleek(false);
        sleekCanvas.removeAllSleek(true);
    }

}
