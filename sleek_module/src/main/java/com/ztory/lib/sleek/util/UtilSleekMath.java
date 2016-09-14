package com.ztory.lib.sleek.util;

/**
 * Util functions related to math functionality.
 * Created by jonruna on 09/10/14.
 */
public class UtilSleekMath {

    public static float distance(
            float pointOneX,
            float pointOneY,
            float pointTwoX,
            float pointTwoY
    ) {
        float x = pointOneX - pointTwoX;
        float y = pointOneY - pointTwoY;
        return (float) Math.sqrt((x * x) + (y * y));
    }

    public static float roundFloat(float floatValue) {
        return (float) Math.floor(floatValue + 0.5);
    }

}
