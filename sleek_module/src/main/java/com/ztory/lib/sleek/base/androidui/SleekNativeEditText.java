package com.ztory.lib.sleek.base.androidui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.util.UtilSleekTouch;

/**
 * Subclass of SleekNativeView<FrameLayout> used to display and interact with EditText instances
 * in a simple way.
 * Created by jonruna on 09/10/14.
 */
public class SleekNativeEditText extends SleekNativeView<FrameLayout> {

    private EditText mEditText;

    public SleekNativeEditText(
            Context theContext,
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

        mEditText = new EditText(theContext);

        getFrameLayout().addView(
                mEditText,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    public FrameLayout getFrameLayout() {
        return mAndroidView;
    }

    public EditText getEditText() {
        return mEditText;
    }

    @Override
    protected void refreshNativeBounds(SleekCanvasInfo info, boolean forceMarginSet) {

        // Dont set margins if not touchable or view does not have focus and forceMarginSet==FALSE
        if (
                !touchable ||
                !careAboutNativeMargins ||
                (!mEditText.hasFocus() && !forceMarginSet)
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

            boolean touchReturn = mEditText.dispatchTouchEvent(event);

            event.offsetLocation(sleekX, sleekY);

            if (touchReturn) {
                mSlkCanvas.setNativeFocusView(mEditText, this);
            }

            return touchReturn;
        }
        else {
            return false;
        }
        //return touchHandler.onSleekTouchEvent(event, info);
    }

    public void clearFocus() {
        mEditText.clearFocus();
        if (mSlkCanvas != null) {
            mSlkCanvas.clearNativeFocusView();
        }
    }

    public void requestFocus() {
        if (mEditText.requestFocus()) {
            mSlkCanvas.setNativeFocusView(mEditText, this);
        }
    }

    public void requestFocusAndShowKeyboard() {

        if (mEditText.requestFocus()) {

            mSlkCanvas.setNativeFocusView(mEditText, this);

            InputMethodManager imm = (InputMethodManager) mEditText.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            imm.toggleSoftInput(0, 0);
        }
    }

}
