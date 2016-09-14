package com.ztory.lib.sleek;

import android.graphics.Canvas;

import com.ztory.lib.sleek.contract.ISleekDrawView;

import java.util.ArrayList;

/**
 * Data class for SleekCanvas to provide info relevant to drawing and resizing to its
 * Sleek instances.
 * Created by jonruna on 09/10/14.
 */
public class SleekCanvasInfo {

    public int width = 0, height = 0;
    public long drawTimestamp;// SleekCanvas draw start timestamp
    public long stateTimestamp;// SleekCanvas state change timestamp
    public float scrollerScaleX = 1.0f, scrollerScaleY = 1.0f;
    public float scrollerPosTop = 0.0f, scrollerPosLeft = 0.0f;
    public int
            scrollerPaddingTop = 0,
            scrollerPaddingBottom = 0,
            scrollerPaddingLeft = 0,
            scrollerPaddingRight = 0,
            scrollerMarginTop = 0,
            scrollerMarginBottom = 0,
            scrollerMarginLeft = 0,
            scrollerMarginRight = 0;

    private boolean hasDrawTasks = false;
    private ArrayList<ISleekDrawView> drawTaskList = new ArrayList<>();

    private int invalidateCount = 0;

    public void invalidate() {
        invalidateCount++;
    }

    public boolean shouldInvalidate() {
        if (invalidateCount == 0) {
            return false;
        }

        invalidateCount = 0;
        return true;
    }

    public void addDrawTask(ISleekDrawView taskDrawView) {
        drawTaskList.add(taskDrawView);
        hasDrawTasks = true;
    }

    public void runAllDrawTasks(Canvas canvas) {
        if (!hasDrawTasks) return;
        for (ISleekDrawView iterTask : drawTaskList) {
            iterTask.drawView(null, canvas, SleekCanvasInfo.this);
        }
        drawTaskList.clear();
        hasDrawTasks = false;
    }

    @Override
    public String toString(){
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("SleekCanvasInfo").append("[")
                .append("width=").append(width).append(",")
                .append("height=").append(height).append(",")
                .append("scrollerPosTop=").append(scrollerPosTop).append(",")
                .append("scrollerPosLeft=").append(scrollerPosLeft);

        return strBuffer.toString();
    }

}
