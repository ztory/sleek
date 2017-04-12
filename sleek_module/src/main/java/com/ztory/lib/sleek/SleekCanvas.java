package com.ztory.lib.sleek;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.contract.ISleekAnimRun;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.util.UtilSleekMath;
import com.ztory.lib.sleek.util.UtilSleekTouch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Github: https://github.com/ztory/sleek
 * <br/><br/>
 * SleekCanvas is the link between the Android-UI-world and the Sleek-UI-world.
 * SleekCanvas has a couple of obvious use cases:<br/>
 * 1.<br/>
 * SleekCanvas can handle the entire UI of an app. It is much less overhead than the regular
 * Android-UI-framework when adding nested views and layouts inside of the SleekCanvas.
 * It uses the <code>android.graphics.Canvas</code> to draw its Sleek instances, so everythhing
 * you can draw on a Canvas you can also draw with a Sleek.
 * SleekCanvas overrides the <code>View.onTouchEvent()</code> method to delegate touch events to
 * Sleek instances, so again, anything you can do in the <code>View.onTouchEvent()</code> method
 * you can also do in a Sleek implementation.<br/>
 * 2.<br/>
 * SleekCanvas can be used as a custom View inside of the regular Android view framework.
 * You can use it to build custom views in a fast and predictable manner, adding image, text and
 * shapes in near infinite nesting without loosing unnecessary performance.<br/>
 * 3.<br/>
 * SleekCanvas can be used as a custom ViewGroup but with the power and flexibilty of the
 * positioning logic, responsive touch and predictable layout. If you choose to add regular View
 * subclasses to the SleekCanvas then you can use the classes in the
 * com.ztory.lib.sleek.base.androidui package.<br/>
 * <br/>
 * Created by jonruna on 09/10/14.
 */
public class SleekCanvas extends RelativeLayout {

    public static final int STICKY_TOUCH_PRIO = Integer.MAX_VALUE - 10000;

    protected final SleekCanvasInfo drawInfo = new SleekCanvasInfo();

    protected final Object canvasLockObj = new Object();
    protected final Object preRunLockObj = new Object();

    protected AtomicInteger atomInt = new AtomicInteger(0);

    protected AtomicInteger drawPrioAtomInt = new AtomicInteger(0);

    protected ISleekAnimRun animRun;
    protected boolean hasAnimRun = false;

    protected ArrayList<ISleekAnimRun> preDrawRunList;
    protected boolean hasPreDrawItems = false;

    protected ArrayList<Sleek> drawItemList;
    protected ArrayList<Sleek> touchItemList;
    protected ArrayList<Sleek> drawFixedItemList;
    protected ArrayList<Sleek> touchFixedItemList;

    protected int activeTouchIndex = -1;
    protected int activeFixedTouchIndex = -1;
    protected Sleek activeTouchView;

    protected int widthLoadPadding = 0;
    protected int heightLoadPadding = 0;

    protected boolean loadPaddingEqualToSize = true;

    protected boolean hasSleekScroller;
    protected SleekCanvasScroller sleekScroller;

    protected ISleekAnimView scrollerAnimView = ISleekAnimView.NO_ANIMATION;
    protected ISleekAnimView scrollerDrawAnimView;

    protected boolean blockDraw = false;

    protected boolean autoInvalidate = true;

    protected int activityId, uiParentId;

    protected boolean shouldCallWindowDetachListener = true;
    protected Runnable windowDetachListener = null;

    protected float[] scrollEdges = null;

    public SleekCanvas(Context context) {
        this(context, null, 0, 0);
    }

