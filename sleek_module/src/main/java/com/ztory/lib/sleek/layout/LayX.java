package com.ztory.lib.sleek.layout;

import com.ztory.lib.sleek.Sleek;

/**
 * Created by jonruna on 09/10/14.
 */
public class LayX extends LayBounds {

    public static final int
            UNCHANGED = SleekLayout.UNCHANGED,
            ABSOLUTE = SleekLayout.ABSOLUTE,
            WEST_OF = SleekLayout.POS_X_WEST_OF,
            EAST_OF = SleekLayout.POS_X_EAST_OF,
            PARENT = SleekLayout.POS_X_PARENT,
//            POS_Y_NORTH_OF = SlkLay.POS_Y_NORTH_OF,
//            POS_Y_SOUTH_OF = SlkLay.POS_Y_SOUTH_OF,
//            POS_Y_PARENT = SlkLay.POS_Y_PARENT,
            POS_CENTER = SleekLayout.POS_CENTER,
//            SIZE_FIT_PARENT = SlkLay.SIZE_FIT_PARENT,
            PERCENT = SleekLayout.PERCENT,
            PERCENT_CANVAS = SleekLayout.PERCENT_CANVAS,
            COMPUTE = SleekLayout.COMPUTE,
            PARENT_LEFT = SleekLayout.POS_X_PARENT_LEFT,
            PARENT_RIGHT = SleekLayout.POS_X_PARENT_RIGHT,
//            POS_Y_PARENT_TOP = SlkLay.POS_Y_PARENT_TOP,
//            POS_Y_PARENT_BOTTOM = SlkLay.POS_Y_PARENT_BOTTOM,
            PARENT_PERCENT = SleekLayout.POS_PARENT_PERCENT;

    IComputeFloat computeFloat = null;

    /** Used for SlkLay.COMPUTE layout values. */
    LayX(IComputeFloat theComputeFloat) {
        mode = SleekLayout.COMPUTE;
        computeFloat = theComputeFloat;
    }

    /** Used for SlkLay.ABSOLUTE layout values. */
    LayX(int theMargin) {
        mode = SleekLayout.ABSOLUTE;
        margin = theMargin;
    }

    LayX(int theMode, int theMargin, Sleek theParent) {
        mode = theMode;
        margin = theMargin;
        parent = theParent;
    }

    LayX(int theMode, int theMargin, Sleek theParent, float thePercent) {
        mode = theMode;
        margin = theMargin;
        parent = theParent;
        percent = thePercent;
    }

}
