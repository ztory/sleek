package com.ztory.lib.sleek.util;

import android.view.MotionEvent;

import com.ztory.lib.sleek.Sleek;

/**
 * Util functions related to touch functionality.
 * Created by jonruna on 09/10/14.
 */
public class UtilSleekTouch {

    public static boolean isTouchInside(MotionEvent event, Sleek view) {
        return isTouchInside(event.getX(), event.getY(), view);
    }

    public static boolean isTouchInside(float eventX, float eventY, Sleek view) {
        if (
                eventX < view.getSleekX() ||
                eventX > view.getSleekX() + view.getSleekW() ||
                eventY < view.getSleekY() ||
                eventY > view.getSleekY() + view.getSleekH()
                ) {
            return false;
        }
        return true;
    }

    public static boolean isTouchInside(
            float eventX,
            float eventY,
            float viewX,
            float viewY,
            float viewW,
            float viewH
            ) {
        if (
                eventX < viewX ||
                eventX > viewX + viewW ||
                eventY < viewY ||
                eventY > viewY + viewH
                ) {
            return false;
        }
        return true;
    }

    public static boolean isTouchDown(MotionEvent event) {
        return event.getActionMasked() == MotionEvent.ACTION_DOWN;
    }

    public static boolean isTouchUp(MotionEvent event) {
        return event.getActionMasked() == MotionEvent.ACTION_UP;
    }

    public static boolean isTouchCancel(MotionEvent event) {
        return event.getActionMasked() == MotionEvent.ACTION_CANCEL;
    }

}