    public SleekCanvas(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public SleekCanvas(Context context, int theActivityId) {
        this(context, null, theActivityId, theActivityId);//uiParentId==activityId
    }

    public SleekCanvas(Context context, int theActivityId, int theUiParentId) {
        this(context, null, theActivityId, theUiParentId);
    }

    public SleekCanvas(Context context, AttributeSet attrs, int theActivityId, int theUiParentId) {
        super(context, attrs);

        initView(theActivityId, theUiParentId);

        // This enables SleekCanvas to have Focus
        setDescendantFocusability(RelativeLayout.FOCUS_BEFORE_DESCENDANTS);
        setFocusableInTouchMode(true);

        setAutoInvalidate(false);
    }

    public int getDrawPrioCurrent() {
        return drawPrioAtomInt.get();
    }

    public int getDrawPrioNext() {
        return drawPrioAtomInt.incrementAndGet();
    }

    public void setWindowDetachListener(Runnable theWindowDetachListener) {
        windowDetachListener = theWindowDetachListener;
    }

    public SleekCanvasScroller getSleekScroller() {
        return sleekScroller;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int theActivityId) {
        activityId = theActivityId;
    }

    public int getUiParentId() {
        return uiParentId;
    }

    public void setUiParentId(int theUiParentId) {
        uiParentId = theUiParentId;
    }

    protected void initView(int theActivityId, int theUiParentId) {

        activityId = theActivityId;

        uiParentId = theUiParentId;

        resetCanvasInfoStateTimestamp();

        preDrawRunList = new ArrayList<>();

        drawItemList = new ArrayList<>();
        touchItemList = new ArrayList<>();

        drawFixedItemList = new ArrayList<>();
        touchFixedItemList = new ArrayList<>();
    }

    public void setShouldCallWindowDetachListener(boolean theShouldCallWindowDetachListener) {
        shouldCallWindowDetachListener = theShouldCallWindowDetachListener;
    }

    /** This method is called when this view is removed from its parent.
     * Or if the parent is removed from its parent. */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (shouldCallWindowDetachListener && windowDetachListener != null) {
            windowDetachListener.run();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Need to invalidate here so that the initial onDraw() call is executed
        invalidate();
    }

    public boolean isBlockDraw() {
        synchronized (canvasLockObj) {
            return blockDraw;
        }
    }

    public void setBlockDraw(boolean blockDraw) {
        synchronized (canvasLockObj) {
            this.blockDraw = blockDraw;
        }
    }

    public void setAnimRun(ISleekAnimRun theAnimRun) {
        synchronized (canvasLockObj) {
            animRun = theAnimRun;
            hasAnimRun = animRun != null;
        }
        invalidateSafe();
    }

    public void clearAnimRun() {
        synchronized (canvasLockObj) {
            animRun = null;
            hasAnimRun = false;
        }
    }

    protected void runAnimRun() {
        synchronized (canvasLockObj) {
            if (!hasAnimRun) {
                return;
            }
            animRun.run(drawInfo);
            clearAnimRun();
        }
    }

    public void addPreDrawRun(final Runnable preDrawRun) {
        addPreDrawRun(new ISleekAnimRun() { @Override public void run(SleekCanvasInfo info) {
            preDrawRun.run();
        } });
    }

    public void addPreDrawRun(ISleekAnimRun preDrawRun) {
        synchronized (preRunLockObj) {
            atomInt.incrementAndGet();
            preDrawRunList.add(preDrawRun);
            hasPreDrawItems = true;
            atomInt.decrementAndGet();
        }
        invalidateSafe();
    }

    protected void runAllPreDrawRuns(SleekCanvasInfo info) {
        if (atomInt.get() > 0) {
            // Another thread is adding a preDrawRun,
            // skip running of preDrawRun's until next onDraw()

            info.invalidate();//ensure another draw is executed after current draw
            return;
        }
        synchronized (preRunLockObj) {
            if (!hasPreDrawItems) {
                return;
            }
            while (preDrawRunList.size() > 0) {
                preDrawRunList.remove(0).run(drawInfo);
            }
            hasPreDrawItems = false;
        }
    }

    public void setSleekScroller(SleekCanvasScroller theSleekScroller) {
        sleekScroller = theSleekScroller;
        if (sleekScroller != null) {
            sleekScroller.setSleekCanvas(this);
            hasSleekScroller = true;
        }
        else {
            hasSleekScroller = false;
        }
    }

    public void loadSleek(Sleek theSleekView) {
        theSleekView.onSleekLoad(drawInfo);
    }

    protected Comparator<Sleek> drawPrioComparator = new Comparator<Sleek>() {
        @Override
        public int compare(Sleek lhs, Sleek rhs) {
            return Integer.valueOf(lhs.getSleekPriority()).compareTo(rhs.getSleekPriority());
        }
    };

    protected Comparator<Sleek> touchPrioComparator = new Comparator<Sleek>() {
        @Override
        public int compare(Sleek lhs, Sleek rhs) {
            return Integer.valueOf(rhs.getSleekPriority()).compareTo(lhs.getSleekPriority());
        }
    };

    public void reloadScrollEdges() {
        getScrollEdges(true);//initializes scrollEdges member
        sleekScroller.setRightScrollEdge(scrollEdges[0]);
        sleekScroller.setBottomScrollEdge(scrollEdges[1]);
        sleekScroller.checkScrollBounds();
    }

    public void invalidateSafe() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {// On UI thread.
            invalidate();
        } else {// Not on UI thread.
            postInvalidate();
        }
    }

