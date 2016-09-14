package com.ztory.lib.sleek.layout;

import com.ztory.lib.sleek.Sleek;

/**
 Usage with SleekBase classes:
    .getLayout()
        .x(Lay.x(LayX.PARENT_LEFT, X_MARGIN, X_PARENT))
        .y(Lay.y(LayY.PARENT_TOP, Y_MARGIN, Y_PARENT))
        .w(Lay.w(LayW.MATCH_PARENT, W_MARGIN, W_PARENT))
        .h(Lay.h(LayH.MATCH_PARENT, H_MARGIN, H_PARENT));
 * Created by jonruna on 09/10/14.
 */
public class Lay {

    //________________ -START- X ________________

    /** Returns a LayX instance used as parameter for the SlkLay class.
     * Used for SlkLay.COMPUTE layout values. */
    public static LayX x(IComputeFloat theComputeFloat) {
        return new LayX(theComputeFloat);
    }

    /** Returns a LayX instance used as parameter for the SlkLay class.
     * Used for SlkLay.ABSOLUTE layout values. */
    public static LayX x(int theMargin) {
        return new LayX(theMargin);
    }

    /** Returns a LayX instance used as parameter for the SlkLay class. */
    public static LayX x(int theMode, int theMargin, Sleek theParent) {
        return new LayX(theMode, theMargin, theParent);
    }

    /** Returns a LayX instance used as parameter for the SlkLay class. */
    public static LayX x(int theMode, int theMargin, Sleek theParent, float thePercent) {
        return new LayX(theMode, theMargin, theParent, thePercent);
    }
    //________________ - END - X ________________


    //________________ -START- Y ________________

    /** Returns a LayY instance used as parameter for the SlkLay class.
     * Used for SlkLay.COMPUTE layout values. */
    public static LayY y(IComputeFloat theComputeFloat) {
        return new LayY(theComputeFloat);
    }

    /** Returns a LayY instance used as parameter for the SlkLay class.
     * Used for SlkLay.ABSOLUTE layout values. */
    public static LayY y(int theMargin) {
        return new LayY(theMargin);
    }

    /** Returns a LayY instance used as parameter for the SlkLay class. */
    public static LayY y(int theMode, int theMargin, Sleek theParent) {
        return new LayY(theMode, theMargin, theParent);
    }

    /** Returns a LayY instance used as parameter for the SlkLay class. */
    public static LayY y(int theMode, int theMargin, Sleek theParent, float thePercent) {
        return new LayY(theMode, theMargin, theParent, thePercent);
    }
    //________________ - END - Y ________________


    //________________ -START- W ________________

    /** Returns a LayW instance used as parameter for the SlkLay class.
     * Used for SlkLay.COMPUTE layout values. */
    public static LayW w(IComputeInt theComputeInt) {
        return new LayW(theComputeInt);
    }

    /** Returns a LayW instance used as parameter for the SlkLay class.
     * Used for SlkLay.ABSOLUTE layout values. */
    public static LayW w(int theMargin) {
        return new LayW(theMargin);
    }

    /** Returns a LayW instance used as parameter for the SlkLay class. */
    public static LayW w(int theMode, int theMargin, Sleek theParent) {
        return new LayW(theMode, theMargin, theParent);
    }

    /** Returns a LayW instance used as parameter for the SlkLay class. */
    public static LayW w(int theMode, int theMargin, Sleek theParent, float thePercent) {
        return new LayW(theMode, theMargin, theParent, thePercent);
    }
    //________________ - END - W ________________


    //________________ -START- H ________________

    /** Returns a LayH instance used as parameter for the SlkLay class.
     * Used for SlkLay.COMPUTE layout values. */
    public static LayH h(IComputeInt theComputeInt) {
        return new LayH(theComputeInt);
    }

    /** Returns a LayH instance used as parameter for the SlkLay class.
     * Used for SlkLay.ABSOLUTE layout values. */
    public static LayH h(int theMargin) {
        return new LayH(theMargin);
    }

    /** Returns a LayH instance used as parameter for the SlkLay class. */
    public static LayH h(int theMode, int theMargin, Sleek theParent) {
        return new LayH(theMode, theMargin, theParent);
    }

    /** Returns a LayH instance used as parameter for the SlkLay class. */
    public static LayH h(int theMode, int theMargin, Sleek theParent, float thePercent) {
        return new LayH(theMode, theMargin, theParent, thePercent);
    }
    //________________ - END - H ________________


}
