package com.ztory.lib.sleek.base;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.SleekParent;
import com.ztory.lib.sleek.contract.IScrollXY;
import com.ztory.lib.sleek.contract.ISleekParentDid;
import com.ztory.lib.sleek.contract.ISleekAnimRun;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.contract.ISleekResize;
import com.ztory.lib.sleek.layout.SleekLayout;
import com.ztory.lib.sleek.touch.SleekTouchHandler;

import java.util.ArrayList;

/**
 * Base implementation of Sleek interface. Also adding animation functionality by wrapping the draw
 * within calls to the ISleekAnimView member instances animTickStart() and animTickEnd() methods.
 * By extending this class you get a simple way of creating custom Sleek implementations without
 * having to care to much about boilerplate code.
 * Created by jonruna on 09/10/14.
 */
public class SleekBase implements Sleek, ISleekDrawView {

    public static void addPreDrawSafe(
            final SleekCanvas theSleekCanvas,
            final ISleekAnimRun theRun
    ) {
        if (theSleekCanvas != null && theRun != null) {
            theSleekCanvas.addPreDrawRun(theRun);
        }
    }

    private static void callSafeIParentDid(
            ISleekParentDid theIParentDid,
            SleekCanvas sCanvas,
            SleekParent sViewCollection
    ) {
        if (theIParentDid != null) {
            theIParentDid.parentDid(sCanvas, sViewCollection);
        }
    }

    public static Sleek[] getSleekParents(Sleek sleek) {
        if (sleek.getSleekParent() == null) {
            return new Sleek[0];
        }

        ArrayList<Sleek> svcArrayList = new ArrayList<>(4);
        svcArrayList.add(sleek.getSleekParent());

        Sleek iterSlkViewColl = sleek.getSleekParent().getSleekParent();

        while (iterSlkViewColl != null) {
            svcArrayList.add(iterSlkViewColl);
            iterSlkViewColl = iterSlkViewColl.getSleekParent();
        }

        return svcArrayList.toArray(
                new Sleek[svcArrayList.size()]
        );
    }

    public static float[] getSleekParentsAggregatedPos(
            Sleek[] sleekParentArray,
            boolean includeParentScroll
    ) {

        float aggregatedX = 0.0f, aggregatedY = 0.0f;

        for (Sleek iterSvcParent : sleekParentArray) {
            aggregatedX += iterSvcParent.getSleekX();
            aggregatedY += iterSvcParent.getSleekY();

            if (includeParentScroll) {
                if (iterSvcParent instanceof IScrollXY) {
                    aggregatedX += ((IScrollXY) iterSvcParent).getScrollX();
                    aggregatedY += ((IScrollXY) iterSvcParent).getScrollY();
                }
            }
        }

        return new float[] { aggregatedX, aggregatedY };
    }

    public final static boolean
            FIXED_POSITION_TRUE = true,
            FIXED_POSITION_FALSE = false,
            TOUCHABLE_TRUE = true,
            TOUCHABLE_FALSE = false,
            LOADABLE_TRUE = true,
            LOADABLE_FALSE = false;

    public final static int
            TOUCH_PRIO_DEFAULT = 0;

    protected SleekTouchHandler touchHandler;
    protected ISleekResize resizeListener;

    protected ISleekAnimView animView = ISleekAnimView.NO_ANIMATION;
    private ISleekAnimView drawAnimView;

    private ISleekDrawView sleekDrawView = ISleekDrawView.NO_DRAW;

    protected float sleekX, sleekY;
    protected int sleekW, sleekH;
    protected volatile boolean loaded, addedToParent = false;

    protected boolean fixedPosition, touchable, loadable;
    protected int touchPrio;

    protected ISleekParentDid parentDidAddListener, parentDidRemoveListener;

    protected SleekCanvas mSlkCanvas;
    protected SleekParent mSlkParent;

    private boolean mHasSlkLay = false, mHasSlkLayLandscape = false;
    private SleekLayout mSleekLayout, mSleekLayoutLandscape;

    private boolean loadOnAdd = false, unloadOnRemove = false, execOnSizeChanged = false;

    public SleekBase(SleekParam sleekParam) {
        this(sleekParam.fixed, sleekParam.touchable, sleekParam.loadable, sleekParam.priority);
    }