    protected void addSleekPrioritySorted(Sleek sleekDrawItem, boolean fixedList) {

        if (fixedList) {
            drawFixedItemList.add(0, sleekDrawItem);
            if (sleekDrawItem.getSleekPriority() > 0) {
                Collections.sort(drawFixedItemList, drawPrioComparator);
            }
            if (sleekDrawItem.isSleekTouchable()) {
                touchFixedItemList.add(sleekDrawItem);
                if (sleekDrawItem.getSleekPriority() > 0) {
                    Collections.sort(touchFixedItemList, touchPrioComparator);
                }
            }
        }
        else {
            drawItemList.add(0, sleekDrawItem);
            if (sleekDrawItem.getSleekPriority() > 0) {
                Collections.sort(drawItemList, drawPrioComparator);
            }
            if (sleekDrawItem.isSleekTouchable()) {
                touchItemList.add(sleekDrawItem);
                if (sleekDrawItem.getSleekPriority() > 0) {
                    Collections.sort(touchItemList, touchPrioComparator);
                }
            }
        }

        invalidateSafe();
    }

    public void addSleek(Sleek sleek) {
        synchronized (canvasLockObj) {

            addSleekPrioritySorted(
                    sleek,
                    sleek.isSleekFixedPosition()
            );

            sleek.onSleekParentAdd(SleekCanvas.this, null);

            // Check if size is initialized
            if (drawInfo.width > 0 && drawInfo.height > 0) {
                sleek.onSleekCanvasResize(drawInfo); // to init size when added at runtime

                if (sleekScroller.isAutoLoading()) {
                    reloadScrollEdgesFromSleek(sleek);
                    loadAndUnloadSleek(sleek);
                }
            }
        }
    }

