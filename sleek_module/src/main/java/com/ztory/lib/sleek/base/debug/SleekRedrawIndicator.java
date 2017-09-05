package com.ztory.lib.sleek.base.debug;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.base.SleekParam;

/**
 * Created by jonruna on 2017-04-06.
 */
public class SleekRedrawIndicator extends SleekBase {

  protected Paint bgPaint, mainPaint, secondaryPaint;

  protected int roundRadius, currentDegree = 0, secondaryCurrentDegree = 0;

  public SleekRedrawIndicator(SleekParam sleekParam) {
    super(sleekParam);

    bgPaint = new Paint();
    bgPaint.setAntiAlias(true);
    bgPaint.setColor(Color.parseColor("#222222"));

    mainPaint = new Paint();
    mainPaint.setAntiAlias(true);
    mainPaint.setColor(Color.parseColor("#f9f9f9"));
    mainPaint.setStyle(Style.FILL_AND_STROKE);
    mainPaint.setStrokeWidth(4);

    secondaryPaint = new Paint();
    secondaryPaint.setAntiAlias(true);
    secondaryPaint.setColor(Color.parseColor("#bada55"));
    secondaryPaint.setStyle(Style.FILL_AND_STROKE);
    secondaryPaint.setStrokeWidth(4);
  }

  @Override
  public void setSleekBounds(float x, float y, int w, int h) {

    h = w;//set height to be equal to width

    super.setSleekBounds(x, y, w, h);

    roundRadius = (int) (w / 2.0f);
  }

  @Override
  public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

    canvas.save();

    canvas.translate(sleekX, sleekY);

    canvas.drawCircle(roundRadius, roundRadius, roundRadius, bgPaint);

    canvas.save();
    canvas.rotate(currentDegree, roundRadius, roundRadius);
    canvas.drawLine(
        roundRadius,
        roundRadius,
        roundRadius + roundRadius,
        roundRadius,
        mainPaint
    );
    canvas.restore();

    canvas.save();
    canvas.rotate(secondaryCurrentDegree, roundRadius, roundRadius);
    canvas.drawLine(
        roundRadius,
        roundRadius,
        roundRadius + (roundRadius / 2.0f),
        roundRadius,
        secondaryPaint
    );
    canvas.restore();

    canvas.restore();

    currentDegree++;
    secondaryCurrentDegree += 2;
  }

}
