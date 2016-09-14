package com.ztory.lib.sleek.base.scroller;

import android.view.MotionEvent;

import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.SleekCanvasScroller;

/**
 * Empty implementation of SleekCanvasScroller.
 * Created by jonruna on 09/10/14.
 */
public class SleekScrollerBase implements SleekCanvasScroller {

    private boolean mIsAutoLoading = false;

    public SleekScrollerBase(boolean theIsAutoLoading) {
        mIsAutoLoading = theIsAutoLoading;
    }

    @Override
    public void setSleekCanvas(SleekCanvas parentSleekCanvas) {

    }

    @Override
    public float getPosLeft() {
        return 0;
    }

    @Override
    public float getPosTop() {
        return 0;
    }

    @Override
    public void onSleekCanvasSizeChanged(SleekCanvasInfo info) {

    }

    @Override
    public boolean isAutoLoading() {
        return mIsAutoLoading;
    }

    @Override
    public boolean onSleekTouchEvent(
            MotionEvent event, SleekCanvasInfo info
    ) {
        return false;
    }

    @Override
    public void setBottomScrollEdge(float bottomScrollEdge) {

    }

    @Override
    public void setRightScrollEdge(float rightScrollEdge) {

    }

    @Override
    public void setPaddingTop(int topPadding) {

    }

    @Override
    public void setPaddingBottom(int bottomPadding) {

    }

    @Override
    public void setPaddingLeft(int leftPadding) {

    }

    @Override
    public void setPaddingRight(int rightPadding) {

    }

    @Override
    public int getPaddingTop() {
        return 0;
    }

    @Override
    public int getPaddingBottom() {
        return 0;
    }

    @Override
    public int getPaddingLeft() {
        return 0;
    }

    @Override
    public int getPaddingRight() {
        return 0;
    }

    @Override
    public void setMarginTop(int topMargin) {

    }

    @Override
    public void setMarginBottom(int bottomMargin) {

    }

    @Override
    public void setMarginLeft(int leftMargin) {

    }

    @Override
    public void setMarginRight(int rightMargin) {

    }

    @Override
    public int getMarginTop() {
        return 0;
    }

    @Override
    public int getMarginBottom() {
        return 0;
    }

    @Override
    public int getMarginLeft() {
        return 0;
    }

    @Override
    public int getMarginRight() {
        return 0;
    }

    @Override
    public boolean isScaled() {
        return false;
    }

    @Override
    public float getScaleX() {
        return 0;
    }

    @Override
    public float getScaleY() {
        return 0;
    }

    @Override
    public float getScalePivotX() {
        return 0;
    }

    @Override
    public float getScalePivotY() {
        return 0;
    }
}
