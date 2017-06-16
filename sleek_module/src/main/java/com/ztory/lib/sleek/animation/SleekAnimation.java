package com.ztory.lib.sleek.animation;

import android.graphics.Canvas;
import android.view.animation.Interpolator;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.animation.SAVpercent.PercentDrawView;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.util.Calc;
import com.ztory.lib.sleek.util.UtilPx;

/**
 * Created by jonruna on 2017-06-09.
 */
public class SleekAnimation implements ISleekAnimView, PercentDrawView {

  public final static int
      ANIM_DURATION_SHORT =
      UtilPx.getDefaultContext().getResources().getInteger(android.R.integer.config_shortAnimTime),
      ANIM_DURATION_SHORT_HALF =
      (int) (ANIM_DURATION_SHORT / 2.0f),
      ANIM_DURATION_MEDIUM =
      UtilPx.getDefaultContext().getResources().getInteger(android.R.integer.config_mediumAnimTime),
      ANIM_DURATION_LONG =
      UtilPx.getDefaultContext().getResources().getInteger(android.R.integer.config_longAnimTime);

  protected Interpolator interpolator = null;
  protected boolean hasInterpolator = false;
  protected float percent;
  protected long startTs = System.currentTimeMillis(), duration = ANIM_DURATION_SHORT, progressTs;
  protected boolean finished = false;

  protected final PercentDrawView callbackRun;
  protected ISleekDrawView doneRun = null;

  public SleekAnimation(PercentDrawView theCallbackRun) {
    if (theCallbackRun != null) {
      callbackRun = theCallbackRun;
    } else {
      callbackRun = this;
    }
  }

  public SleekAnimation setDoneListener(ISleekDrawView theDoneRun) {
    doneRun = theDoneRun;
    return this;
  }

  public SleekAnimation setStartTimestamp(long theStartTimestamp) {
    startTs = theStartTimestamp;
    return this;
  }

  public SleekAnimation setDuration(long theDuration) {
    duration = theDuration;
    return this;
  }

  public SleekAnimation setInterpolator(Interpolator theInterpolator) {
    interpolator = theInterpolator;
    hasInterpolator = interpolator != null;
    return this;
  }

  // percentDrawStart and percentDrawEnd can be overriden in subclasses of SleekAnimation

  @Override
  public void percentDrawStart(float percent, Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

  }

  @Override
  public void percentDrawEnd(float percent, Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

  }

  @Override
  public void animTickStart(Sleek view, Canvas canvas, SleekCanvasInfo info) {
    progressTs = info.drawTimestamp - startTs;

    if (progressTs < duration) {
      percent = Calc.divide(progressTs, duration);
      if (hasInterpolator) {
        percent = interpolator.getInterpolation(percent);
      }
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
