package com.ztory.lib.sleek.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.SleekParent;

public class SVframeRate implements Sleek {

    private long lastTs, msPassed;

    private float fps, mediumFps = 0, totalFrames = 0, totalDraws = 0;

    private Paint paint, bgPaint;

    private boolean topAligned = false;

    public SVframeRate(boolean theTopAligned) {

        topAligned = theTopAligned;

        lastTs = System.currentTimeMillis();

        paint = new Paint();
        paint.setColor(
                Color.argb(
                        255,
                        255,
                        255,
                        255
                        )
                );
        paint.setTextSize(20.0f);

        bgPaint = new Paint();
        bgPaint.setColor(
                Color.argb(
                        255,
                        0,
                        0,
                        0
                        )
                );
    }

    private void updateMediumFps() {
        totalDraws++;

        totalFrames += fps;

        mediumFps = totalFrames / totalDraws;
    }

    @Override
    public void onSleekDraw(Canvas canvas, SleekCanvasInfo drawInfo) {

        msPassed = drawInfo.drawTimestamp - lastTs;

        fps = msPassed > 0 ? 1000 / msPassed : 0;

        updateMediumFps();

        if (topAligned) {
            canvas.drawRect(
                    5,
                    25,
                    200,
                    50,
                    bgPaint
            );

            canvas.drawText(
                    fps + " fps / " + mediumFps,
                    10,
                    44,
                    paint
            );
        }
        else {
            canvas.drawRect(
                    5,
                    drawInfo.height - 50,
                    200,
                    drawInfo.height - 25,
                    bgPaint
            );

            canvas.drawText(
                    fps + " fps / " + mediumFps,
                    10,
                    drawInfo.height - 30,
                    paint
            );
        }

        lastTs = drawInfo.drawTimestamp;
    }

    @Override
    public void onSleekCanvasResize(SleekCanvasInfo info) {

    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {

    }

    @Override
    public float getSleekX() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getSleekY() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo drawInfo) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getSleekPriority() {
        return SleekCanvas.STICKY_TOUCH_PRIO;
    }

    @Override
    public boolean isSleekReadyToDraw() {
        return false;
    }

    @Override
    public boolean isSleekLoaded() {
        return false;
    }

    @Override
    public boolean isSleekLoadable() {
        return false;
    }

    @Override
    public void onSleekLoad(SleekCanvasInfo info) {

    }

    @Override
    public void onSleekUnload() {

    }

    @Override
    public void onSleekParentAdd(SleekCanvas sleekCanvas, SleekParent sleekParent) {

    }

    @Override
    public void onSleekParentRemove(SleekCanvas sleekCanvas, SleekParent sleekParent) {

    }

    @Override
    public SleekCanvas getSleekCanvas() {
        return null;
    }

    @Override
    public SleekParent getSleekParent() {
        return null;
    }

    @Override
    public int getSleekW() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSleekH() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isSleekFixedPosition() {
        return true;
    }

    @Override
    public boolean isSleekTouchable() {
        return false;
    }

}
