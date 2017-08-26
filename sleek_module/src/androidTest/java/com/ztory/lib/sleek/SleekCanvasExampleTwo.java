package com.ztory.lib.sleek;

import android.content.Context;
import com.ztory.lib.sleek.assumption.Assumeable;
import com.ztory.lib.sleek.base.element.SleekCSSanim;
import com.ztory.lib.sleek.base.element.SleekElement;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.base.scroller.xy.SleekScrollerXY;
import com.ztory.lib.sleek.layout.SL;
import com.ztory.lib.sleek.layout.SL.H;
import com.ztory.lib.sleek.layout.SL.W;
import com.ztory.lib.sleek.util.UtilPx;
import com.ztory.lib.sleek.util.UtilSleekLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017-08-08.
 */

public class SleekCanvasExampleTwo extends SleekCanvas {

  private static final String
      CSS_TOOLBAR = "{" +
          "background-color: rgba(30, 68, 210, 0.7);" +
    //          "background-image: url(\"https://example.com/example.png\");" +
    //          "background-size: cover;" +
    //          "border-radius: 22px;" +
          //"border: 2px solid #0f0;" +//TODO border does not work with transparent background !
          "box-shadow: 0px 0px 8px rgba(255, 0, 0, 1.0);" +
          "padding: 0px 10px 0px 30px;" +
          "color: #eee;" +
          "font-size: 20px;" +
          "line-height: 20px;" +
          "text-align: left;" +
          "vertical-align: middle;" +
          "text-shadow: 1px 1px 4px #38B0DE;" +
          "}",
      CSS_TOOLBAR_ACTIVE = "{" +
//          "padding: 10px 10px 0px 40px;" +
//          "color: #f00;" +
//          "font-size: 30px;" +
          //"border: 1px solid #202020;" +
          //"box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);" +
          "line-height: 50px;" +
          "text-shadow: 1px 1px 8px #ff0000;" +
          "}",
      CSS_CELL_BASIC = "{" +
          "background-color: #121212;" +
//          "background-image: url(\"https://example.com/example.png\");" +
          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Ms._magazine_Cover_-_Winter_2009.jpg/577px-Ms._magazine_Cover_-_Winter_2009.jpg\");" +
//          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/3/39/Alexander_Meiklejohn_Time_magazine_cover%2C_October_1%2C_1928.jpg\");" +
//          "background-image: url(\"http://lorempixel.com/320/320/people/\");" +
          "background-size: cover;" +
          "border-radius: 6px;" +
          "border: 1px solid #121212;" +
          "box-shadow: 0px 0px 20px rgba(30, 68, 210, 0.7);" +
          "padding: 10px;" +
          "color: #e8e8e8;" +
          "font-size: 26px;" +
          "line-height: 20px;" +
          "text-align: left;" +
          "vertical-align: top;" +
//          "text-shadow: 1px 1px 2px black;" +
          "}",
      CSS_CELL_BASIC_PRESSED = "{" +
          "background-color: #FFC638;" +
//          "background-image: url(\"https://example.com/example.png\");" +
//          "background-size: cover;" +
          "border-radius: 2px;" +
//          "border: 1px solid #0000ff;" +
          "box-shadow: 1px 2px 4px rgba(120, 130, 140, 0.5);" +
//          "padding: 20px;" +
//          "color: #666;" +
//          "font-size: 20px;" +
//          "line-height: 46px;" +
//          "text-align: center;" +
//          "vertical-align: middle;" +
//          "text-shadow: 1px 1px 2px black;" +
          "}",
      CSS_CELL_BASIC_CLICKED = "{" +
//          "background-color: #4860E3;" +
//          "background-image: url(\"https://example.com/example.png\");" +
//          "background-size: cover;" +
          "border-radius: 40px;" +
//          "border: 1px solid #FFC638;" +
          "box-shadow: 1px 2px 14px rgba(255, 0, 0, 0.9);" +
//"box-shadow: 1px 2px 4px rgba(120, 130, 140, 0.5);" +
//          "padding: 30px;" +
//          "color: #FFC638;" +
//          "font-size: 24px;" +
//          "line-height: 40px;" +
//          "text-align: center;" +
//          "vertical-align: middle;" +
//          "text-shadow: 1px 1px 2px black;" +
          "}";

  private static final CSSblock
      cellBasicCSS = new CSSblockBase(CSS_CELL_BASIC),
      cellBasicPressedCSS = new CSSblockBase(CSS_CELL_BASIC_PRESSED),
      cellBasicClickedCSS = new CSSblockBase(CSS_CELL_BASIC_CLICKED);

  private static final int CELL_WIDTH = UtilPx.getPixels(120);

  private final int toolbarHeight = UtilPx.getPixels(102);

