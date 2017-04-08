package com.ztory.lib.sleek.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;

/**
 * Created by jonruna on 2017-04-06.
 */
public class SleekFrameRate extends SleekColorArea {

    private long lastTs, msPassed;

    private float fps, mediumFps = 0, totalFrames = 0, totalDraws = 0;

    private Paint paint, labelPaint;

    public SleekFrameRate(int color) {
        super(color, true, true, false, false, SleekCanvas.STICKY_TOUCH_PRIO);
        setRounded(8);

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
        paint.setTextAlign(Paint.Align.RIGHT);

        labelPaint = new Paint();
        labelPaint.setColor(
                Color.argb(
                        255,
                        255,
                        255,
                        255
                )
        );
        labelPaint.setTextSize(20.0f);
        labelPaint.setTextAlign(Paint.Align.LEFT);
    }

    private void updateMediumFps() {
        totalDraws++;

        totalFrames += fps;

        mediumFps = totalFrames / totalDraws;
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {
        super.drawView(view, canvas, info);

        msPassed = info.drawTimestamp - lastTs;

        fps = msPassed > 0 ? 1000 / msPassed : 0;

        updateMediumFps();

        canvas.drawText(
                "fps:",
                sleekX + 20,
                sleekY + (sleekH / 2.0f) - 5,
                labelPaint
        );

        canvas.drawText(
                "" + (int) fps,
                sleekX + sleekW - 20,
                sleekY + (sleekH / 2.0f) - 5,
                paint
        );

        canvas.drawText(
                "avg:",
                sleekX + 20,
                sleekY + (sleekH / 2.0f) + 20,
                labelPaint
        );

        canvas.drawText(
                "" + (int) mediumFps,
                sleekX + sleekW - 20,
                sleekY + (sleekH / 2.0f) + 20,
                paint
        );

        lastTs = info.drawTimestamp;

        info.invalidate();
    }

}
