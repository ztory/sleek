package com.ztory.lib.sleek;

import android.graphics.Canvas;
import com.ztory.lib.sleek.base.element.SleekElement;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.contract.ISleekAnimRun;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.contract.ISleekParentDid;
import com.ztory.lib.sleek.layout.SL.H;
import com.ztory.lib.sleek.layout.SL.W;

/**
 * Created by jonruna on 2017-09-18.
 */
public class LoadOverlay extends SleekElement {

  public final static CSSblock
      LOAD_OVERLAY = new CSSblockBase("{" +
          "color:#f9f9f900;" +
          "background-color: rgba(255, 0, 0, 0.0);" +
          "font-size: 30px;" +
          "line-height: 34px;" +
          "text-align: center;" +
          "vertical-align: middle;" +
          "}"
      ),
      LOAD_OVERLAY_ANIMATION = new CSSblockBase("{" +
          "color:#f9f9f9;" +
          "background-color: rgba(0, 0, 255, 0.9);" +
          "}"
      );

  public LoadOverlay() {
    super(FIXED_POSITION_TRUE, SleekCanvas.STICKY_TOUCH_PRIO);
    setParentDidAddListener(new ISleekParentDid() {
      @Override
      public void parentDid(SleekCanvas sleekCanvas, SleekParent sleekParent) {
        removeCSS(LOAD_OVERLAY_ANIMATION);
        addCSStransition(LOAD_OVERLAY_ANIMATION)
            .setDuration(4000)
            .setDoneListener(new ISleekDrawView() {
              @Override
              public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                removeCSStransition(LOAD_OVERLAY_ANIMATION)
                    .setDuration(4000)
                    .setDoneListener(new ISleekDrawView() {
                      @Override
                      public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                        parentRemove(true);
                      }
                    });
              }
            });
      }
    });
    setElementString("Loading...");
    getLayout()
        .w(W.PERCENT_CANVAS, 0, null, 1.0f)
        .h(H.PERCENT_CANVAS, 0, null, 1.0f);
    getTouchHandler().setClickActionNone();

    addCSS(LOAD_OVERLAY);
  }

  public void addToSleekCanvas(final SleekCanvas slkCanvas) {
    if (slkCanvas == null) {
      return;
    }
    slkCanvas.addPreDrawRun(new ISleekAnimRun() {
      @Override
      public void run(SleekCanvasInfo info) {
        slkCanvas.addSleek(LoadOverlay.this);
//        setSleekAnimView(
//            new SAVfade(0, 200, 300, getBackground().getPaint(), ISleekDrawView.NO_DRAW)
//        );
      }
    });
  }
}
