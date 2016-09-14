package com.ztory.lib.sleek.animation;

import android.graphics.Canvas;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.contract.IAnimProgress;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;

import java.util.ArrayList;
import java.util.List;

/**
 * Translate multiple Sleek instances in X and Y direction.
 * Created by jonruna on 09/10/14.
 */
public class SAVmassTransXY implements ISleekAnimView {

    private ArrayList<Sleek> animList = new ArrayList<>(20);

    private float
            tickTransX,
            haveTranslatedX,
            amountTransX,
            tickTransY,
            haveTranslatedY,
            amountTransY,
            percent;

    private long duration, progressTs, startTs = -1;
    private boolean finished = false, hasAnimTickListener;

    private IAnimProgress mAnimTickListener;
    private ISleekDrawView doneRun;

    public SAVmassTransXY(
            float theAmountTransX,
            float theAmountTransY,
            long theDuration,
            IAnimProgress theAnimTickListener,
            ISleekDrawView theDoneRun
    ) {
        amountTransX = theAmountTransX;
        amountTransY = theAmountTransY;

        haveTranslatedX = 0.0f;
        haveTranslatedY = 0.0f;

        duration = theDuration;

        mAnimTickListener = theAnimTickListener;
        hasAnimTickListener = mAnimTickListener != null;

        doneRun = theDoneRun;
    }

    public SAVmassTransXY addView(Sleek viewToAdd) {
        animList.add(viewToAdd);
        return this;
    }

    public SAVmassTransXY addViews(List<SleekBase> listToAdd) {
        animList.addAll(listToAdd);
        return this;
    }

    /** Translates the views pos to negative amountTransX/amountTransX. */
    public SAVmassTransXY applyPosNegativeTrans() {
        for (Sleek iterView : animList) {
            iterView.setSleekBounds(
                    iterView.getSleekX() - amountTransX,
                    iterView.getSleekY() - amountTransY,
                    iterView.getSleekW(),
                    iterView.getSleekH()
            );
        }
        return this;
    }

    @Override
    public void animTickStart(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        if (startTs == -1) {
            startTs = info.drawTimestamp;


        }
        else {
            progressTs = info.drawTimestamp - startTs;

            if (progressTs < duration) {
                percent = (float) progressTs / (float) duration;
                percent = percent * percent;

                float amountTransXpercent = (amountTransX * percent);
                tickTransX = amountTransXpercent - haveTranslatedX;
                haveTranslatedX = amountTransXpercent;

                float amountTransYpercent = (amountTransY * percent);
                tickTransY = amountTransYpercent - haveTranslatedY;
                haveTranslatedY = amountTransYpercent;
            }
            else {
                finished = true;
                percent = 1.0f;

                tickTransX = amountTransX - haveTranslatedX;
                tickTransY = amountTransY - haveTranslatedY;
            }

            for (Sleek iterView : animList) {
                iterView.setSleekBounds(
                        iterView.getSleekX() + tickTransX,
                        iterView.getSleekY() + tickTransY,
                        iterView.getSleekW(),
                        iterView.getSleekH()
                );
            }
        }

        if (hasAnimTickListener) {
            mAnimTickListener.progress(percent);
        }
    }

    @Override
    public boolean animTickEnd(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        if (
                finished &&
                doneRun != null
                ) {
            doneRun.drawView(view, canvas, info);
        }

        info.invalidate();

        return finished;
    }

}
