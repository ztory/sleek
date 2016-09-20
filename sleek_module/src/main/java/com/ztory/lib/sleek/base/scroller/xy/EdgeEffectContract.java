package com.ztory.lib.sleek.base.scroller.xy;

import android.graphics.Canvas;

/**
 * Contract for working with edge effects.
 * Created by jonruna on 16/09/16.
 */
public interface EdgeEffectContract {
    void setSize(int width, int height);
    boolean isFinished();
    void finish();
    void onPull(float deltaDistance);
    void onPull(float deltaDistance, float displacement);
    void onRelease();
    void onAbsorb(int velocity);
    void setColor(int color);
    int getColor();
    boolean draw(Canvas canvas);
    int getMaxHeight();
}
