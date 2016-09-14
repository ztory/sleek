package com.ztory.lib.sleek.layout;

import com.ztory.lib.sleek.Sleek;

/**
 * Created by jonruna on 09/10/14.
 */
public class LayY extends LayBounds {

    public static final int
            UNCHANGED = SleekLayout.UNCHANGED,
            ABSOLUTE = SleekLayout.ABSOLUTE,
//            POS_X_WEST_OF = SlkLay.POS_X_WEST_OF,
//            POS_X_EAST_OF = SlkLay.POS_X_EAST_OF,
//            POS_X_PARENT = SlkLay.POS_X_PARENT,
            NORTH_OF = SleekLayout.POS_Y_NORTH_OF,
            SOUTH_OF = SleekLayout.POS_Y_SOUTH_OF,
            PARENT = SleekLayout.POS_Y_PARENT,
            POS_CENTER = SleekLayout.POS_CENTER,
//            SIZE_FIT_PARENT = SlkLay.SIZE_FIT_PARENT,
            PERCENT = SleekLayout.PERCENT,
            PERCENT_CANVAS = SleekLayout.PERCENT_CANVAS,
            COMPUTE = SleekLayout.COMPUTE,
//            POS_X_PARENT_LEFT = SlkLay.POS_X_PARENT_LEFT,
//            POS_X_PARENT_RIGHT = SlkLay.POS_X_PARENT_RIGHT,
            PARENT_TOP = SleekLayout.POS_Y_PARENT_TOP,
            PARENT_BOTTOM = SleekLayout.POS_Y_PARENT_BOTTOM,
            PARENT_PERCENT = SleekLayout.POS_PARENT_PERCENT;

    IComputeFloat computeFloat = null;

    /** Used for SlkLay.COMPUTE layout values. */
    LayY(IComputeFloat theComputeFloat) {
        mode = SleekLayout.COMPUTE;
        computeFloat = theComputeFloat;
    }

    /** Used for SlkLay.ABSOLUTE layout values. */
    LayY(int theMargin) {
        mode = SleekLayout.ABSOLUTE;
        margin = theMargin;
    }

    LayY(int theMode, int theMargin, Sleek theParent) {
        mode = theMode;
        margin = theMargin;
        parent = theParent;
    }

    LayY(int theMode, int theMargin, Sleek theParent, float thePercent) {
        mode = theMode;
        margin = theMargin;
        parent = theParent;
        percent = thePercent;
    }

}
