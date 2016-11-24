package com.ztory.lib.sleek.base.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBase;


/**
 * Base class for displaying a Bitmap in the Sleek-framework.
 * Created by jonruna on 24/11/16.
 */
public class SleekBaseIcon extends SleekBase {

    protected Paint paint = new Paint();

    protected Bitmap image;
    protected Drawable drawable;

    protected Rect sourceRect = new Rect();
    protected RectF imageScaledSize = new RectF();

    protected boolean bitmapMode, initialized = false;

    public SleekBaseIcon(
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio
    ) {
        super(isFixedPosition, isTouchable, isLoadable, theTouchPrio);

        bitmapMode = true;
    }

    public SleekBaseIcon(
            Bitmap theBitmap,
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio
    ) {
        super(isFixedPosition, isTouchable, isLoadable, theTouchPrio);

        bitmapMode = true;

        initBitmap(theBitmap, true);
    }

    public SleekBaseIcon(
            Drawable theDrawable,
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio
    ) {
        super(isFixedPosition, isTouchable, isLoadable, theTouchPrio);

        initialized = true;

        drawable = theDrawable;
        image = null;

        bitmapMode = false;

        setSleekBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public void initBitmap(Bitmap theBitmap, boolean setSleekBounds) {

        if (theBitmap == null) {
            initialized = false;
            image = null;
            return;
        }

        initialized = true;

        image = theBitmap;
        drawable = null;

        sourceRect.set(0, 0, image.getWidth(), image.getHeight());

        if (setSleekBounds) {
            setSleekBounds(0, 0, image.getWidth(), image.getHeight());
        }
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {
        super.setSleekBounds(x, y, w, h);

        imageScaledSize.set(
                sleekX,
                sleekY,
                sleekX + sleekW,
                sleekY + sleekH
        );

        if (!bitmapMode) {
            drawable.setBounds(
                    (int) imageScaledSize.left,
                    (int) imageScaledSize.top,
                    (int) imageScaledSize.right,
                    (int) imageScaledSize.bottom
            );
        }
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        if (!initialized) {
            return;
        }

        if (bitmapMode) {
            canvas.drawBitmap(
                    image,
                    sourceRect,
                    imageScaledSize,
                    paint
            );
        }
        else {
            drawable.draw(canvas);
        }
    }

}
