package com.ztory.lib.sleek.util;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.contract.ISleekResize;

/**
 * Util functions related to layout functionality.
 * Created by jonruna on 09/10/14.
 */
public class UtilSleekLayout {

    public static int getWidthSplit(
            int widthToSplit,
            float splitSpacing,
            float splitCount
            ) {
        return (int) ( (widthToSplit - (splitSpacing * (splitCount + 1.0f) ) ) / splitCount);
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