    public boolean isInsideOfLoadBounds(Sleek sleek) {
        int canvasWidth = getScaledCanvasWidth();
        int canvasHeight = getScaledCanvasHeight();
        int scaledWidthLoadPadding = getScaledWidthLoadPadding();
        int scaledHeightLoadPadding = getScaledHeightLoadPadding();
        float controllerX = 0;
        float controllerY = 0;
        if (!sleek.isSleekFixedPosition()) {//only care about scroll value for non-fixed Sleek
            controllerX = getScaledScrollerPosLeft();
            controllerY = getScaledScrollerPosTop();
        }
        if (
                sleek.getSleekX() + sleek.getSleekW() < controllerX - scaledWidthLoadPadding ||
                sleek.getSleekX() > controllerX + canvasWidth + scaledWidthLoadPadding ||
                sleek.getSleekY() + sleek.getSleekH() < controllerY - scaledHeightLoadPadding ||
                sleek.getSleekY() > controllerY + canvasHeight + scaledHeightLoadPadding
                ) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Load/unload Sleek-instance if loadable and inside/outside of loading bounds.
     * @param sleek Sleek instance to check
     */
    public void loadAndUnloadSleek(Sleek sleek) {
        if (sleek.isSleekLoadable()) {
            if (isInsideOfLoadBounds(sleek)) {
                if (!sleek.isSleekLoaded()) {
                    loadSleek(sleek);
                }
            }
            else {
                if (sleek.isSleekLoaded()) {
                    sleek.onSleekUnload();
                }
            }
        }
    }

    /**
     * Reload scroll edges if Sleek instance is outside of current bounds.
     * @param sleek Sleek instance to check
     */
    public void reloadScrollEdgesFromSleek(Sleek sleek) {
        float[] scrollerEdges = getScrollEdges(false);// false == use cached values if not null
        if (
                sleek.getSleekX() + sleek.getSleekW() > scrollerEdges[0] ||
                sleek.getSleekY() + sleek.getSleekH() > scrollerEdges[1]
                ) {
            //reload scroll edges since added Sleek is outside of old bounds
            reloadScrollEdges();
        }
    }

    public void removeAllSleek(boolean fixedDrawItems) {
        synchronized (canvasLockObj) {

            Sleek removeItem;

            if (fixedDrawItems) {
                for (int i = drawFixedItemList.size() - 1; i >= 0; i--) {
                    if (i >= drawFixedItemList.size()) {//needed because SleekUI removal of childs
                        i = drawFixedItemList.size() - 1;
                        if (i < 0) {
                            break;
                        }
                    }
                    removeItem = drawFixedItemList.remove(i);
                    removeItem.onSleekUnload();
                    removeItem.onSleekParentRemove(SleekCanvas.this, null);
                }
                for (int i = touchFixedItemList.size() - 1; i >= 0; i--) {
                    if (i >= touchFixedItemList.size()) {//needed because SleekUI removal of childs
                        i = touchFixedItemList.size() - 1;
                        if (i < 0) {
                            break;
                        }
                    }
                    removeItem = touchFixedItemList.remove(i);
                    removeItem.onSleekUnload();
                    removeItem.onSleekParentRemove(SleekCanvas.this, null);
                }
            }
            else {
                for (int i = drawItemList.size() - 1; i >= 0; i--) {
                    if (i >= drawItemList.size()) {//needed because SleekUI removal of childs
                        i = drawItemList.size() - 1;
                        if (i < 0) {
                            break;
                        }
                    }
                    removeItem = drawItemList.remove(i);
                    removeItem.onSleekUnload();
                    removeItem.onSleekParentRemove(SleekCanvas.this, null);
                }
                for (int i = touchItemList.size() - 1; i >= 0; i--) {
                    if (i >= touchItemList.size()) {//needed because SleekUI removal of childs
                        i = touchItemList.size() - 1;
                        if (i < 0) {
                            break;
                        }
                    }
                    removeItem = touchItemList.remove(i);
                    removeItem.onSleekUnload();
                    removeItem.onSleekParentRemove(SleekCanvas.this, null);
                }
            }

            if (sleekScroller.isAutoLoading()) {
                reloadScrollEdges();
            }
        }

        invalidateSafe();
    }

    public void removeSleek(Sleek sleekDrawItem) {
        removeSleek(sleekDrawItem, true, true);
    }

    public void removeSleek(Sleek sleekDrawItem, boolean unloadView, boolean reloadScrollEdges) {
        synchronized (canvasLockObj) {

            if (sleekDrawItem == null) {
                return;
            }

            if (sleekDrawItem.isSleekFixedPosition()) {
                drawFixedItemList.remove(sleekDrawItem);
                if (sleekDrawItem.isSleekTouchable()) {
                    touchFixedItemList.remove(sleekDrawItem);
                }
            }
            else {
                drawItemList.remove(sleekDrawItem);
                if (sleekDrawItem.isSleekTouchable()) {
                    touchItemList.remove(sleekDrawItem);
                }
            }

            if (unloadView) {
                sleekDrawItem.onSleekUnload();
            }

            sleekDrawItem.onSleekParentRemove(SleekCanvas.this, null);

            if (reloadScrollEdges) {

                float scrollLeft = getSleekScroller().getPosLeft();
                float scrollTop = getSleekScroller().getPosTop();

                // Reload scroll edges so that removed view is no longer taken in to account
                reloadScrollEdges();

                // If removed view modified scroll position then also load/unload new viewport
                if (
                        scrollLeft != getSleekScroller().getPosLeft()
                        || scrollTop != getSleekScroller().getPosTop()
                        ) {
                    loadAndUnloadSleekLists(false);
                }
            }
        }

        invalidateSafe();
    }

    public int getViewCount() {
        return getDrawItemListCount() + getDrawFixedItemListCount();
    }

    public int getDrawItemListCount() {
        return drawItemList.size();
    }

    public int getDrawFixedItemListCount() {
        return drawFixedItemList.size();
    }

    public float[] getScrollEdges(boolean forceRefresh) {

        if (!forceRefresh && scrollEdges != null) {
            return scrollEdges;
        }

        float mostRightItem = 0.0f;
        float mostBottomItem = 0.0f;

        float iterEdge;

        synchronized (canvasLockObj) {
            for (Sleek iterDraw : drawItemList) {

                iterEdge = iterDraw.getSleekX() + iterDraw.getSleekW();
                if (iterEdge > mostRightItem) {
                    mostRightItem = iterEdge;
                }

                iterEdge = iterDraw.getSleekY() + iterDraw.getSleekH();
                if (iterEdge > mostBottomItem) {
                    mostBottomItem = iterEdge;
                }
            }
        }

        scrollEdges = new float[] { mostRightItem, mostBottomItem };
        return scrollEdges;
    }

    public void forceUnloadAllSleek() {

        synchronized (canvasLockObj) {
            for (Sleek iterDraw : drawItemList) {
                iterDraw.onSleekUnload();
            }

            for (Sleek iterDraw : drawFixedItemList) {
                iterDraw.onSleekUnload();
            }
        }

        invalidateSafe();
    }

    public void reloadAllSleek() {
        loadAndUnloadSleekLists(true);
    }

    public void loadAndUnloadSleekLists(boolean includeFixedViews) {

        synchronized (canvasLockObj) {
            int canvasWidth = getScaledCanvasWidth();
            int canvasHeight = getScaledCanvasHeight();

            float controllerX = getScaledScrollerPosLeft();
            float controllerY = getScaledScrollerPosTop();

            int scaledWidthLoadPadding = getScaledWidthLoadPadding();
            int scaledHeightLoadPadding = getScaledHeightLoadPadding();

            for (Sleek iterDraw : drawItemList) {

                if (!iterDraw.isSleekLoadable()) {
                    continue;
                }

                if (
                        iterDraw.getSleekX() + iterDraw.getSleekW() < controllerX - scaledWidthLoadPadding ||
                        iterDraw.getSleekX() > controllerX + canvasWidth + scaledWidthLoadPadding ||
                        iterDraw.getSleekY() + iterDraw.getSleekH() < controllerY - scaledHeightLoadPadding ||
                        iterDraw.getSleekY() > controllerY + canvasHeight + scaledHeightLoadPadding
                        ) {
                    if (iterDraw.isSleekLoaded()) {
                        iterDraw.onSleekUnload();
                    }
                }
                else {
                    if (!iterDraw.isSleekLoaded()) {
                        iterDraw.onSleekLoad(drawInfo);
                    }
                }
            }

            if (!includeFixedViews) return;

            for (Sleek iterDraw : drawFixedItemList) {

                if (!iterDraw.isSleekLoadable()) {
                    continue;
                }

                if (
                        iterDraw.getSleekX() + iterDraw.getSleekW() < -scaledWidthLoadPadding ||
                        iterDraw.getSleekX() > canvasWidth + scaledWidthLoadPadding ||
                        iterDraw.getSleekY() + iterDraw.getSleekH() < -scaledHeightLoadPadding ||
                        iterDraw.getSleekY() > canvasHeight + scaledHeightLoadPadding
                        ) {
                    if (iterDraw.isSleekLoaded()) {
                        iterDraw.onSleekUnload();
                    }
                }
                else {
                    if (!iterDraw.isSleekLoaded()) {
                        iterDraw.onSleekLoad(drawInfo);
                    }
                }
            }
        }

        invalidateSafe();
    }

    protected int getScaledCanvasWidth() {
        if (!sleekScroller.isScaled()) {
            return drawInfo.width;
        }
        return Math.round(drawInfo.width / sleekScroller.getScaleX());
    }

    protected int getScaledCanvasHeight() {
        if (!sleekScroller.isScaled()) {
            return drawInfo.height;
        }
        return Math.round(drawInfo.height / sleekScroller.getScaleY());
    }

    protected float getScaledScrollerPosLeft() {
        if (!sleekScroller.isScaled()) {
            return -sleekScroller.getPosLeft();
        }
        return UtilSleekMath.roundFloat(-sleekScroller.getPosLeft() / sleekScroller.getScaleX());
    }

    protected float getScaledScrollerPosTop() {
        if (!sleekScroller.isScaled()) {
            return -sleekScroller.getPosTop();
        }
        return UtilSleekMath.roundFloat(-sleekScroller.getPosTop() / sleekScroller.getScaleY());
    }

    protected int getScaledWidthLoadPadding() {
        if (!sleekScroller.isScaled()) {
            return widthLoadPadding;
        }
        return Math.round(widthLoadPadding / sleekScroller.getScaleX());
    }

    protected int getScaledHeightLoadPadding() {
        if (!sleekScroller.isScaled()) {
            return heightLoadPadding;
        }
        return Math.round(heightLoadPadding / sleekScroller.getScaleY());
    }

    public void resetCanvasInfoStateTimestamp() {
        drawInfo.stateTimestamp = System.currentTimeMillis();
    }

    public void setLoadPaddingEqualToSize(boolean theLoadPaddingEqualToSize) {
        loadPaddingEqualToSize = theLoadPaddingEqualToSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        synchronized (canvasLockObj) {

            if (w != oldw) {//this equals orientation change
                checkRequestFocus(true);
            }

            drawInfo.width = w;
            drawInfo.height = h;

            resetCanvasInfoStateTimestamp();

            sleekScroller.onSleekCanvasSizeChanged(drawInfo);

            drawInfo.scrollerScaleX = sleekScroller.getScaleX();
            drawInfo.scrollerScaleY = sleekScroller.getScaleY();
            drawInfo.scrollerPosLeft = sleekScroller.getPosLeft();
            drawInfo.scrollerPosTop = sleekScroller.getPosTop();

            for (Sleek iterDraw : drawItemList) {
                iterDraw.onSleekCanvasResize(drawInfo);
            }

            for (Sleek iterDraw : drawFixedItemList) {
                iterDraw.onSleekCanvasResize(drawInfo);
            }

            if (loadPaddingEqualToSize) {
                widthLoadPadding = w;
                heightLoadPadding = h;
            }

            if (sleekScroller.isAutoLoading()) {

                reloadScrollEdges();

                //Reload drawInfo values since reloadScrollEdges() could have affected them
                drawInfo.scrollerScaleX = sleekScroller.getScaleX();
                drawInfo.scrollerScaleY = sleekScroller.getScaleY();
                drawInfo.scrollerPosLeft = sleekScroller.getPosLeft();
                drawInfo.scrollerPosTop = sleekScroller.getPosTop();

                loadAndUnloadSleekLists(true);
            }
        }
    }

    public void setScrollerAnimView(ISleekAnimView animView) {
        this.scrollerAnimView = animView;
        if (this.scrollerAnimView == null) {
            this.scrollerAnimView = ISleekAnimView.NO_ANIMATION;
        }

        invalidateSafe();
    }

    protected void loadCanvasInfoScrollerValues() {
        drawInfo.scrollerScaleX = sleekScroller.getScaleX();
        drawInfo.scrollerScaleY = sleekScroller.getScaleY();
        drawInfo.scrollerPosLeft = sleekScroller.getPosLeft();
        drawInfo.scrollerPosTop = sleekScroller.getPosTop();
        drawInfo.scrollerPaddingTop = sleekScroller.getPaddingTop();
        drawInfo.scrollerPaddingBottom = sleekScroller.getPaddingBottom();
        drawInfo.scrollerPaddingLeft = sleekScroller.getPaddingLeft();
        drawInfo.scrollerPaddingRight = sleekScroller.getPaddingRight();
        drawInfo.scrollerMarginTop = sleekScroller.getMarginTop();
        drawInfo.scrollerMarginBottom = sleekScroller.getMarginBottom();
        drawInfo.scrollerMarginLeft = sleekScroller.getMarginLeft();
        drawInfo.scrollerMarginRight = sleekScroller.getMarginRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawInfo.drawTimestamp = System.currentTimeMillis();

        loadCanvasInfoScrollerValues();

        runAllPreDrawRuns(drawInfo);

        runAnimRun();

        if (blockDraw) {
            if (drawInfo.shouldInvalidate() || autoInvalidate) {
                invalidate();
            }
            return;
        }

        canvas.save();

        scrollerDrawAnimView = scrollerAnimView;
        scrollerDrawAnimView.animTickStart(null, canvas, drawInfo);

        //if scrollerAnimView updates scroll values, drawInfo scroller values must be the same.
        loadCanvasInfoScrollerValues();

        if (sleekScroller.isScaled()) {
            canvas.scale(
                    drawInfo.scrollerScaleX,
                    drawInfo.scrollerScaleY,
                    0,
                    0
            );

            canvas.translate(
                    UtilSleekMath.roundFloat(drawInfo.scrollerPosLeft / drawInfo.scrollerScaleX),
                    UtilSleekMath.roundFloat(drawInfo.scrollerPosTop / drawInfo.scrollerScaleY)
            );
        }
        else {
            canvas.translate(
                    drawInfo.scrollerPosLeft,
                    drawInfo.scrollerPosTop
            );
        }

        synchronized (canvasLockObj) {
            for (Sleek iterDraw : drawItemList) {
                if (iterDraw.isSleekReadyToDraw()) {
                    iterDraw.onSleekDraw(canvas, drawInfo);
                }
            }
        }

        canvas.restore();

        if (scrollerDrawAnimView.animTickEnd(null, canvas, drawInfo)) {
            setScrollerAnimView(null);
        }

        drawInfo.runAllDrawTasks(canvas);

        synchronized (canvasLockObj) {
            for (Sleek iterDraw : drawFixedItemList) {
                if (iterDraw.isSleekReadyToDraw()) {
                    iterDraw.onSleekDraw(canvas, drawInfo);
                }
            }
        }

        if (drawInfo.shouldInvalidate() || autoInvalidate) {
            invalidate();
        }
    }

    protected boolean pagerIntercepted = false;
    protected boolean actionCancelled = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }

