package com.ztory.lib.sleek;

import com.ztory.lib.sleek.base.SVframeRate;
import com.ztory.lib.sleek.base.SleekFrameRate;
import com.ztory.lib.sleek.layout.SL;

/**
 * Created by jonruna on 2017-04-06.
 */
public class SleekTestActivityUtil {

    public static void addTestUIbasicToSleekCanvas(SleekCanvas sleekCanvas) {

        SleekFrameRate frameRate = new SleekFrameRate(0xff0000ff);
        frameRate.setSleekBounds(100, 100, 200, 80);
        frameRate.getLayout()
                .x(SL.X.POS_CENTER, 0, null)
                .y(SL.Y.PERCENT_CANVAS, -100, null, 1.0f);
        sleekCanvas.addSleek(frameRate);

        sleekCanvas.addSleek(new SVframeRate(false));
    }

    public static void removeUIfromSleekCanvas(SleekCanvas sleekCanvas) {
        sleekCanvas.removeAllSleek(false);
        sleekCanvas.removeAllSleek(true);
    }

}
