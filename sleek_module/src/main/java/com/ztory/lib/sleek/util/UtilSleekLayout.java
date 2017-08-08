package com.ztory.lib.sleek.util;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.base.element.SleekElement;
import com.ztory.lib.sleek.contract.ISleekResize;
import com.ztory.lib.sleek.layout.SL.H;
import com.ztory.lib.sleek.layout.SL.W;
import com.ztory.lib.sleek.layout.SL.X;
import com.ztory.lib.sleek.layout.SL.Y;
import java.util.List;

/**
 * Util functions related to layout functionality.
 * Created by jonruna on 09/10/14.
 */
public class UtilSleekLayout {

  public static void initVerticalListLayout(List<? extends SleekBase> sleekBaseList) {
    SleekBase layoutParent = null;
    for (SleekBase iterSleekBase : sleekBaseList) {
      if (layoutParent == null) {// First view
        iterSleekBase.getLayout()
            .x(X.POS_CENTER, 0, null)
            .y(Y.ABSOLUTE, UtilPx.getPixels(140), null)
            .w(W.ABSOLUTE, UtilPx.getPixels(400), null)
            .h(H.ABSOLUTE, UtilPx.getPixels(300), null);
      } else {
        iterSleekBase.getLayout()
            .x(X.POS_CENTER, 0, layoutParent)
            .y(Y.SOUTH_OF, UtilPx.getPixels(40), layoutParent)
            .w(W.MATCH_PARENT, 0, layoutParent)
            .h(H.MATCH_PARENT, 0, layoutParent);
      }
      layoutParent = iterSleekBase;
    }
  }

  public static int getWidthSplit(
      int widthToSplit,
      float splitSpacing,
      float splitCount
  ) {
    return (int) ((widthToSplit - (splitSpacing * (splitCount + 1.0f))) / splitCount);
  }

  public static int getWidthMax(
      int baseWidth,
      int maxWidth
  ) {
    return (baseWidth > maxWidth) ? maxWidth : baseWidth;
  }

  public static float getAlignCenterHoriz(
      int drawItemWidth,
      float containerX,
      int containerWidth
  ) {
    return Calc.roundFloat(containerX + ((containerWidth - drawItemWidth) / 2.0f));
  }

  public static float getAlignCenterVert(
      int drawItemHeight,
      float containerY,
      int containerHeight
  ) {
    return Calc.roundFloat(containerY + ((containerHeight - drawItemHeight) / 2.0f));
  }

  public static void alignCenter(
      Sleek drawItem,
      int containerX,
      int containerY,
      int containerWidth,
      int containerHeight
  ) {
    drawItem.setSleekBounds(
        Calc.roundFloat(containerX + ((containerWidth - drawItem.getSleekW()) / 2.0f)),
        Calc.roundFloat(containerY + ((containerHeight - drawItem.getSleekH()) / 2.0f)),
        drawItem.getSleekW(),
        drawItem.getSleekH()
    );
  }

  public static ISleekResize getBottomRightLayout(final Sleek drawItem) {
    ISleekResize returnLayout = new ISleekResize() {
      @Override
      public void onResize(Sleek sleekView, SleekCanvasInfo drawInfo) {
        drawItem.setSleekBounds(
            drawInfo.width - drawItem.getSleekW(),
            drawInfo.height - drawItem.getSleekH(),
            drawItem.getSleekW(),
            drawItem.getSleekH()
        );
      }
    };
    return returnLayout;
  }

}
