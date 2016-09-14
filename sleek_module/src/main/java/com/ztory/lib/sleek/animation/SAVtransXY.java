package com.ztory.lib.sleek.animation;

import android.graphics.Canvas;

import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.Sleek;

/**
 * Translate Sleek instance in X and Y direction.
 * Created by jonruna on 09/10/14.
 */
public class SAVtransXY implements ISleekAnimView {

    private float startX, goalX, amountTransX, currTransX, startY, goalY, amountTransY, currTransY, percent;
    private long duration, progressTs, startTs;
    private boolean finished = false;
    private ISleekDrawView doneRun;

    public SAVtransXY(
            float theStartX,
            float theGoalX,
            float theStartY,
            float theGoalY,
            long theDuration,
            ISleekDrawView theDoneRun
            ) {
        startX = theStartX;
        goalX = theGoalX;
        amountTransX = goalX - startX;

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
            percent = percent * percent;
            currTransX = startX + (amountTransX * percent);
            currTransY = startY + (amountTransY * percent);
        }
        else {
            finished = true;
            currTransX = goalX;
            currTransY = goalY;
        }

        view.setSleekBounds(
                currTransX,
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
