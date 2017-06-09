package com.ztory.lib.sleek.base.element;

import android.graphics.Canvas;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.animation.SAVpercent.PercentDrawView;
import com.ztory.lib.sleek.animation.SleekAnimation;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;

/**
 * Created by jonruna on 2017-06-09.
 */
public class SleekCSSanim extends SleekAnimation implements PercentDrawView {

  protected final SleekElement sleekElement;
  protected final CSSblock startCSS, goalCSS, animCSS;
  protected boolean animatingBounds = false;
  protected float startX, startY, goalX, goalY;
  protected int startW, startH, goalW, goalH;

  public SleekCSSanim(SleekElement theSleekElement, CSSblock targetCSSblock) {
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

    CSSblock sleekElementCSS = sleekElement.getCSS();
    animCSS = new CSSblockBase(sleekElementCSS.size());
    animCSS.putAll(sleekElementCSS);
    startCSS = new CSSblockBase(sleekElementCSS.size());
    startCSS.putAll(sleekElementCSS);
    goalCSS = new CSSblockBase(sleekElementCSS.size());
    goalCSS.putAll(sleekElementCSS);
    goalCSS.putAll(targetCSSblock);
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
  }

  @Override
  public void percentDrawEnd(float percent, Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

    if (animatingBounds) {
      sleekElement.setSleekBounds(
          goalX,
          goalY,
          goalW,
          goalH
      );
    }

  }

}
