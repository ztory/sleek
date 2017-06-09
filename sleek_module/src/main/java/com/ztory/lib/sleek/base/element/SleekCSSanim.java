package com.ztory.lib.sleek.base.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.animation.SAVpercent.PercentDrawView;
import com.ztory.lib.sleek.animation.SleekAnimation;
import com.ztory.lib.sleek.base.SleekColorArea;
import com.ztory.lib.sleek.base.element.css.CSS.Property;
import com.ztory.lib.sleek.base.element.css.CSS.Unit;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.util.Calc;
import com.ztory.lib.sleek.util.UtilPx;

/**
 * Created by jonruna on 2017-06-09.
 */
public class SleekCSSanim extends SleekAnimation implements PercentDrawView {

  protected final SleekElement sleekElement;
  protected final CSSblock targetCSS, startCSS, animStateCSS;//, goalCSS;
  protected boolean animatingBounds = false;
  protected float startX, startY, goalX, goalY;
  protected int startW, startH, goalW, goalH;

  //TODO SUPPORT both ADDING and REMOVING blocks of CSS with this animation !!!!

  public SleekCSSanim(SleekElement theSleekElement, CSSblock theTargetCSS) {
    super(null);
    sleekElement = theSleekElement;
    startX = sleekElement.getSleekX();
    startY = sleekElement.getSleekY();
    startW = sleekElement.getSleekW();
    startH = sleekElement.getSleekH();
    goalX = startX;
    goalY = startY;
    goalW = startW;
    goalH = startH;

    targetCSS = theTargetCSS;
    CSSblock sleekElementCSS = sleekElement.getCSS();
    startCSS = new CSSblockBase(sleekElementCSS.size());
    startCSS.putAll(sleekElementCSS);
//    goalCSS = new CSSblockBase(sleekElementCSS.size());
//    goalCSS.putAll(sleekElementCSS);
//    goalCSS.putAll(targetCSS);

    animStateCSS = new CSSblockBase(sleekElementCSS.size());
    //animStateCSS.putAll(sleekElementCSS);
  }

  public SleekCSSanim setGoalX(float theGoalX) {
    animatingBounds = true;
    goalX = theGoalX;
    return this;
  }

  public SleekCSSanim setGoalY(float theGoalY) {
    animatingBounds = true;
    goalY = theGoalY;
    return this;
  }

  public SleekCSSanim setGoalW(int theGoalW) {
    animatingBounds = true;
    goalW = theGoalW;
    return this;
  }

  public SleekCSSanim setGoalH(int theGoalH) {
    animatingBounds = true;
    goalH = theGoalH;
    return this;
  }

  @Override
  public void percentDrawStart(float percent, Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

    if (animatingBounds) {
      sleekElement.setSleekBounds(
          startX + Math.round((goalX - startX) * percent),
          startY + Math.round((goalY - startY) * percent),
          startW + Math.round((goalW - startW) * percent),
          startH + Math.round((goalH - startH) * percent)
      );
    }

    if (isPropertyUpdated(targetCSS.getBackgroundColor(), startCSS.getBackgroundColor())) {
      int backgroundColor = getAnimatedColor(
          percent,
          getOrDefault(startCSS.getBackgroundColor(), SleekColorArea.COLOR_TRANSPARENT),
          targetCSS.getBackgroundColor()
      );
      animStateCSS.put(Property.BACKGROUND_COLOR, getStringColorFromInt(backgroundColor));

      Log.d("SleekCSSanim", "SleekCSSanim" +
          " | target.bg: " + targetCSS.getBackgroundColor() +
          " | anim.bg: " + animStateCSS.getBackgroundColor() +
          " | getStringColorFromInt: " + getStringColorFromInt(backgroundColor)
      );
    }

    if (isPropertyUpdated(targetCSS.getBorderRadius(), startCSS.getBorderRadius())) {
      int borderRadius = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getBorderRadius(), 0),
          targetCSS.getBorderRadius()
      );
      animStateCSS.put(Property.BORDER_RADIUS, getStringPXfromPixels(borderRadius));

