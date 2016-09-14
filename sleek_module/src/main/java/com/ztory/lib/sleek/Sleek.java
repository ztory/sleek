package com.ztory.lib.sleek;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Github: https://github.com/ztory/sleek
 * <br/><br/>
 * The base interface for all things Sleek.<br/>
 * The idea is that a Sleek instance:<br/>
 * - Has a size<br/>
 * - Knows how to draw itself<br/>
 * - Can handle touch if isSleekTouchable() == TRUE<br/>
 * - Can be loaded and unloaded if isSleekLoadable() == TRUE<br/>
 * A SleekCanvas is used to draw and delegate touch to the Sleek instances.
 * If implementations want to have SleekCanvas and SleekParent references then those references
 * can be initialized and released in the onSleekParentAdd() and onSleekParentRemove() methods
 * respectively, SleekCanvas is never null, but SleekParent is null if this Sleek is added directly
 * to the SleekCanvas.
 * Created by jonruna on 09/10/14.
 */
public interface Sleek {

    void onSleekDraw(Canvas canvas, SleekCanvasInfo info);

    void onSleekCanvasResize(SleekCanvasInfo info);

    void setSleekBounds(float x, float y, int w, int h);
    float getSleekX();
    float getSleekY();
    int getSleekW();
    int getSleekH();

    boolean isSleekFixedPosition();

    boolean isSleekTouchable();
    boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo info);
    int getSleekPriority();

    boolean isSleekReadyToDraw();

    boolean isSleekLoaded();
    boolean isSleekLoadable();
    void onSleekLoad(SleekCanvasInfo info);
    void onSleekUnload();

    void onSleekParentAdd(SleekCanvas sleekCanvas, SleekParent sleekParent);
    void onSleekParentRemove(SleekCanvas sleekCanvas, SleekParent sleekParent);

    SleekCanvas getSleekCanvas();
    SleekParent getSleekParent();

}