    protected boolean hasNativeFocusView;
    protected View mNativeFocusView;
    protected Sleek mSleekFocusView;
    protected boolean requestFocusOnTouchUp = false;

    public void setNativeFocusView(View theNativeFocusView, Sleek theSleekFocusView) {

        if (
                theNativeFocusView != null &&
                theSleekFocusView != null
                ) {
            mNativeFocusView = theNativeFocusView;
            mSleekFocusView = theSleekFocusView;
            hasNativeFocusView = true;
            setDescendantFocusability(RelativeLayout.FOCUS_AFTER_DESCENDANTS);
        }
        else {
            mNativeFocusView = null;
            mSleekFocusView = null;
            hasNativeFocusView = false;
        }
    }

    protected void checkFocusNativeFocusView(Sleek checkSleekView, MotionEvent event) {

        if (checkSleekView == null) {
            requestFocusOnTouchUp = true;
            return;
        }

        if (!hasNativeFocusView) {
            return;
        }

        if (mSleekFocusView.equals(checkSleekView)) {
            return;
        }

        if (mSleekFocusView != null) {

            float[] posXY = SleekBase.getSleekParentsAggregatedPos(
                    SleekBase.getSleekParents(mSleekFocusView),
                    true
            );

            float posX = posXY[0];
            float posY = posXY[1];

            boolean isTouchInside = UtilSleekTouch.isTouchInside(
                    event.getX(),
                    event.getY(),
                    mSleekFocusView.getSleekX() + posX,
                    mSleekFocusView.getSleekY() + posY,
                    mSleekFocusView.getSleekW(),
                    mSleekFocusView.getSleekH()
            );

            if (isTouchInside) {
                return;
            }
        }

        requestFocusOnTouchUp = true;
    }

