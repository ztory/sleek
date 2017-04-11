package com.ztory.lib.sleek;

import android.graphics.Color;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.base.element.SleekElement;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.layout.SL;
import com.ztory.lib.sleek.mapd.Mapd;
import com.ztory.lib.sleek.util.UtilPx;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestSleekUI {

    /*
{
    background: #d8d8d8;
    color: #666;
    border-radius: 22px;
    margin: 0 0 8px 0;
    font-size: 10px;
    width: 46px;
    height: 46px;
    line-height: 46px;
    text-align: center;
}
    */
    private static final String
            CSS_STRING_1 = "{\n" +
                           "    background: #999;\n" +
                           "    border-radius: 22px;\n" +
                           "    box-shadow: 1px 2px 4px rgba(255, 0, 0, 1.0);\n" +
                           "}",
            CSS_STRING_2 = "{\n" +
                           "    background: #33E77699;\n" +
                           "    border-radius: 8px;\n" +
                           "    color: #4860E3;\n" +
                           "    font-size: 16px;\n" +
                           "    line-height: 30px;\n" +
                           "    text-align: center;\n" +
                           "    vertical-align: bottom;\n" +
                           "}",
            CSS_STRING_3 = "{\n" +
                           "    background: purple;\n" +
                           "    border-radius: 8px;\n" +
                           "    color: #38B0DE;\n" +
                           "    font-size: 20px;\n" +
                           "    line-height: 30px;\n" +
                           "    text-align: center;\n" +
                           "    vertical-align: middle;\n" +
                           "    box-shadow: 1px 2px 4px #38B0DE;\n" +//offset-x | offset-y | blur-radius | color
                           "}",
            CSS_STRING_4 = "{\n" +
                           "    background: rgba(255, 0, 0, 1.0);\n" +
                           "    border-radius: 2px;\n" +
                           "    color: rgba(0, 0, 255, 0.7);\n" +
                           "    font-size: 20px;\n" +
                           "    line-height: 30px;\n" +
                           "    text-align: center;\n" +
                           "    vertical-align: top;\n" +
                           "    box-shadow: 0px 0px 12px rgba(0, 255, 0, 0.5);\n" +//offset-x | offset-y | blur-radius | color
                           "}";

    @Rule
    public ActivityTestRule<SleekActivity> mActivityRule = new ActivityTestRule<>(SleekActivity.class, true, true);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    private static final void loadUIscrollXYareas(SleekCanvas sleekCanvas) {
        UtilTestSleekUI.addUIframeRate(sleekCanvas);
        UtilTestSleekUI.addUIcolorAreaOnClickRandomTranslate(sleekCanvas);
        UtilTestSleekUI.addUIcolorAreaDraggable(sleekCanvas);
        UtilTestSleekUI.addUIcolorAreaAtScreenPercentPos(sleekCanvas, 0.95f, 0);
        UtilTestSleekUI.addUIcolorAreaAtScreenPercentPos(sleekCanvas, 0.0f, 2.0f);
        UtilTestSleekUI.addUIcolorAreaAtScreenPercentPos(sleekCanvas, 2.0f, 0.0f);
        UtilTestSleekUI.addUIcolorAreaAtScreenPercentPos(sleekCanvas, 2.0f, 2.0f);
        UtilTestSleekUI.addUIcolorAreaAtScreenPercentPos(sleekCanvas, 0.98f, 0.98f);
        UtilTestSleekUI.addUIcolorAreaAtScreenPercentPos(sleekCanvas, 1.3f, 1.3f);
        UtilTestSleekUI.reloadUI(sleekCanvas);
    }

    private static final void loadUIscrollXYbasicSleekElements(SleekCanvas sleekCanvas) {
        UtilTestSleekUI.addUIframeRate(sleekCanvas);
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                null,
                new CSSblockBase(CSS_STRING_1),
                0.1f,
                0.1f,
                160,
                160
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Hej!\nDetta är en ny rad.\nOch en till rad?!\n+1\n+2",
                new CSSblockBase(CSS_STRING_2),
                0.8f,
                0.1f,
                600,
                UtilPx.getPixels(sleekCanvas.getContext(), 150)
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Uno\nDos\nTres\nQuattro\nCinco",
                new CSSblockBase(CSS_STRING_3),
                0.15f,
                0.4f,
                600,
                UtilPx.getPixels(sleekCanvas.getContext(), 150)
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Boom Box",
                new CSSblockBase(CSS_STRING_4),
                0.15f,
                0.25f,
                UtilPx.getPixels(sleekCanvas.getContext(), 80),
                UtilPx.getPixels(sleekCanvas.getContext(), 80)
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Hola\nmuchacho\nhallå\nvill du LATTJO?!",
                new CSSblockBase(CSS_STRING_2),
                0.2f,
                0.95f,
                600,
                UtilPx.getPixels(sleekCanvas.getContext(), 150)
        );
        UtilTestSleekUI.addUIcolorAreaDraggable(sleekCanvas);
        UtilTestSleekUI.reloadUI(sleekCanvas);
    }

    private static final void loadUIensureRuntimeAddedViewsGetLoaded(final SleekCanvas sleekCanvas) {
        UtilTestSleekUI.addUIframeRate(sleekCanvas);
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Offscreen #1\nHej!\nDetta är en text!",
                new CSSblockBase(CSS_STRING_2),
                0.95f,
                0.1f,
                600,
                UtilPx.getPixels(sleekCanvas.getContext(), 150)
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Offscreen #2\nHola muchacho hallå vill du LATTJO?!",
                new CSSblockBase(CSS_STRING_2),
                0.2f,
                0.95f,
                600,
                UtilPx.getPixels(sleekCanvas.getContext(), 150)
        );
        SleekElement sleekElement = new SleekElement(
                SleekParam.DEFAULT_TOUCHABLE.newPriority(SleekCanvas.STICKY_TOUCH_PRIO + 10)
        );
        sleekElement.setElementString("Corner Box");
        sleekElement.addCSSblock(new CSSblockBase(CSS_STRING_2));
        sleekElement.getLayout()
                .x(SL.X.PERCENT_CANVAS, -200, null, 1.0f)
                .y(SL.Y.PERCENT_CANVAS, -200, null, 1.0f)
                .w(SL.W.ABSOLUTE, 200, null)
                .h(SL.H.ABSOLUTE, 200, null);
        sleekCanvas.addSleek(sleekElement);
        UtilTestSleekUI.addUIcolorAreaDraggable(sleekCanvas);

        Runnable delayAddedView = new Runnable() {
            @Override
            public void run() {
                UtilTestSleekUI.addUIbasicSleekElement(
                        sleekCanvas,
                        "Offscreen #3 (runtime)\nHola muchacho hallå vill du LATTJO?!",
                        new CSSblockBase(CSS_STRING_2),
                        0.95f,
                        0.95f,
                        600,
                        UtilPx.getPixels(sleekCanvas.getContext(), 150)
                );
            }
        };
        sleekCanvas.getHandler().postDelayed(delayAddedView, 2000);

        final SleekElement sleekElementAddRemove = new SleekElement(
                SleekParam.DEFAULT_TOUCHABLE.newPriority(SleekCanvas.STICKY_TOUCH_PRIO + 10)
        );
        sleekElementAddRemove.setElementString("Add Remove Box");
        sleekElementAddRemove.addCSSblock(new CSSblockBase(CSS_STRING_3));
        sleekElementAddRemove.getLayout()
                .x(SL.X.PERCENT_CANVAS, 0, null, 1.95f)
                .y(SL.Y.PERCENT_CANVAS, 0, null, 1.95f)
                .w(SL.W.ABSOLUTE, 200, null)
                .h(SL.H.ABSOLUTE, 200, null);
        Runnable delayAddedView2 = new Runnable() {
            @Override
            public void run() {
                sleekCanvas.addSleek(sleekElementAddRemove);

                Runnable delayAddedView3 = new Runnable() {
                    @Override
                    public void run() {
                        sleekCanvas.removeSleek(sleekElementAddRemove);

                        // SCROLL BOUNDS AND POSITION SHOULD BE REFRESHED HERE AS WELL !!!!
                    }
                };
                sleekCanvas.getHandler().postDelayed(delayAddedView3, 4000);
            }
        };
        sleekCanvas.getHandler().postDelayed(delayAddedView2, 4000);
    }

    @Test
    public void testCSSblock2() {

        UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

        CSSblockBase cssBlock = new CSSblockBase(CSS_STRING_2);
        assertEquals(0x9933E776, cssBlock.getBackgroundColor().intValue());
        assertEquals("#33E77699", Mapd.get(cssBlock, "background", String.class));
        assertEquals(UtilPx.getPixels(8), cssBlock.getBorderRadius().intValue());
        assertEquals(0xff4860E3, cssBlock.getColor().intValue());
        assertEquals(UtilPx.getPixels(16), cssBlock.getFontSize().intValue());
        assertEquals(UtilPx.getPixels(30), cssBlock.getLineHeight().intValue());
        assertEquals("center", cssBlock.getTextAlign());
        assertEquals("bottom", cssBlock.getVerticalAlign());
    }

    @Test
    public void testCSSblock4() {

        UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

        CSSblockBase cssBlock = new CSSblockBase(CSS_STRING_4);
        assertEquals(Color.argb(255, 255, 0, 0), cssBlock.getBackgroundColor().intValue());
        assertEquals(Color.argb(127, 0, 255, 0), cssBlock.getBoxShadowColor().intValue());
        assertEquals(Color.argb((int) (255 * 0.7), 0, 0, 255), cssBlock.getColor().intValue());
    }

    @Test
    public void testGeneralUI() throws Exception {

        if (mActivityRule.getActivity() == null) {
            throw new IllegalStateException("mActivityRule.getActivity() == null");
        }

        UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

        //loadUIscrollXYareas(mActivityRule.getActivity().getSleekCanvas());
        //loadUIscrollXYbasicSleekElements(mActivityRule.getActivity().getSleekCanvas());
        loadUIensureRuntimeAddedViewsGetLoaded(mActivityRule.getActivity().getSleekCanvas());

        final CountDownLatch activityPauseLatch = new CountDownLatch(1);

        mActivityRule.getActivity().setPauseListener(new Runnable() {
            @Override
            public void run() {
                activityPauseLatch.countDown();
            }
        });

        activityPauseLatch.await();
    }

}