    public SleekBase(
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio
    ) {
        fixedPosition = isFixedPosition;
        touchable = isTouchable;
        loadable = isLoadable;
        touchPrio = theTouchPrio;

        if (touchable) {
            touchHandler = new SleekTouchHandler(SleekBase.this);
        }

        setSleekDrawView(this);// use ISleekDrawView to support animations while drawing
    }

    public void executeSleekCanvasResize() {
        final SleekCanvas theSleekCanvas = mSlkCanvas;
        if (theSleekCanvas != null) {
            theSleekCanvas.addPreDrawRun(new ISleekAnimRun() {
                @Override
                public void run(SleekCanvasInfo info) {
                    theSleekCanvas.executeResize();
                }
            });
        }
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {
        // Override this to do your drawing, alternatively pass a ISleekDrawView to the setSleekDrawView() method
    }

    @Override
    public void onSleekCanvasResize(SleekCanvasInfo info) {

        if (mHasSlkLay) {
            applyLayout(info);
        }

        if (resizeListener != null) {
            resizeListener.onResize(this, info);
        }
    }

    @Override
    public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo info) {
        return touchHandler.onSleekTouchEvent(event, info);
    }

    @Override
    public void onSleekDraw(Canvas canvas, SleekCanvasInfo info) {

        if (execOnSizeChanged) {
            execOnSizeChanged = false;
            onSleekCanvasResize(info);
        }

        // Must use drawAnimView to have a steady reference during the draw call.
        // if setSleekAnimView() is called before the previous animation has called animTickEnd == BUG RISK
        // NOTE: added drawAnimView.equals(animView) below so this should no longer be a problem!
        drawAnimView = animView;

        drawAnimView.animTickStart(this, canvas, info);
        sleekDrawView.drawView(this, canvas, info);

        if (// if animation is complete and drawAnimView is still equal to animView
                drawAnimView.animTickEnd(this, canvas, info) &&
                drawAnimView.equals(animView)
                ) {
            setSleekAnimView(null);
        }
    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {
        sleekX = x;
        sleekY = y;
        sleekW = w;
        sleekH = h;
    }

    @Override
    public float getSleekX() {
        return sleekX;
    }

    @Override
    public float getSleekY() {
        return sleekY;
    }

    @Override
    public int getSleekW() {
        return sleekW;
    }

    @Override
    public int getSleekH() {
        return sleekH;
    }

    @Override
    public boolean isSleekFixedPosition() {
        return fixedPosition;
    }

    @Override
    public boolean isSleekTouchable() {
        return touchable;
    }

    @Override
    public int getSleekPriority() {
        return touchPrio;
    }

    @Override
    public boolean isSleekReadyToDraw() {
        // returns true if the view is not loadable or if the view is loaded
        return !loadable || loaded;
    }

    @Override
    public boolean isSleekLoaded() {
        return loaded;
    }

    @Override
    public boolean isSleekLoadable() {
        return loadable;
    }

    @Override
    public void onSleekLoad(SleekCanvasInfo info) {
        loaded = true;
    }

    @Override
    public void onSleekUnload() {
        loaded = false;
    }

    @Override
    public void onSleekParentAdd(SleekCanvas sleekCanvas, SleekParent composite) {

        mSlkCanvas = sleekCanvas;
        mSlkParent = composite;

        addedToParent = true;

        if (loadOnAdd) {
            mSlkCanvas.loadSleek(this);
        }

        callSafeIParentDid(parentDidAddListener, sleekCanvas, composite);
    }

    @Override
    public void onSleekParentRemove(SleekCanvas sleekCanvas, SleekParent composite) {

        addedToParent = false;

        callSafeIParentDid(parentDidRemoveListener, sleekCanvas, composite);

        if (unloadOnRemove) {
            onSleekUnload();
        }

        mSlkCanvas = null;
        mSlkParent = null;
    }

    @Override
    public SleekCanvas getSleekCanvas() {
        return mSlkCanvas;
    }

    /** This will call onSleekCanvasSizeChanged() before next draw() call. */
    public void requestLayout() {
        execOnSizeChanged = true;
        invalidateSafe();
    }

    public boolean isAddedToParent() {
        return addedToParent;
    }

    public void invalidateSafe() {
        if (mSlkCanvas != null) {
            mSlkCanvas.invalidateSafe();
        }
    }

    public void invalidatePreDrawRun() {

        addPreDrawSafe(
                mSlkCanvas,
                new ISleekAnimRun() {
                    @Override
                    public void run(SleekCanvasInfo info) {

                        info.invalidate();

                        addPreDrawSafe(
                                mSlkCanvas,
                                new ISleekAnimRun() {
                                    @Override
                                    public void run(SleekCanvasInfo info) {
                                        info.invalidate();
                                    }
                                }
                        );
                    }
                }
        );
    }

    @Override
    public SleekParent getSleekParent() {
        return mSlkParent;
    }

    public void setLoadOnAdd(boolean theLoadOnAdd) {
        loadOnAdd = theLoadOnAdd;
    }

    public void setUnloadOnRemove(boolean theUnloadOnRemove) {
        unloadOnRemove = theUnloadOnRemove;
    }

    public void parentAdd(final SleekCanvas sleekCanvas) {
        if (sleekCanvas == null) {
            return;
        }
        sleekCanvas.addPreDrawRun(new ISleekAnimRun() {
            @Override
            public void run(SleekCanvasInfo info) {
                sleekCanvas.addSleek(SleekBase.this);
            }
        });
    }

    public void parentRemove(boolean preDrawRun) {
        if (!addedToParent) {
            return;
        }

        if (mSlkParent != null) {
            if (preDrawRun) {
                final SleekParent finalParent = mSlkParent;
                mSlkCanvas.addPreDrawRun(
                        new ISleekAnimRun() {
                            @Override
                            public void run(SleekCanvasInfo info) {
                                finalParent.removeSleek(SleekBase.this);
                            }
                        }
                );
            }
            else {
                mSlkParent.removeSleek(SleekBase.this);
            }
        }
        else {
            if (preDrawRun) {
                final SleekCanvas finalSleekCanvas = mSlkCanvas;
                mSlkCanvas.addPreDrawRun(
                        new ISleekAnimRun() {
                            @Override
                            public void run(SleekCanvasInfo info) {
                                finalSleekCanvas.removeSleek(SleekBase.this);
                            }
                        }
                );
            }
            else {
                mSlkCanvas.removeSleek(SleekBase.this);
            }
        }

        invalidateSafe();
    }

    public void setParentDidAddListener(ISleekParentDid theParentDidAddListener) {
        parentDidAddListener = theParentDidAddListener;
    }

    public void setParentDidRemoveListener(ISleekParentDid theParentDidRemoveListener) {
        parentDidRemoveListener = theParentDidRemoveListener;
    }

    public SleekTouchHandler getTouchHandler() {
        return touchHandler;
    }

    public void setTouchHandler(SleekTouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public void setResizeListener(ISleekResize canvasResize) {
        this.resizeListener = canvasResize;
    }

    public void setSleekAnimView(ISleekAnimView animView) {
        this.animView = animView;
        if (this.animView == null) {
            this.animView = ISleekAnimView.NO_ANIMATION;
        }

        invalidateSafe();
    }

    public void setSleekDrawView(ISleekDrawView sleekDrawView) {
        this.sleekDrawView = sleekDrawView;
        if (this.sleekDrawView == null) {
            this.sleekDrawView = ISleekDrawView.NO_DRAW;
        }
    }

    public void applyLayout(SleekCanvasInfo info) {
        if (!mHasSlkLay && !mHasSlkLayLandscape) {// NO layout set
            return;
        }
        else if (mHasSlkLay && mHasSlkLayLandscape) {// BOTH layouts set

            if (info.width > info.height) {//if SleekCanvas width is larger == landscape
                getLayoutLandscape().apply(this, info);
            }
            else {
                getLayout().apply(this, info);
            }

            return;
        }

        //if only one layout is used, use getLayout()
        getLayout().apply(this, info);
    }

    public boolean hasLayout() {
        return mHasSlkLay;
    }

    /**
     Usage:
     SleekViewBase.getLayout()
         .x(SL.X.EAST_OF, marginX, coverImage)
         .y(SL.Y.SOUTH_OF, marginY, titleLabel)
         .w(SL.W.MATCH_PARENT, marginHorizontal, Sleek.this)
         .h(SL.H.ABSOLUTE, absoluteHeight, null);
     */
    public SleekLayout getLayout() {
        if (!mHasSlkLay) {//create on demand
            mHasSlkLay = true;
            mSleekLayout = SleekLayout.create();
        }
        return mSleekLayout;
    }

    public SleekLayout getLayoutLandscape() {
        if (!mHasSlkLayLandscape) {//create on demand
            mHasSlkLayLandscape = true;
            mSleekLayoutLandscape = SleekLayout.create();
        }
        return mSleekLayoutLandscape;
    }

}
