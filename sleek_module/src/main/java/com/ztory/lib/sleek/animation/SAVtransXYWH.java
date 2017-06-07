package com.ztory.lib.sleek.animation;

import android.graphics.Canvas;

import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.contract.ISleekAnimRun;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.util.Calc;

/**
 * Translate Sleek instance in X and Y direction, as well as resizing W and H.
 * Created by jonruna on 09/10/14.
 */
public class SAVtransXYWH implements ISleekAnimView {

    private int
            startW, goalW, amountTransW, currTransW,
            startH, goalH, amountTransH, currTransH;
    private float
            startX, goalX, amountTransX, currTransX,
            startY, goalY, amountTransY, currTransY,
            percent;
    private long duration, progressTs, startTs;
    private boolean finished = false;
    private ISleekDrawView doneRun;
    private ISleekAnimRun tickListener;

    public SAVtransXYWH(
            float theStartX,
            float theGoalX,
            float theStartY,
            float theGoalY,
            int theStartW,
            int theGoalW,
            int theStartH,
            int theGoalH,
            long theDuration,
            ISleekDrawView theDoneRun
    ) {
        startX = theStartX;
        goalX = theGoalX;
        amountTransX = goalX - startX;

        startY = theStartY;
        goalY = theGoalY;
        amountTransY = goalY - startY;

        startW = theStartW;
        goalW = theGoalW;
        amountTransW = goalW - startW;

        startH = theStartH;
        goalH = theGoalH;
        amountTransH = goalH - startH;

        duration = theDuration;
        doneRun = theDoneRun;
        startTs = System.currentTimeMillis();
    }

    public SAVtransXYWH setAnimTickListener(ISleekAnimRun theTickListener) {
        tickListener = theTickListener;
        return this;
    }

    @Override
    public void animTickStart(Sleek view, Canvas canvas, SleekCanvasInfo info) {
        progressTs = info.drawTimestamp - startTs;

        if (progressTs < duration) {
            percent = (float) progressTs / (float) duration;
            percent = percent * percent;
            currTransX = startX + Calc.multiplyToInt(amountTransX, percent);
            currTransY = startY + Calc.multiplyToInt(amountTransY, percent);
            currTransW = startW + Calc.multiplyToInt(amountTransW, percent);
            currTransH = startH + Calc.multiplyToInt(amountTransH, percent);
        }
        else {
            finished = true;
            currTransX = goalX;
            currTransY = goalY;
            currTransW = goalW;
            currTransH = goalH;
        }

        view.setSleekBounds(
                currTransX,
                currTransY,
                currTransW,
                currTransH
        );

        if (tickListener != null) {
            tickListener.run(info);
        }
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
