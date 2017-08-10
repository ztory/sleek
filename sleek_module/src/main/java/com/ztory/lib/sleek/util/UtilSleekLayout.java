package com.ztory.lib.sleek.util;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.base.element.SleekElement;
import com.ztory.lib.sleek.contract.ISleekResize;
import com.ztory.lib.sleek.layout.IComputeFloat;
import com.ztory.lib.sleek.layout.SL.H;
import com.ztory.lib.sleek.layout.SL.W;
import com.ztory.lib.sleek.layout.SL.X;
import com.ztory.lib.sleek.layout.SL.Y;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Util functions related to layout functionality.
 * Created by jonruna on 09/10/14.
 */
public class UtilSleekLayout {

  public static void initVerticalGridLayout(
      final List<? extends SleekBase> sleekBaseList,
      final int columnWidth,
      final int topSpacing,
      final int horizontalSpacing,
      final boolean verticalSpacingMatchHorizontalSpacing,
      final int verticalSpacing
  ) {
    int index = 0;
    SleekBase layoutParent = null;
    final AtomicInteger rowMaxHeight = new AtomicInteger(0);
    for (SleekBase iterSleekBase : sleekBaseList) {
      final int iterIndex = index;
      final SleekBase iterParent = layoutParent;
      iterSleekBase.getLayout()
          .x(X.COMPUTE, 0, null, 0.0f, new IComputeFloat() {
            @Override
            public float compute(SleekCanvasInfo info) {
              int cellWidth = columnWidth + horizontalSpacing + horizontalSpacing;
              int columnCount = (int) ((float) info.width / (float) cellWidth);
              int rowIndex = (int) ((float) iterIndex / (float) columnCount);
              int colIndex = iterIndex - (rowIndex * columnCount);
              int realHorizontalSpacing = (info.width - (columnCount * columnWidth)) / (columnCount + 1);
              int posX = realHorizontalSpacing + ((columnWidth + realHorizontalSpacing) * colIndex);
              return posX;
            }
          })
          .y(Y.COMPUTE, 0, null, 0.0f, new IComputeFloat() {
            @Override
            public float compute(SleekCanvasInfo info) {
              int cellWidth = columnWidth + horizontalSpacing + horizontalSpacing;
              int columnCount = (int) ((float) info.width / (float) cellWidth);
              int rowIndex = (int) ((float) iterIndex / (float) columnCount);
              int colIndex = iterIndex - (rowIndex * columnCount);
              float posY;

              int realVerticalSpacing = verticalSpacing;
              if (verticalSpacingMatchHorizontalSpacing) {
                int realHorizontalSpacing = (info.width - (columnCount * columnWidth)) / (columnCount + 1);
                realVerticalSpacing = realHorizontalSpacing;
              }

              if (iterParent != null) {
                if (rowMaxHeight.get() < iterParent.getSleekH()) {
                  rowMaxHeight.set(iterParent.getSleekH());
                }
              }

              if (colIndex == 0) {
                if (iterParent == null) {
                  posY = topSpacing;//first SleekBase instance
                } else {
                  posY = iterParent.getSleekY() + rowMaxHeight.get() + realVerticalSpacing;
                  rowMaxHeight.set(0);
                }
              } else {
                //TODO HOW TO BOTTOM ALIGN PREVIOUS VIEWS IN ROW ????
                posY = iterParent.getSleekY();
              }

              return posY;
            }
          });
      layoutParent = iterSleekBase;
      index++;
    }
  }

  public static void initVerticalListLayout(
      List<? extends SleekBase> sleekBaseList, int topSpacing, int spacing
  ) {
    SleekBase layoutParent = null;
    for (SleekBase iterSleekBase : sleekBaseList) {
      if (layoutParent == null) {// First view
        iterSleekBase.getLayout()
            .x(X.POS_CENTER, 0, null)
            .y(Y.ABSOLUTE, topSpacing, null);
      } else {
        iterSleekBase.getLayout()
            .x(X.POS_CENTER, 0, layoutParent)
            .y(Y.SOUTH_OF, spacing, layoutParent);
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
