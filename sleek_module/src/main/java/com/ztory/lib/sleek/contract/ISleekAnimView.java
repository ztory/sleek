package com.ztory.lib.sleek.contract;

import android.graphics.Canvas;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;

/**
 * Animation interface, example usage would be to call animTickStart() before drawing what you want
 * to animate, and then animTickEnd() after drawing what you wanted to animate.
 * Created by jonruna on 09/10/14.
 */
public interface ISleekAnimView {

    ISleekAnimView NO_ANIMATION = new ISleekAnimView() {
        @Override
        public void animTickStart(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
            // DO NOTHING
        }
        @Override
        public boolean animTickEnd(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
            // DO NOTHING
            return false;
        }
    };

    void animTickStart(Sleek sleek, Canvas canvas, SleekCanvasInfo info);
    boolean animTickEnd(Sleek sleek, Canvas canvas, SleekCanvasInfo info);

}
