package com.ztory.lib.sleek;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.ztory.lib.sleek.base.scroller.xy.SleekScrollerXY;

/**
 * Created by jonruna on 2017-04-06.
 */
public class SleekActivity extends Activity {

    protected SleekCanvas sleekCanvas = null;

    protected Runnable pauseListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        sleekCanvas = new SleekCanvas(this);
        sleekCanvas.setBackgroundColor(0xffffffff);
        //sleekCanvas.setSleekScroller(new SleekScrollerBase(true));
        sleekCanvas.setSleekScroller(new SleekScrollerXY(true, true));
        setContentView(sleekCanvas);
    }

    public SleekCanvas getSleekCanvas() {
        return sleekCanvas;
    }

    public void setPauseListener(Runnable thePauseListener) {
        pauseListener = thePauseListener;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (pauseListener != null) {
            pauseListener.run();
        }
    }

}
