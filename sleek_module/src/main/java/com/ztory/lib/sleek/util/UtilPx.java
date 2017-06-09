package com.ztory.lib.sleek.util;

import android.content.Context;

/**
 * Util functions related to pixel and "Density-independent pixel" sizes.
 * Created by jonruna on 15/09/16.
 */
public class UtilPx {

    private static Context sDefaultContext;

    public static void setDefaultContext(Context theDefaultContext) {
        sDefaultContext = theDefaultContext;
    }

    public static Context getDefaultContext() {
        return sDefaultContext;
    }

    public static int getPixels(int dipValue) {
        return getPixels(sDefaultContext, dipValue);
    }

    /** Returns the number of pixels that the supplied <code>dipValue</code> corresponds to on the device calling this method. */
    public static int getPixels(Context theContext, int dipValue) {
        return Math.round(dipValue * theContext.getResources().getDisplayMetrics().density);
    }

    public static int getDP(Context theContext, int pixelValue) {
        return Math.round(pixelValue / theContext.getResources().getDisplayMetrics().density);
    }

}
