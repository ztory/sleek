package com.ztory.lib.sleek.base.scroller.xy;

import android.graphics.Canvas;

/**
 * Empty implementation of EdgeEffectContract.
 * Created by jonruna on 16/09/16.
 */
public class EdgeEffectEmptyContract implements EdgeEffectContract {

    @Override
    public void setSize(int width, int height) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void finish() {

    }

    @Override
    public void onPull(float deltaDistance) {

    }

    @Override
    public void onPull(float deltaDistance, float displacement) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onAbsorb(int velocity) {

    }

    @Override
    public void setColor(int color) {

    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public boolean draw(Canvas canvas) {
        return false;
    }

    @Override
    public int getMaxHeight() {
        return 0;
    }
}
