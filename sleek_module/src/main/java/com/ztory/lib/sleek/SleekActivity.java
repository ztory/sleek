package com.ztory.lib.sleek;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by jonruna on 2017-04-06.
 */
public class SleekActivity extends Activity {

    protected Handler uiHandler;

    protected SleekCanvas sleekCanvas = null;

    protected Runnable pauseListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        uiHandler = new Handler();
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public void setSleekCanvas(SleekCanvas theSleekCanvas) {
        sleekCanvas = theSleekCanvas;
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
