package com.ztory.lib.sleek.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;

/**
 * ISleekAnimView implementation that helps to create custom ISleekAnimView implementations.
 * This class also have a couple of pre built animations.
 * Created by jonruna on 09/10/14.
 */
public class SAVpercent implements ISleekAnimView {

    public interface PercentDrawView {
        void percentDrawStart(float percent, Sleek sleek, Canvas canvas, SleekCanvasInfo info);
        void percentDrawEnd(float percent, Sleek sleek, Canvas canvas, SleekCanvasInfo info);
    }

    public static SAVpercent.PercentDrawView getAnimColors(
            final Paint paint,
            final int colorStart,
            final int colorEnd
    ) {
        return new SAVpercent.PercentDrawView() {

            private boolean initialized = false;
            private int startAlpha, startRed, startGreen, startBlue;
            private int amountAlpha, amountRed, amountGreen, amountBlue;

            @Override
            public void percentDrawStart(
                    float percent,
                    Sleek sleek,
                    Canvas canvas,
                    SleekCanvasInfo info
            ) {
                if (percent == 1.0f) {
                    paint.setColor(colorEnd);
                    return;
                }

                if (!initialized) {
                    initialized = true;

                    startAlpha = Color.alpha(colorStart);
                    startRed = Color.red(colorStart);
                    startGreen = Color.green(colorStart);
                    startBlue = Color.blue(colorStart);

                    amountAlpha = Color.alpha(colorEnd) - Color.alpha(colorStart);
                    amountRed = Color.red(colorEnd) - Color.red(colorStart);
                    amountGreen = Color.green(colorEnd) - Color.green(colorStart);
                    amountBlue = Color.blue(colorEnd) - Color.blue(colorStart);
                }

                paint.setColor(
                        Color.argb(
                                startAlpha + (int) (amountAlpha * percent),
                                startRed + (int) (amountRed * percent),
                                startGreen + (int) (amountGreen * percent),
                                startBlue + (int) (amountBlue * percent)
                        )
                );
            }

            @Override
            public void percentDrawEnd(
                    float percent,
                    Sleek sleek,
                    Canvas canvas,
                    SleekCanvasInfo info
            ) {

            }
        };
    }

    public static SAVpercent.PercentDrawView getAnimBounds(
            final Sleek sleekReceiver,
            final float targetX,
            final float targetY,
            final int targetW,
            final int targetH,
            final boolean applyLayout
    ) {
        return new SAVpercent.PercentDrawView() {

            private boolean initialized = false;
            private float startX, amountX;
            private float startY, amountY;
            private int startW, amountW;
            private int startH, amountH;

            @Override
            public void percentDrawStart(
                    float percent,
                    Sleek sleekAnimator,
                    Canvas canvas,
                    SleekCanvasInfo info
            ) {
                if (percent == 1.0f) {
                    sleekReceiver.setSleekBounds(
                            targetX,
                            targetY,
                            targetW,
                            targetH
                    );
                    if (applyLayout) {
                        sleekReceiver.onSleekCanvasResize(info);
                    }
                    return;
                }

                if (!initialized) {
                    initialized = true;

                    startX = sleekReceiver.getSleekX();
                    startY = sleekReceiver.getSleekY();
                    startW = sleekReceiver.getSleekW();
                    startH = sleekReceiver.getSleekH();

                    amountX = targetX - startX;
                    amountY = targetY - startY;
                    amountW = targetW - startW;
                    amountH = targetH - startH;
                }

                sleekReceiver.setSleekBounds(
                        startX + (amountX * percent),
                        startY + (amountY * percent),
                        startW + (int) (amountW * percent),
                        startH + (int) (amountH * percent)
                );
                if (applyLayout) {
                    sleekReceiver.onSleekCanvasResize(info);
                }
            }

            @Override
            public void percentDrawEnd(
                    float percent,
                    Sleek sleekAnimator,
                    Canvas canvas,
                    SleekCanvasInfo info
            ) {

            }
        };
    }

    private float percent;
    private long duration, progressTs, startTs;
    private boolean finished = false, accelerating;
    private ISleekDrawView doneRun;

    private PercentDrawView callbackRun;

    public SAVpercent(
            long theDuration,
            boolean theAccelerating,
            long theStartTs,
            PercentDrawView theCallbackRun,
            ISleekDrawView theDoneRun
            ) {

        callbackRun = theCallbackRun;

        duration = theDuration;
        accelerating = theAccelerating;
        doneRun = theDoneRun;
        startTs = theStartTs;
    }

    @Override
    public void animTickStart(Sleek view, Canvas canvas, SleekCanvasInfo info) {
        progressTs = info.drawTimestamp - startTs;

        if (progressTs < duration) {
            percent = (float) progressTs / (float) duration;
            if (accelerating) percent = percent * percent;
            //else percent = percent / percent;
        }
        else {
            finished = true;
            percent = 1.0f;
        }

        if (percent < 0) {
            percent = 0;
        }

        callbackRun.percentDrawStart(percent, view, canvas, info);
    }

    @Override
    public boolean animTickEnd(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        callbackRun.percentDrawEnd(percent, view, canvas, info);

        if (finished && doneRun != null) {
            doneRun.drawView(view, canvas, info);
        }

        info.invalidate();

        return finished;
    }

}
