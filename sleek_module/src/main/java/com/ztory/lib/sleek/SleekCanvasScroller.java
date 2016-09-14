package com.ztory.lib.sleek;

import android.view.MotionEvent;

/**
 * Scroller interface used by SleekCanvas to manipulate its Sleek instances that are not fixed.
 * Created by jonruna on 09/10/14.
 */
public interface SleekCanvasScroller {

    void setSleekCanvas(SleekCanvas parentSleekCanvas);

    float getPosLeft();
    float getPosTop();

    void onSleekCanvasSizeChanged(SleekCanvasInfo info);
    boolean isAutoLoading();

    boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo info);

    void setBottomScrollEdge(float bottomScrollEdge);
    void setRightScrollEdge(float rightScrollEdge);

    void setPaddingTop(int topPadding);
    void setPaddingBottom(int bottomPadding);
    void setPaddingLeft(int leftPadding);
    void setPaddingRight(int rightPadding);

    int getPaddingTop();
    int getPaddingBottom();
    int getPaddingLeft();
    int getPaddingRight();

    void setMarginTop(int topMargin);
    void setMarginBottom(int bottomMargin);
    void setMarginLeft(int leftMargin);
    void setMarginRight(int rightMargin);

    int getMarginTop();
    int getMarginBottom();
    int getMarginLeft();
    int getMarginRight();

    boolean isScaled();
    float getScaleX();
    float getScaleY();
    float getScalePivotX();
    float getScalePivotY();

}
