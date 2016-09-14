package com.ztory.lib.sleek.layout;

import com.ztory.lib.sleek.Sleek;

/**
 * Created by jonruna on 09/10/14.
 */
public class LayW extends LayBounds {

    public static final int
            UNCHANGED = SleekLayout.UNCHANGED,
            ABSOLUTE = SleekLayout.ABSOLUTE,
//            POS_X_WEST_OF = SlkLay.POS_X_WEST_OF,
//            POS_X_EAST_OF = SlkLay.POS_X_EAST_OF,
//            POS_X_PARENT = SlkLay.POS_X_PARENT,
//            POS_Y_NORTH_OF = SlkLay.POS_Y_NORTH_OF,
//            POS_Y_SOUTH_OF = SlkLay.POS_Y_SOUTH_OF,
//            POS_Y_PARENT = SlkLay.POS_Y_PARENT,
//            POS_CENTER = SlkLay.POS_CENTER,
            MATCH_PARENT = SleekLayout.SIZE_FIT_PARENT,
            PERCENT = SleekLayout.PERCENT,
            PERCENT_CANVAS = SleekLayout.PERCENT_CANVAS,
            COMPUTE = SleekLayout.COMPUTE;
//            POS_X_PARENT_LEFT = SlkLay.POS_X_PARENT_LEFT,
//            POS_X_PARENT_RIGHT = SlkLay.POS_X_PARENT_RIGHT,
//            POS_Y_PARENT_TOP = SlkLay.POS_Y_PARENT_TOP,
//            POS_Y_PARENT_BOTTOM = SlkLay.POS_Y_PARENT_BOTTOM,
//            POS_PARENT_PERCENT = SlkLay.POS_PARENT_PERCENT;

    IComputeInt computeInt = null;

    /** Used for SlkLay.COMPUTE layout values. */
    LayW(IComputeInt theComputeInt) {
        mode = SleekLayout.COMPUTE;
        computeInt = theComputeInt;
    }

    /** Used for SlkLay.ABSOLUTE layout values. */
    LayW(int theMargin) {
        mode = SleekLayout.ABSOLUTE;
        margin = theMargin;
    }

    LayW(int theMode, int theMargin, Sleek theParent) {
        mode = theMode;
        margin = theMargin;
        parent = theParent;
    }

    LayW(int theMode, int theMargin, Sleek theParent, float thePercent) {
        mode = theMode;
        margin = theMargin;
        parent = theParent;
        percent = thePercent;
    }

}
