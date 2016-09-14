package com.ztory.lib.sleek.base;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;

/**
 * Subclass of SleekBase that has a background color that can have rounded corners.
 * Created by jonruna on 09/10/14.
 */
public class SleekColorArea extends SleekBase {

    public final static int COLOR_TRANSPARENT = 0x00000000;

    public final static boolean
            ANTI_ALIASED_TRUE = true,
            ANTI_ALIASED_FALSE = false;

    protected RectF bounds = new RectF();

    protected Paint paint;

    protected boolean rounded;
    protected int roundRadius;

    public SleekColorArea(int color, boolean isAntiAliased, boolean isLoadable, int theTouchPrio) {
        this(color, isAntiAliased, false, false, isLoadable, theTouchPrio);
    }

    /**
     new SleekColorArea(
         0x99ff0000,
         SleekColorArea.ANTI_ALIASED_TRUE,
         SleekColorArea.FIXED_POSITION_FALSE,
         SleekColorArea.TOUCHABLE_FALSE,
         SleekColorArea.LOADABLE_FALSE,
         SleekColorArea.TOUCH_PRIO_DEFAULT
     );
     * @param color
     * @param isAntiAliased
     * @param isFixedPosition
     * @param isTouchable
     * @param isLoadable
     * @param theTouchPrio
     */
    public SleekColorArea(int color, boolean isAntiAliased, boolean isFixedPosition, boolean isTouchable, boolean isLoadable, int theTouchPrio) {
        super(isFixedPosition, isTouchable, isLoadable, theTouchPrio);

        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(isAntiAliased);
    }

    public void setColor(int color){
        if (paint != null) {
            paint.setColor(color);
        }
    }

    public void setRounded(boolean theRounded, int theRoundRadius) {
        rounded = theRounded;
        roundRadius = theRoundRadius;
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {
        if (rounded) canvas.drawRoundRect(bounds, roundRadius, roundRadius, paint);
        else canvas.drawRect(bounds, paint);
    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {
        super.setSleekBounds(x, y, w, h);
        bounds.set(sleekX, sleekY, sleekX + sleekW, sleekY + sleekH);
    }

}
