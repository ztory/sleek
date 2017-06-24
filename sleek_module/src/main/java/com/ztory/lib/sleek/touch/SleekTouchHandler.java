package com.ztory.lib.sleek.touch;

import android.view.MotionEvent;

import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.util.UtilSleekTouch;

/**
 * General purpose TouchHandler used to simplify custom touch behaviour.
 * Created by jonruna on 09/10/14.
 */
public class SleekTouchHandler {

    public static final ISleekTouchRun TOUCH_RUN_RETURN_TRUE = new ISleekTouchRun() { @Override
        public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo canvasInfo) {
            return RETURN_TRUE;
        }
    };

    private Sleek sleekView;

    private boolean
    wantsTouch,
    checkWantsTouch = true;

    private ISleekTouchRun
    preTouchRun,
    postTouchRun,
    touchDownRun,
    touchUpRun,
    touchCancelRun,
    touchMoveRun,
    touchPointerDownRun,
    touchPointerUpRun;

    private boolean
    hasPreTouchRun,
    hasPostTouchRun,
    hasTouchDownRun,
    hasTouchUpRun,
    hasTouchCancelRun,
    hasTouchMoveRun,
    hasTouchPointerDownRun,
    hasTouchPointerUpRun;

    private int returnInt;

    private long lastTouchDown;
    private float eventStartX, eventStartY;

    public SleekTouchHandler(Sleek theSleekView) {
        sleekView = theSleekView;
    }

    public void setWantsTouchFalse() {
        wantsTouch = false;
    }

    public void setWantsTouchTrue() {
        wantsTouch = true;
    }

    public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo canvasInfo) {

        if (checkWantsTouch) {
            if (UtilSleekTouch.isTouchDown(event)) {
                if (UtilSleekTouch.isTouchInside(event, sleekView)) {
                    wantsTouch = true;
                    setLastTouchDown(System.currentTimeMillis());
                    setEventStartX(event.getX());
                    setEventStartY(event.getY());
                }
                else {
                    wantsTouch = false;
                }
            }

            if (!wantsTouch) return false;
        }
        else if (
                UtilSleekTouch.isTouchDown(event) &&
                UtilSleekTouch.isTouchInside(event, sleekView)
                ) {
            wantsTouch = true;
            setLastTouchDown(System.currentTimeMillis());
            setEventStartX(event.getX());
            setEventStartY(event.getY());
        }

        if (hasPreTouchRun) {
            returnInt = preTouchRun.onTouch(sleekView, SleekTouchHandler.this, event, canvasInfo);
            if (returnInt == ISleekTouchRun.RETURN_TRUE) return true;
            else if (returnInt == ISleekTouchRun.RETURN_FALSE) return false;
        }

        switch (event.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
            if (hasTouchDownRun) {
                returnInt = touchDownRun.onTouch(sleekView, SleekTouchHandler.this, event, canvasInfo);
                if (returnInt == ISleekTouchRun.RETURN_TRUE) return true;
                else if (returnInt == ISleekTouchRun.RETURN_FALSE) return false;
            }
            break;
        case MotionEvent.ACTION_UP:
            wantsTouch = false;
            if (hasTouchUpRun) {
                returnInt = touchUpRun.onTouch(sleekView, SleekTouchHandler.this, event, canvasInfo);
                if (returnInt == ISleekTouchRun.RETURN_TRUE) return true;
                else if (returnInt == ISleekTouchRun.RETURN_FALSE) return false;
            }
            break;
        case MotionEvent.ACTION_CANCEL:
            wantsTouch = false;
            if (hasTouchCancelRun) {
                returnInt = touchCancelRun.onTouch(sleekView, SleekTouchHandler.this, event, canvasInfo);
                if (returnInt == ISleekTouchRun.RETURN_TRUE) return true;
                else if (returnInt == ISleekTouchRun.RETURN_FALSE) return false;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (hasTouchMoveRun) {
                returnInt = touchMoveRun.onTouch(sleekView, SleekTouchHandler.this, event, canvasInfo);
                if (returnInt == ISleekTouchRun.RETURN_TRUE) return true;
                else if (returnInt == ISleekTouchRun.RETURN_FALSE) return false;
            }
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            if (hasTouchPointerDownRun) {
                returnInt = touchPointerDownRun.onTouch(sleekView, SleekTouchHandler.this, event, canvasInfo);
                if (returnInt == ISleekTouchRun.RETURN_TRUE) return true;
                else if (returnInt == ISleekTouchRun.RETURN_FALSE) return false;
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            if (hasTouchPointerUpRun) {
                returnInt = touchPointerUpRun.onTouch(sleekView, SleekTouchHandler.this, event, canvasInfo);
                if (returnInt == ISleekTouchRun.RETURN_TRUE) return true;
                else if (returnInt == ISleekTouchRun.RETURN_FALSE) return false;
            }
            break;
        }

        if (hasPostTouchRun) {
            returnInt = postTouchRun.onTouch(sleekView, SleekTouchHandler.this, event, canvasInfo);
            if (returnInt == ISleekTouchRun.RETURN_TRUE) return true;
            else if (returnInt == ISleekTouchRun.RETURN_FALSE) return false;
        }

        return false;
    }

    private static void runSafeRunnable(Runnable runnable) {
        if (runnable != null) runnable.run();
    }

    /** Just catch clicks */
    public void setClickActionNone() {
        setTouchDownRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
        setTouchUpRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
        setTouchCancelRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
        setTouchMoveRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
        setTouchPointerDownRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
        setTouchPointerUpRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
    }

    public void setClickAction(
        final Runnable downAction,
        final Runnable upAction,
        final Runnable clickAction
    ) {
        setClickAction(true, downAction, upAction, clickAction);
    }

    /** Helper metod that sets setCheckWantsTouch(true) and sets actions for DOWN, MOVE, CANCEL and UP */
    public void setClickAction(
            final boolean execUpActionOnClick,
            final Runnable downAction,
            final Runnable upAction,
            final Runnable clickAction
            ) {

        setCheckWantsTouch(true);

        setTouchPointerDownRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
        setTouchPointerUpRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);

        ISleekTouchRun runTouchDownRun = new ISleekTouchRun() {
            @Override
            public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info) {
                runSafeRunnable(downAction);
                return RETURN_TRUE;
            }
        };
        setTouchDownRun(runTouchDownRun);

        ISleekTouchRun runTouchMoveRun = new ISleekTouchRun() {

            private boolean runIsTouchInside;
            private long runLastTouchDownTs;

            @Override
            public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info) {

                if (runLastTouchDownTs != handler.getLastTouchDown()) {
                    runLastTouchDownTs = handler.getLastTouchDown();
                    runIsTouchInside = UtilSleekTouch.isTouchInside(event, sleekView);

                    if (!runIsTouchInside) runSafeRunnable(upAction);

                    return RETURN_TRUE;
                }

                if (runIsTouchInside == UtilSleekTouch.isTouchInside(event, sleekView)) {
                    return RETURN_TRUE;
                }

                runIsTouchInside = UtilSleekTouch.isTouchInside(event, sleekView);

                if (runIsTouchInside) runSafeRunnable(downAction);
                else runSafeRunnable(upAction);

                return RETURN_TRUE;
            }
        };
        setTouchMoveRun(runTouchMoveRun);

        ISleekTouchRun runTouchCancelRun = new ISleekTouchRun() {
            @Override
            public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info) {
                runSafeRunnable(upAction);
                return RETURN_FALSE;
            }
        };
        setTouchCancelRun(runTouchCancelRun);

        ISleekTouchRun runTouchUpRun = new ISleekTouchRun() {
            @Override
            public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info) {

                if (execUpActionOnClick) {
                    runSafeRunnable(upAction);
                }

                if (UtilSleekTouch.isTouchInside(event, sleekView)) {
                    runSafeRunnable(clickAction);
                }
                return RETURN_TRUE;
            }
        };
        setTouchUpRun(runTouchUpRun);

    }

    public ISleekTouchRun getTouchDownRun() {
        return touchDownRun;
    }

    public void setCheckWantsTouch(boolean checkWantsTouch) {
        this.checkWantsTouch = checkWantsTouch;
    }

    public void setPreTouchRun(ISleekTouchRun preTouchRun) {
        this.preTouchRun = preTouchRun;
        hasPreTouchRun = this.preTouchRun != null;
    }

    public void setPostTouchRun(ISleekTouchRun postTouchRun) {
        this.postTouchRun = postTouchRun;
        hasPostTouchRun = this.postTouchRun != null;
    }

    public void setTouchDownRun(ISleekTouchRun touchDownRun) {
        this.touchDownRun = touchDownRun;
        hasTouchDownRun = this.touchDownRun != null;
    }

    public void setTouchUpRun(ISleekTouchRun touchUpRun) {
        this.touchUpRun = touchUpRun;
        hasTouchUpRun = this.touchUpRun != null;
    }

    public void setTouchCancelRun(ISleekTouchRun touchCancelRun) {
        this.touchCancelRun = touchCancelRun;
        hasTouchCancelRun = this.touchCancelRun != null;
    }

    public void setTouchMoveRun(ISleekTouchRun touchMoveRun) {
        this.touchMoveRun = touchMoveRun;
        hasTouchMoveRun = this.touchMoveRun != null;
    }

    public void setTouchPointerDownRun(ISleekTouchRun touchPointerDownRun) {
        this.touchPointerDownRun = touchPointerDownRun;
        hasTouchPointerDownRun = this.touchPointerDownRun != null;
    }

    public void setTouchPointerUpRun(ISleekTouchRun touchPointerUpRun) {
        this.touchPointerUpRun = touchPointerUpRun;
        hasTouchPointerUpRun = this.touchPointerUpRun != null;
    }

    public long getLastTouchDown() {
        return lastTouchDown;
    }

    public void setLastTouchDown(long lastTouchDown) {
        this.lastTouchDown = lastTouchDown;
    }

    public float getEventStartX() {
        return eventStartX;
    }

    public void setEventStartX(float eventStartX) {
        this.eventStartX = eventStartX;
    }

    public float getEventStartY() {
        return eventStartY;
    }

    public void setEventStartY(float eventStartY) {
        this.eventStartY = eventStartY;
    }

}
