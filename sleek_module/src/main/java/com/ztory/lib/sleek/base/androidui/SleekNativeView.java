package com.ztory.lib.sleek.base.androidui;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.SleekParent;
import com.ztory.lib.sleek.contract.ISleekParentDid;
import com.ztory.lib.sleek.contract.ISleekAnimRun;
import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.util.UtilSleekTouch;

/**
 * SleekBase class for wrapping native Android View instances.
 * Created by jonruna on 09/10/14.
 */
public class SleekNativeView<V extends View> extends SleekBase {

    protected V mAndroidView;

    protected RelativeLayout.LayoutParams mLayoutParams;

    protected boolean mHandleTouchEvent = false;
    protected float lastScrollerTop, lastScrollerLeft;

    protected boolean careAboutNativeMargins;

    public SleekNativeView(
            V theAndroidView,
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio,
            boolean shouldCareAboutNativeMargins
    ) {
        super(isFixedPosition, isTouchable, isLoadable, theTouchPrio);

        mAndroidView = theAndroidView;

        careAboutNativeMargins = shouldCareAboutNativeMargins;

        mLayoutParams = new RelativeLayout.LayoutParams(0, 0);

        initAddRemoveListeners();
    }

    @Override
    public void onSleekCanvasResize(SleekCanvasInfo info) {
        super.onSleekCanvasResize(info);

        refreshNativeBounds(true);
    }

    /**
     * Calls refreshNativeBounds() when this view gains or looses focus.
     */
    public void setOnFocusChangeRefreshBounds() {
        mAndroidView.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        //used to reset bounds to drawing bounds, instead of editing bounds...
                        //...when clicking the textview when it is partially off-screen.
                        SleekNativeView.this.refreshNativeBounds();
                    }
                }
        );
    }

    public void refreshNativeBounds() {
        refreshNativeBounds(false);
    }

    public void refreshNativeBounds(final boolean forceMarginSet) {
        if (addedToParent) {
            mSlkCanvas.addPreDrawRun(
                    new ISleekAnimRun() {
                        @Override
                        public void run(SleekCanvasInfo info) {
                            refreshNativeBounds(info, forceMarginSet);
                            info.invalidate();
                        }
                    }
            );
        }
    }

    protected void refreshNativeBounds(SleekCanvasInfo info, boolean forceMarginSet) {

        // Dont set margins if not touchable or view does not have focus and forceMarginSet==FALSE
        if (
                !touchable ||
                !careAboutNativeMargins ||
                (!mAndroidView.hasFocus() && !forceMarginSet)
                ) {

            // Only update size if it differs from getMeasuredWidth / getMeasuredHeight
            if (
                    mAndroidView.getMeasuredWidth() != sleekW ||
                    mAndroidView.getMeasuredHeight() != sleekH
                    ) {
                mLayoutParams = new RelativeLayout.LayoutParams(sleekW, sleekH);
                mAndroidView.setLayoutParams(mLayoutParams);
            }
            return;
        }

        // The below code only needs to be called if the view needs to have correct native-margins.
        // The drawing of the view is not affected by native margins, but for example the ...
        // ... EditText views text-selector is drawn by another class based on the native-margins.

        mLayoutParams = new RelativeLayout.LayoutParams(sleekW, sleekH);

        int topMargin, leftMargin;

        //this view has a SleekViewCollection parent
        if (mSlkParent != null) {

            leftMargin = (int) sleekX;
            topMargin = (int) sleekY;

            Sleek[] svcParents = getSleekParents(this);

            float[] aggregatedXY = getSleekParentsAggregatedPos(svcParents, true);

            leftMargin += aggregatedXY[0];
            topMargin += aggregatedXY[1];

            //If the root SleekViewCollection is not fixedPosition, then include scroller pos
            if (!svcParents[svcParents.length - 1].isSleekFixedPosition()) {
                leftMargin += info.scrollerPosLeft;
                topMargin += info.scrollerPosTop;
            }
        }
        else if (fixedPosition) {
            leftMargin = (int) sleekX;
            topMargin = (int) sleekY;
        }
        else {
            leftMargin = (int) (sleekX + info.scrollerPosLeft);
            topMargin = (int) (sleekY + info.scrollerPosTop);
        }

        mLayoutParams.setMargins(
                leftMargin,
                topMargin,
                0,
                0
        );

        mAndroidView.setLayoutParams(mLayoutParams);
    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {

        super.setSleekBounds(x, y, w, h);

        refreshNativeBounds();
    }

    @Override
    public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo info) {

        if (UtilSleekTouch.isTouchDown(event)) {
            if (UtilSleekTouch.isTouchInside(event.getX(), event.getY(), this)) {
                mHandleTouchEvent = true;
                refreshNativeBounds(info, true);
            }
            else {
                mHandleTouchEvent = false;
            }
        }

        if (mHandleTouchEvent) {

            event.offsetLocation(-sleekX, -sleekY);

            boolean touchReturn = mAndroidView.dispatchTouchEvent(event);

            event.offsetLocation(sleekX, sleekY);

            if (touchReturn) {
                mSlkCanvas.setNativeFocusView(mAndroidView, this);
            }

            return touchReturn;
        }
        else {
            return false;
        }
        //return touchHandler.onSleekTouchEvent(event, info);
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        if (!fixedPosition) {

            if (
                    lastScrollerLeft != info.scrollerPosLeft ||
                    lastScrollerTop != info.scrollerPosTop
                    ) {
                refreshNativeBounds(info, false);
            }

            lastScrollerLeft = info.scrollerPosLeft;
            lastScrollerTop = info.scrollerPosTop;
        }

        canvas.save();
        canvas.translate(sleekX, sleekY);
        mAndroidView.draw(canvas);

        //superDrawChild() doesnt work with the native margins
//        mSlkCanvas.superDrawChild(
//                canvas,
//                mAndroidView,
//                mSlkCanvas.getDrawingTime()
//        );

        //mSlkCanvas.dispatchDraw(canvas);

        canvas.restore();
    }

    public V getAndroidView() {
        return mAndroidView;
    }

    private void initAddRemoveListeners() {

        ISleekParentDid addListener = new ISleekParentDid() {
            @Override
            public void parentDid(SleekCanvas sCanvas, SleekParent sViewCollection) {

                if (
                        mAndroidView.getParent() != null &&
                        mAndroidView.getParent() instanceof ViewGroup &&
                        mAndroidView.getParent() != sCanvas
                        ){
                    ((ViewGroup) mAndroidView.getParent()).removeView(mAndroidView);
                }

                if (mAndroidView.getParent() == null) {
                    sCanvas.addView(mAndroidView, mLayoutParams);
                }

                refreshNativeBounds();
            }
        };

        ISleekParentDid removeListener = new ISleekParentDid() {
            @Override
            public void parentDid(SleekCanvas sCanvas, SleekParent sViewCollection) {
                sCanvas.removeView(mAndroidView);
            }
        };

        setParentDidAddListener(addListener);
        setParentDidRemoveListener(removeListener);
    }

}