      Log.d("SleekCSSanim", "SleekCSSanim" +
          " | start.borderRadius: " + startCSS.getBorderRadius() +
          " | current.borderRadius: " + borderRadius +
          " | current.borderRadius.DP: " + getStringPXfromPixels(borderRadius)
      );
    }

    if (isPropertyUpdated(targetCSS.getBorderWidth(), startCSS.getBorderWidth())
        || isPropertyUpdated(targetCSS.getBorderColor(), startCSS.getBorderColor())) {
      int borderWidth = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getBorderWidth(), new Rect()).left,
          targetCSS.getBorderWidth().left
      );
//      animStateCSS.put(
//          Property.BORDER_RADIUS,
//          UtilPx.getDP(UtilPx.getDefaultContext(), borderRadius) + Unit.PX
//      );
      int borderColor = getAnimatedColor(
          percent,
          getOrDefault(startCSS.getBorderColor(), SleekColorArea.COLOR_TRANSPARENT),
          targetCSS.getBorderColor()
      );
//      animStateCSS.put(Property.BACKGROUND_COLOR, getStringColorFromInt(backgroundColor));

      animStateCSS.put(
          Property.BORDER,
          getStringPXfromPixels(borderWidth) +
              " solid " + getStringColorFromInt(borderColor)
      );

      Log.d("SleekCSSanim", "SleekCSSanim" +
          " | current.borderWidth.CSS_PX: " + getStringPXfromPixels(borderWidth) +
          " | current.borderColor: " + getStringColorFromInt(borderColor) +
          " | Property.BORDER.start: " + startCSS.get(Property.BORDER) +
          " | Property.BORDER: " + animStateCSS.get(Property.BORDER)
      );
    }

    // Force update SleekElement CSS
    sleekElement.addCSSblockRaw(animStateCSS);// Use addCSSblockRaw() to avoid calling requestLayout
    sleekElement.checkCSS(true);
    sleekElement.reloadShadowBitmap(false);
    sleekElement.removeCSSblockRaw(animStateCSS);
  }

  @Override
  public void percentDrawEnd(float percent, Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

    if (finished) {
      if (animatingBounds) {
        sleekElement.setSleekBounds(
            goalX,
            goalY,
            goalW,
            goalH
        );
      }

      // Add targetCSS and update CSS without calling SleekElement.requestLayout()
      sleekElement.addCSSblockRaw(targetCSS);
      sleekElement.checkCSS(true);
      sleekElement.reloadShadowBitmap(false);
    }
  }

  public static <T> boolean isPropertyUpdated(T targetValue, T oldValue) {
    return targetValue != null && !targetValue.equals(oldValue);
  }

  public static String getStringPXfromPixels(int pixels) {
    return UtilPx.getDP(UtilPx.getDefaultContext(), pixels) + Unit.PX;
  }

  public static String getStringColorFromInt(int color) {
    int colorAlpha = Color.alpha(color);
    int colorRed = Color.red(color);
    int colorGreen = Color.green(color);
    int colorBlue = Color.blue(color);
    //rgba(120, 130, 140, 0.5)
    return "rgba(" + colorRed + "," + colorGreen + "," + colorBlue + "," + Calc.divideToInt(colorAlpha, 255) + ")";
  }

  public static int getAnimatedInt(float percent, int startInt, int goalInt) {
    return startInt + Math.round((goalInt - startInt) * percent);
  }

  public static int getAnimatedColor(float percent, int startColor, int goalColor) {

    if (percent <= 0) {
      return startColor;
    } else if (percent >= 1.0f) {
      return goalColor;
    }

    int startAlpha = Color.alpha(startColor);
    int startRed = Color.red(startColor);
    int startGreen = Color.green(startColor);
    int startBlue = Color.blue(startColor);

    int goalAlpha = Color.alpha(goalColor) - startAlpha;
    int goalRed = Color.red(goalColor) - startRed;
    int goalGreen = Color.green(goalColor) - startGreen;
    int goalBlue = Color.blue(goalColor) - startBlue;

    return Color.argb(
        startAlpha + (int) (goalAlpha * percent),
        startRed + (int) (goalRed * percent),
        startGreen + (int) (goalGreen * percent),
        startBlue + (int) (goalBlue * percent)
    );
  }

  public static <T> T getOrDefault(T value, T defaultValue) {
    if (value != null) {
      return value;
    } else {
      return defaultValue;
    }
  }

}