    public void hideKeyboard() {
        InputMethodManager imm =  (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
        );
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    public void clearNativeFocusView() {
        checkRequestFocus(true);
    }

    protected void checkRequestFocus(boolean forced) {

        if (!forced) {
            if (!requestFocusOnTouchUp) {
                return;
            }
            else if (blockRequestFocus) {
                return;
            }
        }

        requestFocusOnTouchUp = false;

        if (!hasNativeFocusView) {
            return;
        }

        hideKeyboard();

        setDescendantFocusability(RelativeLayout.FOCUS_BEFORE_DESCENDANTS);
        requestFocus();

        setNativeFocusView(null, null);
    }

    public void setBlockRequestFocus() {
        blockRequestFocus = true;
    }

    protected boolean blockRequestFocus = false;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        invalidate();

        drawInfo.scrollerScaleX = sleekScroller.getScaleX();
        drawInfo.scrollerScaleY = sleekScroller.getScaleY();
        drawInfo.scrollerPosLeft = sleekScroller.getPosLeft();
        drawInfo.scrollerPosTop = sleekScroller.getPosTop();

        boolean isTouchDownEvent = UtilSleekTouch.isTouchDown(event);

        if (isTouchDownEvent) {
            blockRequestFocus = false;
            activeTouchIndex = -1;
            activeFixedTouchIndex = -1;
            activeTouchView = null;
            pagerIntercepted = false;
            actionCancelled = false;
        }

        //Make native view loose focus on touch-up
        if (
                UtilSleekTouch.isTouchUp(event) ||
                UtilSleekTouch.isTouchCancel(event)
                ) {
            checkRequestFocus(false);
        }

        if (pagerIntercepted) {
            sleekScroller.onSleekTouchEvent(event, drawInfo);
            return true;
        }

        if (!actionCancelled) {

            if (// item in the non-fixed touch list has required touch exclusivety by setting its touch prio to STICKY_TOUCH_PRIO or above
                    activeTouchIndex != -1 &&
                    activeTouchView.getSleekPriority() >= STICKY_TOUCH_PRIO
                    ) {
                event.offsetLocation(-sleekScroller.getPosLeft(), -sleekScroller.getPosTop());
                activeTouchView.onSleekTouchEvent(event, drawInfo);
                event.offsetLocation(sleekScroller.getPosLeft(), sleekScroller.getPosTop());
            }
            else if (!determineActiveTouchIndex(event, true)) {
                if (sleekScroller.onSleekTouchEvent(event, drawInfo)) {

                    pagerIntercepted = true;

                    checkFocusNativeFocusView(null, event);

                    // only send one cancel event to children
                    if (!actionCancelled) {
                        event.setAction(MotionEvent.ACTION_CANCEL);

                        if (activeFixedTouchIndex != -1) {
                            determineActiveTouchIndex(event, true);
                        }

                        if (activeTouchIndex != -1) {
                            event.offsetLocation(-sleekScroller.getPosLeft(), -sleekScroller.getPosTop());
                            determineActiveTouchIndex(event, false);
                            event.offsetLocation(sleekScroller.getPosLeft(), sleekScroller.getPosTop());
                        }

                        actionCancelled = true;
                    }
                }
                else {
                    event.offsetLocation(-sleekScroller.getPosLeft(), -sleekScroller.getPosTop());
                    determineActiveTouchIndex(event, false);
                    event.offsetLocation(sleekScroller.getPosLeft(), sleekScroller.getPosTop());
                }
            }
        }

        if (
                isTouchDownEvent &&
                !pagerIntercepted
                ) {
            checkFocusNativeFocusView(activeTouchView, event);
        }

        return true;
    }

