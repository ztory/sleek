package com.ztory.lib.sleek;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.ztory.lib.sleek.animation.SAVpercent;
import com.ztory.lib.sleek.animation.SAVtransXY;
import com.ztory.lib.sleek.base.SleekColorArea;
import com.ztory.lib.sleek.base.SleekFrameRate;
import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.layout.SL;
import com.ztory.lib.sleek.touch.ISleekTouchRun;
import com.ztory.lib.sleek.touch.SleekTouchHandler;
import com.ztory.lib.sleek.util.UtilPx;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jonruna on 2017-04-06.
 */
public class SleekTestActivityUtil {

    public static void addUIframeRate(SleekCanvas sleekCanvas) {
        SleekFrameRate frameRate = new SleekFrameRate(0xff38B0DE);
        frameRate.getLayout()
                .x(SL.X.POS_CENTER, 0, null)
                .y(SL.Y.PERCENT_CANVAS, -100, null, 1.0f)
                .w(SL.W.ABSOLUTE, 120, null)
                .h(SL.H.ABSOLUTE, 60, null);
        sleekCanvas.addSleek(frameRate);
    }

    public static void addUIcolorAreaDraggable(SleekCanvas sleekCanvas) {
        //http://paletton.com/#uid=33q0u0koYPSdUVTjTSNuiMgHvBj
        final int offColor = 0xffFF5B38, onColor = 0xff38B0DE;
        final SleekColorArea sleekColorArea = new SleekColorArea(
                offColor,
                SleekColorArea.ANTI_ALIASED_TRUE,
                SleekParam.DEFAULT_TOUCHABLE.newLoadable(false)
        );
        sleekColorArea.getLayout()
                .x(SL.X.PERCENT_CANVAS, 0, null, 0.6f)
                .y(SL.Y.PERCENT_CANVAS, 0, null, 0.6f)
                .w(SL.W.ABSOLUTE, 300, null)
                .h(SL.H.ABSOLUTE, 300, null);
        int pixelsFromDip = UtilPx.getPixels(sleekCanvas.getContext(), 8);// 8 DIP converted to pixels
        final Runnable animateToOffColor = new Runnable() {
            @Override
            public void run() {
                sleekColorArea.setSleekAnimView(new SAVpercent(
                        300,
                        false,
                        System.currentTimeMillis(),
                        SAVpercent.getAnimColors(
                                sleekColorArea.getPaint(),
                                sleekColorArea.getPaint().getColor(),
                                offColor
                        ),
                        ISleekDrawView.NO_DRAW
                ));
            }
        };
        final AtomicInteger startDragPosX = new AtomicInteger();
        final AtomicInteger startDragPosY = new AtomicInteger();
        sleekColorArea.setRounded(true, pixelsFromDip);
        sleekColorArea.getTouchHandler().setCheckWantsTouch(true);
        sleekColorArea.getTouchHandler().setTouchPointerDownRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
        sleekColorArea.getTouchHandler().setTouchPointerUpRun(SleekTouchHandler.TOUCH_RUN_RETURN_TRUE);
        sleekColorArea.getTouchHandler().setTouchDownRun(new ISleekTouchRun() {
            @Override
            public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info) {
                startDragPosX.set((int) sleekColorArea.getSleekX());
                startDragPosY.set((int) sleekColorArea.getSleekY());
                sleekColorArea.setSleekAnimView(new SAVpercent(
                        300,
                        false,
                        System.currentTimeMillis(),
                        SAVpercent.getAnimColors(
                                sleekColorArea.getPaint(),
                                sleekColorArea.getPaint().getColor(),
                                onColor
                        ),
                        ISleekDrawView.NO_DRAW
                ));
                return RETURN_TRUE;
            }
        });
        sleekColorArea.getTouchHandler().setTouchUpRun(new ISleekTouchRun() {
            @Override
            public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info) {
                animateToOffColor.run();
                return RETURN_TRUE;
            }
        });
        sleekColorArea.getTouchHandler().setTouchCancelRun(new ISleekTouchRun() {
            @Override
            public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info) {
                animateToOffColor.run();
                return RETURN_TRUE;
            }
        });
        sleekColorArea.getTouchHandler().setTouchMoveRun(new ISleekTouchRun() {
            @Override
            public int onTouch(Sleek view, SleekTouchHandler handler, MotionEvent event, SleekCanvasInfo info) {
                sleekColorArea.setSleekBounds(
                        startDragPosX.get() + (event.getX() - handler.getEventStartX()),
                        startDragPosY.get() + (event.getY() - handler.getEventStartY()),
                        sleekColorArea.getSleekW(),
                        sleekColorArea.getSleekH()
                );
                return RETURN_TRUE;
            }
        });
        sleekCanvas.addSleek(sleekColorArea);
    }

    public static void addUIcolorAreaOnClickRandomTranslate(SleekCanvas sleekCanvas) {
        final SleekColorArea sleekColorArea = new SleekColorArea(
                Color.CYAN,
                SleekColorArea.ANTI_ALIASED_TRUE,
                SleekParam.DEFAULT_TOUCHABLE.newLoadable(false)
        );
        sleekColorArea.getLayout()
                .x(SL.X.PERCENT_CANVAS, 0, null, 0.1f)
                .y(SL.Y.PERCENT_CANVAS, 0, null, 0.1f)
                .w(SL.W.ABSOLUTE, 400, null)
                .h(SL.H.ABSOLUTE, 400, null);
        int pixelsFromDip = UtilPx.getPixels(sleekCanvas.getContext(), 8);// 8 DIP converted to pixels
        sleekColorArea.setRounded(true, pixelsFromDip);
        sleekColorArea.getTouchHandler().setClickAction(
                new Runnable() {
                    @Override
                    public void run() {
                        sleekColorArea.setColor(Color.MAGENTA);
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        sleekColorArea.setColor(Color.CYAN);
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        float randomGoalX = sleekColorArea.getSleekX();
                        randomGoalX = randomGoalX - 200 + (float) (400 * Math.random());
                        float randomGoalY = sleekColorArea.getSleekY();
                        randomGoalY = randomGoalY - 200 + (float) (400 * Math.random());
                        sleekColorArea.setSleekAnimView(
                                new SAVtransXY(
                                        sleekColorArea.getSleekX(),
                                        randomGoalX,
                                        sleekColorArea.getSleekY(),
                                        randomGoalY,
                                        300,
                                        new ISleekDrawView() {
                                            @Override
                                            public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                                                float correctX = -1.0f, correctY = -1.0f;

                                                if (sleekColorArea.getSleekX() < 0) {
                                                    correctX = 0;
                                                }
                                                else if (sleekColorArea.getSleekX() > info.width - sleekColorArea.getSleekW()) {
                                                    correctX = info.width - sleekColorArea.getSleekW();
                                                }

                                                if (sleekColorArea.getSleekY() < 0) {
                                                    correctY = 0;
                                                }
                                                else if (sleekColorArea.getSleekY() > info.height - sleekColorArea.getSleekH()) {
                                                    correctY = info.height - sleekColorArea.getSleekH();
                                                }

                                                if (correctX != -1.0f || correctY != -1.0f) {
                                                    sleekColorArea.setSleekAnimView(
                                                            new SAVtransXY(
                                                                    sleekColorArea.getSleekX(),
                                                                    (correctX != -1.0f) ? correctX : sleekColorArea.getSleekX(),
                                                                    sleekColorArea.getSleekY(),
                                                                    (correctY != -1.0f) ? correctY : sleekColorArea.getSleekY(),
                                                                    150,
                                                                    ISleekDrawView.NO_DRAW
                                                            )
                                                    );
                                                }
                                            }
                                        }
                                )
                        );
                    }
                }
        );
        sleekCanvas.addSleek(sleekColorArea);
    }

    public static void removeAllUIfromSleekCanvas(SleekCanvas sleekCanvas) {
        sleekCanvas.removeAllSleek(false);
        sleekCanvas.removeAllSleek(true);
    }

}
