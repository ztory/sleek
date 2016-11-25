package com.ztory.lib.sleek.base;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.SleekParent;

import java.util.ArrayList;
import java.util.List;

/**
 * SleekBase subclass that also implements SleekParent interface and supports drawing and
 * touch-interactions of child Sleek instances.
 * Created by jonruna on 09/10/14.
 */
public class SleekBaseComposite extends SleekBase implements SleekParent {

    protected SleekCanvas mCompositeCanvas;

    protected ArrayList<Sleek> views = new ArrayList<>();

    protected RectF bounds = new RectF();
    protected boolean touchEventReturn, shouldDraw;

    private int canvasWidth, canvasHeight;

    private SleekCanvasInfo canvasInfo;

    private boolean dimensionIgnoreBounds = false, calcBoundsOnResize = false;

    public SleekBaseComposite(SleekParam sleekParam) {
        this(sleekParam.fixed, sleekParam.priority);
    }

    public SleekBaseComposite(boolean isFixedPosition, int theTouchPrio) {
        super(isFixedPosition, true, true, theTouchPrio);
        fixedPosition = isFixedPosition;
        touchPrio = theTouchPrio;
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {
        canvas.save();
        canvas.translate(sleekX, sleekY);

        for (Sleek iterView : views) {
            //if (iterView.shouldBeDrawn()) iterView.draw(canvas, info);// dont need to check this, if this view is loaded it should draw its children
            iterView.onSleekDraw(canvas, info);
        }
        canvas.restore();
    }

    @Override
    public void onSleekCanvasResize(SleekCanvasInfo info) {

        canvasWidth = info.width;
        canvasHeight = info.height;

        canvasInfo = info;

        super.onSleekCanvasResize(info);

        for (Sleek iterView : views) {
            iterView.onSleekCanvasResize(info);
        }

        if (calcBoundsOnResize) {
            calcBounds(null);
        }
    }

    @Override
    public float getSleekX() {
        if (dimensionIgnoreBounds) {
            return sleekX;
        }
        else {
            return sleekX + bounds.left;
        }
    }

    @Override
    public float getSleekY() {
        if (dimensionIgnoreBounds) {
            return sleekY;
        }
        else {
            return sleekY + bounds.top;
        }
    }

    @Override
    public int getSleekW() {
        if (dimensionIgnoreBounds) {
            return sleekW;
        }
        else {
            return Math.round(bounds.width());
        }
    }

    @Override
    public int getSleekH() {
        if (dimensionIgnoreBounds) {
            return sleekH;
        }
        else {
            return Math.round(bounds.height());
        }
    }

    @Override
    public boolean isSleekFixedPosition() {
        return fixedPosition;
    }

    @Override
    public boolean isSleekTouchable() {
        return true;
    }

    @Override
    public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo info) {

        if (!touchable) return false;

        event.offsetLocation(-sleekX, -sleekY);

        touchEventReturn = false;

        for (int i = views.size() - 1; i >= 0; i--) {
            if (views.get(i).isSleekTouchable() && views.get(i).onSleekTouchEvent(event, info)) {
                touchEventReturn = true;
                break;
            }
        }

        event.offsetLocation(sleekX, sleekY);

        return touchEventReturn;
    }

    @Override
    public int getSleekPriority() {
        return touchPrio;
    }

    @Override
    public boolean isSleekReadyToDraw() {
        return shouldDraw;
    }

    @Override
    public boolean isSleekLoaded() {
        return loaded;// to draw child views that are not loadable
    }

    @Override
    public boolean isSleekLoadable() {
        return true;// so that load and unload gets called
    }

    @Override
    public void onSleekLoad(SleekCanvasInfo info) {
        loaded = true;
        shouldDraw = true;
        for (Sleek iterView : views) {
            if (iterView.isSleekLoadable() && !iterView.isSleekLoaded()) iterView.onSleekLoad(info);
        }
    }

    /** If any of this collections children are loadable == false, then this view will always draw itself. */
    @Override
    public void onSleekUnload() {
        loaded = false;
        shouldDraw = false;
        for (Sleek iterView : views) {
            if (iterView.isSleekLoadable() && iterView.isSleekLoaded()) iterView.onSleekUnload();
            else if (!iterView.isSleekLoadable()) shouldDraw = true;
        }
    }

    @Override
    public void onSleekParentAdd(SleekCanvas sleekCanvas, SleekParent composite) {
        super.onSleekParentAdd(sleekCanvas, composite);

        this.mCompositeCanvas = sleekCanvas;

        for (Sleek iterView : views) {
            iterView.onSleekParentAdd(sleekCanvas, SleekBaseComposite.this);
        }
    }