    protected boolean determineActiveTouchIndex(MotionEvent event, boolean fixedList) {
        boolean returnValue = false;

        ArrayList<Sleek> theTouchItemList;
        if (fixedList) {
            theTouchItemList = touchFixedItemList;
        }
        else {
            theTouchItemList = touchItemList;
        }

        int theActiveTouchIndex;
        if (fixedList) {
            theActiveTouchIndex = activeFixedTouchIndex;
        }
        else {
            theActiveTouchIndex = activeTouchIndex;
        }

        // if view is removed at runtime an error would occur if this check was not here.
        if (theActiveTouchIndex > theTouchItemList.size() - 1) {
            theActiveTouchIndex = -1;
            activeFixedTouchIndex = -1;
            activeTouchIndex = -1;
            activeTouchView = null;
        }

        // if view that has touch event has a touchPriority equal to or greater than STICKY_TOUCH_PRIO, then that view gets to keep the event
        if (
                theActiveTouchIndex > -1 &&
                activeTouchView.getSleekPriority() >= STICKY_TOUCH_PRIO
                ) {
            returnValue = activeTouchView.onSleekTouchEvent(event, drawInfo);
            return returnValue;
        }

        int iterCount = -1;

        synchronized (canvasLockObj) {
            for (Sleek iterTouch : theTouchItemList) {
                iterCount++;
                //if (iterTouch.isLoadable() && !iterTouch.isLoaded()) continue;
                if (!iterTouch.isSleekReadyToDraw()) continue;

                returnValue = iterTouch.onSleekTouchEvent(event, drawInfo);
                if (returnValue) {

                    activeTouchView = iterTouch;

                    // event has been intercepted by another view, cancel the previous event holder if there was one.
                    if (theActiveTouchIndex > -1 && theActiveTouchIndex != iterCount) {
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        try {
                            theTouchItemList.get(theActiveTouchIndex).onSleekTouchEvent(event, drawInfo);
                        }
                        catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }

                    if (fixedList) {
                        activeFixedTouchIndex = iterCount;
                    }
                    else {
                        activeTouchIndex = iterCount;
                    }
                    break;
                }
            }
        }

        return returnValue;
    }

