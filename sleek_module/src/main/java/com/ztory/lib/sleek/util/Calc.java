package com.ztory.lib.sleek.util;

/**
 * Util functions related to math functionality.
 * Created by jonruna on 09/10/14.
 */
public class Calc {

    public static int divideToInt(float dividend, float divisor) {
        return (int) (dividend / divisor);
    }

    public static float divide(float dividend, float divisor) {
        return dividend / divisor;
    }

    public static int multiplyToInt(float factorOne, float factorTwo) {
        return (int) (factorOne * factorTwo);
    }

    public static float multiply(float factorOne, float factorTwo) {
        return factorOne * factorTwo;
    }

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