  private final List<SleekElement> sleekElementList = new ArrayList<>(20);

  public SleekCanvasExampleTwo(Context context) {
    super(context);

    setBackgroundColor(0xffccccff);

    SleekScrollerXY sleekScrollerXY = new SleekScrollerXY(false, true);
    sleekScrollerXY.setMarginBottom(toolbarHeight);
    sleekScrollerXY.setPaddingBottom(UtilPx.getPixels(40));
    setSleekScroller(sleekScrollerXY);

    //setLoadPaddingEqualToSize(false);
    //setHeightLoadPadding(UtilPx.getPixels(1000));
    //setHeightLoadPadding(0);

    addFrameRate();

    addToolbar();

    addChildElements();
  }

  private void addChildElements() {
    SleekElement iterElement;
    for (int i = 0; i < 400; i++) {
      iterElement = getSleekElementCellBasic();
      iterElement.setElementString("Cell #" + i);
      sleekElementList.add(iterElement);
    }
//    UtilSleekLayout.initVerticalListLayout(
//        sleekElementList,
//        UtilPx.getPixels(40),
//        UtilPx.getPixels(40)
//    );
    UtilSleekLayout.initVerticalGridLayout(
        sleekElementList,
        CELL_WIDTH,//columnWidth
        UtilPx.getPixels(40),//topSpacing
        UtilPx.getPixels(20),//horizontalSpacing
        true,//verticalSpacingMatchHorizontalSpacing
        0//verticalSpacing
    );
    addSleek(sleekElementList);

    //TODO FIX GRID OF SCROLLABLE VIEWS, MAKE SURE AS LITTLE+CLEAR CODE AS POSSIBLE !!!!
  }

  private void addFrameRate() {
    SleekFrameRate frameRate = new SleekFrameRate(0xff38B0DE);
    frameRate.getLayout()
        .x(SL.X.POS_CENTER, 0, null)
        .y(SL.Y.PERCENT_CANVAS, -100, null, 1.0f)
        .w(W.ABSOLUTE, 120, null)
        .h(H.ABSOLUTE, 60, null);
    addSleek(frameRate);
  }

  private void addToolbar() {
    final CSSblock toolbarCSS = new CSSblockBase(CSS_TOOLBAR);
    final CSSblock toolbarActiveCSS = new CSSblockBase(CSS_TOOLBAR_ACTIVE);

    final SleekElement toolbar = new SleekElement(SleekElement.FIXED_POSITION_TRUE);
    toolbar.setElementString("Sleek" + "\nMore power to the UI");
    toolbar.addCSS(toolbarCSS);
    toolbar.getLayout()// X and W are stretched outside screen to hide WEST / EAST border+shadow
        .x(SL.X.ABSOLUTE, -UtilPx.getPixels(10), null)
        .y(SL.Y.PERCENT_CANVAS, -toolbarHeight, null, 1.0f)
        .w(W.PERCENT_CANVAS, -UtilPx.getPixels(20), null, 1.0f)
        .h(H.ABSOLUTE, toolbarHeight, null);
    toolbar.getBackground().getTouchHandler().setClickAction(
        new Runnable() {
          @Override
          public void run() {
            toolbar.addCSStransition(toolbarActiveCSS);
          }
        }, new Runnable() {
          @Override
          public void run() {
            toolbar.removeCSStransition(toolbarActiveCSS);
          }
        }, new Runnable() {
          @Override
          public void run() {
            if (sleekElementList.get(0).isAddedToParent()) {
              removeSleek(sleekElementList);
            } else {
              addSleek(sleekElementList);
            }
          }
        }
    );
    addSleek(toolbar);
  }

  private static SleekElement getSleekElementCellBasic() {
    final SleekElement sleekElement = new SleekElement();
    sleekElement.addCSS(cellBasicCSS);
    sleekElement.setClickAction(
        cellBasicPressedCSS,
        cellBasicClickedCSS,
        new Assumeable<SleekCSSanim>() {
          @Override
          public void assume(SleekCSSanim cssAnimation) {
            cssAnimation.setDuration(1000);
            if (sleekElement.getSleekW() != CELL_WIDTH) {
              cssAnimation.setGoalW(CELL_WIDTH);
            } else {
              cssAnimation.setGoalW(sleekElement.getSleekW() + UtilPx.getPixels(20));
            }
          }
        }
    );
    sleekElement.getLayout()
        .w(W.ABSOLUTE, CELL_WIDTH, null)
        //.h(H.ABSOLUTE, UtilPx.getPixels(300), null);
        .h(H.ABSOLUTE, UtilPx.getPixels(100) + (int) (UtilPx.getPixels(100) * Math.random()), null);
    return sleekElement;
  }

}