    public int getWidthLoadPadding() {
        return widthLoadPadding;
    }

    public void setWidthLoadPadding(int widthLoadPadding) {
        this.widthLoadPadding = widthLoadPadding;
    }

    public int getHeightLoadPadding() {
        return heightLoadPadding;
    }

    public void setHeightLoadPadding(int heightLoadPadding) {
        this.heightLoadPadding = heightLoadPadding;
    }

    public boolean isAutoInvalidate() {
        return autoInvalidate;
    }

    public void setAutoInvalidate(boolean theAutoInvalidate) {
        autoInvalidate = theAutoInvalidate;

        if (autoInvalidate) {
            invalidate();
        }
    }

    // ____________________ BELOW IS ViewGroup CODE ____________________

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return false;//return false here because SleekNativeView is handling the View's drawing
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (
                MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY &&
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                ) {

            int sizeW = MeasureSpec.getSize(widthMeasureSpec);
            int sizeH = MeasureSpec.getSize(heightMeasureSpec);

            // Use double the size during layout in order to prevent the child views to be...
            // ...resized when they have pos/size that intersects bottom/right SleekCanvas bounds.
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(sizeW + sizeW, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(sizeH + sizeH, MeasureSpec.EXACTLY)
            );

            setMeasuredDimension(sizeW, sizeH);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
