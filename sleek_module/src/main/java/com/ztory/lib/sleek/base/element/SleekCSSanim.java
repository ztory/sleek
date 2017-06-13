package com.ztory.lib.sleek.base.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
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

  public static final boolean ADD_CSS = false, REMOVE_CSS = true;

  protected final SleekElement sleekElement;
  protected final CSSblock goalCSS, startCSS, animStateCSS;
  protected final CSSblock[] targetCSSlist;
  protected boolean animatingBounds = false;
  protected float startX, startY, goalX, goalY;
  protected int startW, startH, goalW, goalH;
  protected final boolean removeTargetCSS;

  public SleekCSSanim(
      SleekElement theSleekElement,
      boolean shouldRemoveCSS,
      CSSblock... theTargetCSS
  ) {
    super(null);
    sleekElement = theSleekElement;
    removeTargetCSS = shouldRemoveCSS;
    startX = sleekElement.getSleekX();
    startY = sleekElement.getSleekY();
    startW = sleekElement.getSleekW();
    startH = sleekElement.getSleekH();
    goalX = startX;
    goalY = startY;
    goalW = startW;
    goalH = startH;

    CSSblock sleekElementCSS = sleekElement.getCSS();
    startCSS = new CSSblockBase(sleekElementCSS.size());
    startCSS.putAll(sleekElementCSS);

    targetCSSlist = theTargetCSS;
    goalCSS = new CSSblockBase(sleekElementCSS.size() + sleekElementCSS.size());
    if (sleekElement.removeAnimationCSSblocks() > 0) {// remove animation blocks
      if (removeTargetCSS) {
        sleekElement.removeCSSblocksRaw(targetCSSlist);
      }
      sleekElement.refreshCSS();
      sleekElementCSS = sleekElement.getCSS();
      goalCSS.putAll(sleekElementCSS);
    } else if (removeTargetCSS && sleekElement.removeCSSblocksRaw(targetCSSlist) > 0) {
      sleekElement.refreshCSS();
      sleekElementCSS = sleekElement.getCSS();
      goalCSS.putAll(sleekElementCSS);
    }

    if (!removeTargetCSS) {
      for (CSSblock iterBlock : targetCSSlist) {
        goalCSS.putAll(iterBlock);
      }
    }

    // Add CSSblocks before animStateCSS is added
    if (!removeTargetCSS) {
      for (CSSblock iterBlock : targetCSSlist) {
        sleekElement.addCSSnoRefresh(iterBlock);
      }
    }

    animStateCSS = new CSSblockBase(sleekElementCSS.size());
    animStateCSS.put(SleekElement.CSS_BLOCK_ANIMATION_KEY, "true");// Mark as animation CSS block
    sleekElement.addCSSnoRefresh(animStateCSS);
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

//      CSS_STRING = "{" +
//      V - "background: #d8d8d8;" +
//      V - "background-color: #d8d8d8;" +
//      V - "background-image: url(\"https://example.com/example.png\");" +
//      V - "background-size: cover;" +
//      V - "border-radius: 22px;" +
//      V - "border: 1px solid #0000ff;" +
//      V - "box-shadow: 1px 2px 4px rgba(120, 130, 140, 0.5);" +
//      V - "padding: 5px 10px 15px 20px;" +
//      V - "color: #666;" +
//      V - "font-size: 10px;" +
//      V - "line-height: 46px;" +
//      V - "text-align: center;" +
//      V - "vertical-align: middle;" +
//      V - "text-shadow: 1px 1px 2px black;" +
//          "}",

    if (isPropertyUpdated(goalCSS.getColor(), startCSS.getColor())) {
      int color = getAnimatedColor(
          percent,
          getOrDefault(startCSS.getColor(), SleekColorArea.COLOR_TRANSPARENT),
          goalCSS.getColor()
      );
      animStateCSS.put(Property.COLOR, getStringColorFromInt(color));
    }

    if (isPropertyUpdated(goalCSS.getBackgroundColor(), startCSS.getBackgroundColor())) {
      int backgroundColor = getAnimatedColor(
          percent,
          getOrDefault(startCSS.getBackgroundColor(), SleekColorArea.COLOR_TRANSPARENT),
          goalCSS.getBackgroundColor()
      );
      animStateCSS.put(Property.BACKGROUND_COLOR, getStringColorFromInt(backgroundColor));
//      Log.d("SleekCSSanim", "SleekCSSanim" +
//          " | target.bg: " + goalCSS.getBackgroundColor() +
//          " | anim.bg: " + animStateCSS.getBackgroundColor() +
//          " | getStringColorFromInt: " + getStringColorFromInt(backgroundColor)
//      );
    }

    if (isPropertyUpdated(goalCSS.getPadding(), startCSS.getPadding())) {
      Rect padding = getAnimatedRect(
          percent,
          getOrDefault(startCSS.getPadding(), new Rect()),
          goalCSS.getPadding()
      );
      animStateCSS.put(
          Property.PADDING, getStringPXfromPixels(padding.top)
              + " " + getStringPXfromPixels(padding.right)
              + " " + getStringPXfromPixels(padding.bottom) +
              " " + getStringPXfromPixels(padding.left)
      );
    }

    if (isPropertyUpdated(goalCSS.getFontSize(), startCSS.getFontSize())) {
      int fontSize = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getFontSize(), 0),
          goalCSS.getFontSize()
      );
      animStateCSS.put(Property.FONT_SIZE, getStringPXfromPixels(fontSize));
    }

    if (isPropertyUpdated(goalCSS.getLineHeight(), startCSS.getLineHeight())) {
      int lineHeight = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getLineHeight(), 0),
          goalCSS.getLineHeight()
      );
      animStateCSS.put(Property.LINE_HEIGHT, getStringPXfromPixels(lineHeight));
    }

    if (isPropertyUpdated(goalCSS.getBorderRadius(), startCSS.getBorderRadius())) {
      int borderRadius = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getBorderRadius(), 0),
          goalCSS.getBorderRadius()
      );
      animStateCSS.put(Property.BORDER_RADIUS, getStringPXfromPixels(borderRadius));
    }

    if (isPropertyUpdated(goalCSS.getBorderWidth(), startCSS.getBorderWidth())
        || isPropertyUpdated(goalCSS.getBorderColor(), startCSS.getBorderColor())) {
      int borderWidth = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getBorderWidth(), new Rect()).left,
          goalCSS.getBorderWidth().left
      );
      int borderColor = getAnimatedColor(
          percent,
          getOrDefault(startCSS.getBorderColor(), SleekColorArea.COLOR_TRANSPARENT),
          goalCSS.getBorderColor()
      );
      animStateCSS.put(
          Property.BORDER,
          getStringPXfromPixels(borderWidth) +
              " solid " + getStringColorFromInt(borderColor)
      );
    }

    if (isPropertyUpdated(goalCSS.getBoxShadowBlurRadius(), startCSS.getBoxShadowBlurRadius())
        || isPropertyUpdated(goalCSS.getBoxShadowColor(), startCSS.getBoxShadowColor())
        || isPropertyUpdated(goalCSS.getBoxShadowOffsetX(), startCSS.getBoxShadowOffsetX())
        || isPropertyUpdated(goalCSS.getBoxShadowOffsetY(), startCSS.getBoxShadowOffsetY())) {
      int offsetXpixels = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getBoxShadowOffsetX(), 0),
          goalCSS.getBoxShadowOffsetX()
      );
      int offsetYpixels = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getBoxShadowOffsetY(), 0),
          goalCSS.getBoxShadowOffsetY()
      );
      int blurRadiusPixels = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getBoxShadowBlurRadius(), 0),
          goalCSS.getBoxShadowBlurRadius()
      );
      int boxShadowColor = getAnimatedColor(
          percent,
          getOrDefault(startCSS.getBoxShadowColor(), SleekColorArea.COLOR_TRANSPARENT),
          goalCSS.getBoxShadowColor()
      );
      animStateCSS.put(
          Property.BOX_SHADOW, getStringPXfromPixels(offsetXpixels)
              + " " + getStringPXfromPixels(offsetYpixels)
              + " " + getStringPXfromPixels(blurRadiusPixels) +
              " " + getStringColorFromInt(boxShadowColor)
      );
    }

    if (isPropertyUpdated(goalCSS.getTextShadowBlurRadius(), startCSS.getTextShadowBlurRadius())
        || isPropertyUpdated(goalCSS.getTextShadowColor(), startCSS.getTextShadowColor())
        || isPropertyUpdated(goalCSS.getTextShadowOffsetX(), startCSS.getTextShadowOffsetX())
        || isPropertyUpdated(goalCSS.getTextShadowOffsetY(), startCSS.getTextShadowOffsetY())) {
      int offsetXpixels = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getTextShadowOffsetX(), 0),
          goalCSS.getTextShadowOffsetX()
      );
      int offsetYpixels = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getTextShadowOffsetY(), 0),
          goalCSS.getTextShadowOffsetY()
      );
      int blurRadiusPixels = getAnimatedInt(
          percent,
          getOrDefault(startCSS.getTextShadowBlurRadius(), 0),
          goalCSS.getTextShadowBlurRadius()
      );
      int textShadowColor = getAnimatedColor(
          percent,
          getOrDefault(startCSS.getTextShadowColor(), SleekColorArea.COLOR_TRANSPARENT),
          goalCSS.getTextShadowColor()
      );
      animStateCSS.put(
          Property.TEXT_SHADOW, getStringPXfromPixels(offsetXpixels)
              + " " + getStringPXfromPixels(offsetYpixels)
              + " " + getStringPXfromPixels(blurRadiusPixels) +
              " " + getStringColorFromInt(textShadowColor)
      );
    }

    sleekElement.refreshCSS();
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

      // Remove animation CSSblock
      sleekElement.removeCSSnoRefresh(animStateCSS);
      sleekElement.refreshCSS();
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

  public static Rect getAnimatedRect(float percent, Rect startInt, Rect goalInt) {
    return new Rect(
        startInt.left + Math.round((goalInt.left - startInt.left) * percent),
        startInt.top + Math.round((goalInt.top - startInt.top) * percent),
        startInt.right + Math.round((goalInt.right - startInt.right) * percent),
        startInt.bottom + Math.round((goalInt.bottom - startInt.bottom) * percent)
    );
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
