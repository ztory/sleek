package com.ztory.lib.sleek.animation;

import android.graphics.Canvas;

import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.Sleek;

/**
 * Translate Sleek instance in Y direction.
 * Created by jonruna on 09/10/14.
 */
public class SAVtransY implements ISleekAnimView {

    private float startY, goalY, amountTransY, currTransY, percent;
    private long duration, progressTs, startTs;
    private boolean finished = false;
    private ISleekDrawView doneRun;

    public SAVtransY(
            float theStartY,
            float theGoalY,
            long theDuration,
            ISleekDrawView theDoneRun
            ) {
        startY = theStartY;
        goalY = theGoalY;
        amountTransY = goalY - startY;
        duration = theDuration;
        doneRun = theDoneRun;
        startTs = System.currentTimeMillis();
    }

    @Override
    public void animTickStart(Sleek view, Canvas canvas, SleekCanvasInfo info) {
        progressTs = info.drawTimestamp - startTs;

        if (progressTs < duration) {
            percent = (float) progressTs / (float) duration;
            percent = (float) Math.pow(percent, (1.0f - percent));
            currTransY = startY + (amountTransY * percent);
        }
        else {
            finished = true;
            currTransY = goalY;
        }

        view.setSleekBounds(
                view.getSleekX(),
                currTransY,
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