    @Override
    public void onSleekParentRemove(SleekCanvas sleekCanvas, SleekParent composite) {
        super.onSleekParentRemove(sleekCanvas, composite);

        for (Sleek iterView : views) {
            iterView.onSleekParentRemove(sleekCanvas, SleekBaseComposite.this);
        }
    }

    public int getViewCount() {
        return views.size();
    }

    /**
     * If this is set to TRUE then getSleekXYWH() methods will not care about the bounds RectF when
     * returning its values.
     * @param theDimensionIgnoreBounds
     */
    public void setDimensionIgnoreBounds(boolean theDimensionIgnoreBounds) {
        dimensionIgnoreBounds = theDimensionIgnoreBounds;
    }

    public void setCalcBoundsOnResize(boolean theCalcBoundsOnResize) {
        calcBoundsOnResize = theCalcBoundsOnResize;
    }

    @Override
    public void addSleek(Sleek addView) {
        addSleek(addView, true);
    }

    public void addSleek(Sleek addView, boolean loadViewOnAdd) {
        addSleek(addView, loadViewOnAdd, views.size());
    }

    public void addSleek(Sleek addView, boolean loadViewOnAdd, int addIndex) {
        views.add(addIndex, addView);

        if (canvasInfo != null) {// Call child views onResize when view is added at runtime
            addView.onSleekCanvasResize(canvasInfo);
            if (
                    loadViewOnAdd
                    && isSleekLoadable()
                    && isSleekLoaded() &&
                    addView.isSleekLoadable()
                    ) {// load child if this view is loaded
                addView.onSleekLoad(canvasInfo);
            }
        }

        //call addView.parentDidAddView() if SleekViewCollection.this is addedToParent==true
        if (addedToParent) {
            addView.onSleekParentAdd(mCompositeCanvas, SleekBaseComposite.this);
        }

        refreshTouchable(addView);
        calcBounds(addView);

        invalidateSafe();
    }

    public void removeSleekAll() {
        removeSleekAll(true);
    }

    public void removeSleekAll(boolean unloadRemovedViews) {

        Sleek removeView;

        for (int i = views.size() - 1; i >= 0; i--) {

            removeView = views.remove(i);

            if (unloadRemovedViews) {
                removeView.onSleekUnload();
            }

            if (addedToParent) {
                removeView.onSleekParentRemove(mCompositeCanvas, SleekBaseComposite.this);
            }
        }

        refreshTouchable(null);
        calcBounds(null);

        invalidateSafe();
    }

    @Override
    public void removeSleek(Sleek removeView) {
        if (removeView == null) {
            return;
        }

        views.remove(removeView);
        removeView.onSleekUnload();

        //call removeView.parentDidRemoveView() if SleekViewCollection.this is addedToParent==true
        if (addedToParent) {
            removeView.onSleekParentRemove(mCompositeCanvas, SleekBaseComposite.this);
        }

        // iterates all items that are left in views array
        refreshTouchable(null);
        calcBounds(null);

        invalidateSafe();
    }

    @Override
    public List<Sleek> getSleekChildren() {
        return views;
    }

    protected void refreshTouchable(Sleek sleekView) {
        if (sleekView != null) {
            if (sleekView.isSleekTouchable()) touchable = true;
            return;
        }

        touchable = false;

        for (Sleek iterView : views) {
            if (iterView.isSleekTouchable()) touchable = true;
        }
    }

    public void calcBounds(Sleek sleekView) {

        if (sleekView != null) {
            if (sleekView.getSleekX() < bounds.left) bounds.left = sleekView.getSleekX();
            if (sleekView.getSleekX() + sleekView.getSleekW() > bounds.right) bounds.right = sleekView.getSleekX() + sleekView.getSleekW();

            if (sleekView.getSleekY() < bounds.top) bounds.top = sleekView.getSleekY();
            if (sleekView.getSleekY() + sleekView.getSleekH() > bounds.bottom) bounds.bottom = sleekView.getSleekY() + sleekView.getSleekH();
            return;
        }

        bounds = new RectF();

        for (Sleek iterView : views) {
            if (iterView.getSleekX() < bounds.left) bounds.left = iterView.getSleekX();
            if (iterView.getSleekX() + iterView.getSleekW() > bounds.right) bounds.right = iterView.getSleekX() + iterView.getSleekW();

            if (iterView.getSleekY() < bounds.top) bounds.top = iterView.getSleekY();
            if (iterView.getSleekY() + iterView.getSleekH() > bounds.bottom) bounds.bottom = iterView.getSleekY() + iterView.getSleekH();
        }
    }

    public RectF getBoundsRectF() {
        return bounds;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

}
