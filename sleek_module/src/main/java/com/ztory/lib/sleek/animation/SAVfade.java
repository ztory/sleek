package com.ztory.lib.sleek.animation;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.Sleek;

/**
 * Fade animation.
 * Created by jonruna on 09/10/14.
 */
public class SAVfade implements ISleekAnimView {

    private Paint animPaint;
    private int startAlpha, goalAlpha, amountAlpha, currAlpha;
    private float percent;
    private long duration, progressTs, startTs;
    private boolean finished = false;
    private ISleekDrawView doneRun;

    public SAVfade(
            int theStartAlpha,
            int theGoalAlpha,
            long theDuration,
            Paint theAnimPaint,
            ISleekDrawView theDoneRun
            ) {

        animPaint = theAnimPaint;

        startAlpha = theStartAlpha;
        goalAlpha = theGoalAlpha;
        amountAlpha = goalAlpha - startAlpha;

        duration = theDuration;
        doneRun = theDoneRun;
        startTs = System.currentTimeMillis();
    }

    @Override
    public void animTickStart(Sleek view, Canvas canvas, SleekCanvasInfo info) {
        progressTs = info.drawTimestamp - startTs;

        if (progressTs < duration) {
            percent = (float) progressTs / (float) duration;
            percent = percent * percent;
            currAlpha = (int) (startAlpha + (amountAlpha * percent));
        }
        else {
            finished = true;
            currAlpha = goalAlpha;
        }

        animPaint.setAlpha(currAlpha);

    }

    @Override
    public boolean animTickEnd(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        if (finished) {
            doneRun.drawView(view, canvas, info);
        }

        info.invalidate();

        return finished;
    }

}
