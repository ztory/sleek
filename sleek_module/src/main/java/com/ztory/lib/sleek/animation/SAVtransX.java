package com.ztory.lib.sleek.animation;

import android.graphics.Canvas;

import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.Sleek;

/**
 * Translate Sleek instance in X direction.
 * Created by jonruna on 09/10/14.
 */
public class SAVtransX implements ISleekAnimView {

    private float startX, goalX, amountTransX, currTransX, percent;
    private long duration, progressTs, startTs;
    private boolean finished = false;
    private ISleekDrawView doneRun;

    public SAVtransX(
            float theStartX,
            float theGoalX,
            long theDuration,
            ISleekDrawView theDoneRun
            ) {
        startX = theStartX;
        goalX = theGoalX;
        amountTransX = goalX - startX;
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
            currTransX = startX + (amountTransX * percent);
        }
        else {
            finished = true;
            currTransX = goalX;
        }

        view.setSleekBounds(
                currTransX,
                view.getSleekY(),
                view.getSleekW(),
                view.getSleekH()
                );

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
