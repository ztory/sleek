package com.ztory.lib.sleek.base.androidui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.util.UtilSleekTouch;

/**
 * Subclass of SleekNativeView<FrameLayout> used to wrap native Android views inside
 * a FrameLayout before adding it to SleekCanvas. This is needed when adding for example
 * a EditText view.
 * Created by jonruna on 09/10/14.
 */
public class SleekNativeWrappedView<W extends View> extends SleekNativeView<FrameLayout> {

    protected W mWrappedView;

    protected boolean mLayoutWrapContent;

    public SleekNativeWrappedView(
            Context theContext,
            W theWrappedView,
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio
    ) {
        this(
                theContext,
                false,
                theWrappedView,
                isFixedPosition,
                isTouchable,
                isLoadable,
                theTouchPrio
        );
    }

    public SleekNativeWrappedView(
            Context theContext,
            boolean layoutWrapContent,
            W theWrappedView,
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio
    ) {
        super(
                new FrameLayout(theContext),
                isFixedPosition,
                isTouchable,
                isLoadable,
                theTouchPrio,
                true//shouldCareAboutNativeMargins
        );

        mLayoutWrapContent = layoutWrapContent;

        mWrappedView = theWrappedView;

        if (mLayoutWrapContent) {
            getFrameLayout().addView(
                    mWrappedView,
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    )
            );
        }
        else {
            getFrameLayout().addView(
                    mWrappedView,
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                    )
            );
        }
    }

    @Override
    public void setOnFocusChangeRefreshBounds() {
        mWrappedView.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        //used to reset bounds to drawing bounds, instead of editing bounds...
                        //...when clicking the textview when it is partially off-screen.
                        SleekNativeWrappedView.this.refreshNativeBounds();
                    }
                }
        );
    }

    public FrameLayout getFrameLayout() {
        return mAndroidView;
    }

    public W getWrappedView() {
        return mWrappedView;
    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {

        if (
                mLayoutWrapContent &&
                (
                        mWrappedView.getMeasuredWidth() != w ||
                        mWrappedView.getMeasuredHeight() != h
                )
                ) {

            mWrappedView.measure(
                    View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.AT_MOST)
            );

            w = mWrappedView.getMeasuredWidth();
            h = mWrappedView.getMeasuredHeight();
        }

        super.setSleekBounds(x, y, w, h);
    }

    @Override
    protected void refreshNativeBounds(SleekCanvasInfo info, boolean forceMarginSet) {

        // Dont set margins if not touchable or view does not have focus and forceMarginSet==FALSE
        if (
                !touchable ||
                !careAboutNativeMargins ||
                (!mWrappedView.hasFocus() && !forceMarginSet)
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

            boolean touchReturn = mWrappedView.dispatchTouchEvent(event);

            event.offsetLocation(sleekX, sleekY);

            if (touchReturn) {
                mSlkCanvas.setNativeFocusView(mWrappedView, this);
            }

            return touchReturn;
        }
        else {
            return false;
        }
        //return touchHandler.onSleekTouchEvent(event, info);
    }

}
