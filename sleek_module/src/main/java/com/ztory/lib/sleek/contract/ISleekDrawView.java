package com.ztory.lib.sleek.contract;

import android.graphics.Canvas;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;

/**
 * Interface for drawing a Sleek instance in a decoupled way. Includes all parameters that are
 * needed to draw a Sleek.
 * Created by jonruna on 09/10/14.
 */
public interface ISleekDrawView {

    ISleekDrawView NO_DRAW = new ISleekDrawView() {
        @Override
        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
            // DO NOTHING
        }
    };

    void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info);

}
