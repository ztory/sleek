package com.ztory.lib.sleek.base.scroller.xy;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.EdgeEffect;

/**
 * EdgeEffectContract implementation using the <code>android.widget.EdgeEffect</code> class.
 * Created by jonruna on 16/09/16.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class EdgeEffectWithContract extends EdgeEffect implements EdgeEffectContract {

    protected int mLowSDKmaxHeight;

    /**
     * Construct a new EdgeEffect with a theme appropriate for the provided context.
     *
     * @param context Context used to provide theming and resource information for the EdgeEffect
     */
    public EdgeEffectWithContract(Context context) {
        super(context);
    }

    @Override
    public void onPull(float deltaDistance, float displacement) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.onPull(deltaDistance, displacement);
        }
        else {
            super.onPull(deltaDistance);
        }
    }

    @Override
    public void setColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.setColor(color);
        }
    }

    @Override
    public int getColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            return super.getColor();
        }
        return 0xff666666;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        mLowSDKmaxHeight = height;
    }

    @Override
    public int getMaxHeight() {
        if (Build.VERSION.SDK_INT >= 21) {
            return super.getMaxHeight();
        }
        return mLowSDKmaxHeight;
    }

}
