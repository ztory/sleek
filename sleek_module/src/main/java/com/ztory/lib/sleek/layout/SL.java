package com.ztory.lib.sleek.layout;

/**
 * Created by jonruna on 09/10/14.
 */
public class SL {

    public enum X {
        ABSOLUTE,
        WEST_OF,
        EAST_OF,
        POS_CENTER,
        PERCENT,
        PERCENT_CANVAS,
        COMPUTE,
        PARENT_LEFT,
        PARENT_RIGHT,
        PARENT_PERCENT
    }

    public enum Y {
        ABSOLUTE,
        NORTH_OF,
        SOUTH_OF,
        POS_CENTER,
        PERCENT,
        PERCENT_CANVAS,
        COMPUTE,
        PARENT_TOP,
        PARENT_BOTTOM,
        PARENT_PERCENT
    }

    public enum W {
        ABSOLUTE,
        MATCH_PARENT,
        PERCENT,
        PERCENT_CANVAS,
        COMPUTE
    }

    public enum H {
        ABSOLUTE,
        MATCH_PARENT,
        PERCENT,
        PERCENT_CANVAS,
        COMPUTE
    }

}
