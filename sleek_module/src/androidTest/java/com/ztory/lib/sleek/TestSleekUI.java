package com.ztory.lib.sleek;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ztory.lib.sleek.animation.SAVfade;
import com.ztory.lib.sleek.animation.SAVtransXYWH;
import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.base.element.SleekElement;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.layout.SL;
import com.ztory.lib.sleek.mapd.Mapd;
import com.ztory.lib.sleek.util.UtilDownload;
import com.ztory.lib.sleek.util.UtilPx;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

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
            FEED_ITEM_STRING_1 = "Now that there is the Tec-9, a crappy spray gun from South Miami. This gun is advertised as the most popular gun in American crime. Do you believe that shit? It actually says that in the little book that comes with it: the most popular gun in American crime. Like they're actually proud of that shit.",
            FEED_ITEM_STRING_2 = "Well, the way they make shows is, they make one show. That show's called a pilot. Then they show that show to the people who make shows, and on the strength of that one show they decide if they're going to make more shows. Some pilots get picked and become television programs. Some don't, become nothing. She starred in one of the ones that became nothing.",
            FEED_ITEM_STRING_3 = "Look, just because I don't be givin' no man a foot massage don't make it right for Marsellus to throw Antwone into a glass motherfuckin' house, fuckin' up the way the nigger talks. Motherfucker do that shit to me, he better paralyze my ass, 'cause I'll kill the motherfucker, know what I'm sayin'?",
            FEED_ITEM_STRING_4 = "Do you see any Teletubbies in here? Do you see a slender plastic tag clipped to my shirt with my name printed on it? Do you see a little Asian child with a blank expression on his face sitting outside on a mechanical helicopter that shakes when you put quarters in it? No? Well, that's what you see at a toy store. And you must think you're in a toy store, because you're here shopping for an infant named Jeb.";

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
                           "}",
            CSS_STRING_5 = "{\n" +
                           "    background: #FF5B38;\n" +
                           "    border-radius: 2px;\n" +
                           "    color: #38B0DE;\n" +
                           "    font-size: 20px;\n" +
                           "    line-height: 30px;\n" +
                           "    text-align: center;\n" +
                           "    vertical-align: top;\n" +
                           "    box-shadow: 0px 1px 2px #FFA03899;\n" +//offset-x | offset-y | blur-radius | color
                           "}",
            CSS_STRING_6 = "{\n" +
                           "    background: #FF5B38;\n" +
                           "    border-radius: 2px;\n" +
                           "    color: #38B0DE;\n" +
                           "    font-size: 20px;\n" +
                           "    line-height: 30px;\n" +
                           "    text-align: center;\n" +
                           "    vertical-align: middle;\n" +
                           "    box-shadow: 0px 1px 2px #FFA03899;\n" +//offset-x | offset-y | blur-radius | color
                           "}",
            CSS_STRING_7 = "{\n" +
                           "    background: #FF5B38;\n" +
                           "    border-radius: 2px;\n" +
                           "    color: #38B0DE;\n" +
                           "    font-size: 20px;\n" +
                           "    line-height: 30px;\n" +
                           "    text-align: center;\n" +
                           "    vertical-align: bottom;\n" +
                           "    box-shadow: 0px 1px 2px #FFA03899;\n" +//offset-x | offset-y | blur-radius | color
                           "}",
            CSS_FEED_ITEM_DEBUG =
                    "{\n" +
                    "    background: #fdfdfd;\n" +
                    "    border-radius: 3px;\n" +
                    "    color: #121212;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +//"    box-shadow: -30px -30px 10px #ff0000;\n"
                    "}",
            CSS_PADDING_X4 =// top | right | bottom | left
                    "{\n" +
                    "    padding: 5px 10px 15px 20px;\n" +
                    "}",
            CSS_PADDING_X3 =// top | left+right | bottom
                    "{\n" +
                    "    padding: 10px 5px 20px;\n" +
                    "}",
            CSS_PADDING_X2 =// top+bottom | left+right
                    "{\n" +
                    "    padding: 10px 20px;\n" +
                    "}",
            CSS_PADDING_X1 =// top+right+bottom+left
                    "{\n" +
                    "    padding: 10px;\n" +
                    "}",
            CSS_FEED_ITEM_PADDING =
                    "{\n" +
                    "    background: #fdfdfd;\n" +
                    "    border-radius: 2px;\n" +
                    "    color: #121212;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    padding: 5px 10px 15px 20px;\n" +
                    //"    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "}",
            CSS_FEED_ITEM_PADDING_BOX_SHADOW =
                    "{\n" +
                    "    background: #fdfdfd;\n" +
                    "    border-radius: 2px;\n" +
                    "    color: #121212;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: bottom;\n" +
                    "    padding: 20px 10px;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "}",
            CSS_FEED_ITEM_IMAGE =
                    "{\n" +
                    "    background: #33E776;\n" +
                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n" +
                    "    border-radius: 8px;\n" +
                    "    color: #121212;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    padding: 12px;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "}",
            CSS_FEED_ITEM_IMAGE_LOCAL =
                    "{\n" +
                    "    background: #33E776;\n" +
                    "    background-image: url(\"sym_def_app_icon\");\n" +
                    "    border-radius: 8px;\n" +
                    "    color: #121212;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    padding: 12px;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "}",
            CSS_FEED_ITEM_IMAGE_CONTAIN =
                    "{\n" +
                    "    background: #33E776;\n" +
//                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n" +
                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/3/39/Beach_Days.jpg\");\n" +
                    "    background-size: contain;\n" +
                    "    border-radius: 8px;\n" +
                    "    color: #121212;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    padding: 12px;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "}",
            CSS_FEED_ITEM_IMAGE_COVER =
                    "{\n" +
                    "    background: #33E776;\n" +
//                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n" +
                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/3/39/Beach_Days.jpg\");\n" +
                    "    background-size: cover;\n" +
                    "    border-radius: 8px;\n" +
                    "    color: #121212;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    padding: 12px;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "}",
            CSS_FEED_ITEM_IMAGE_COVER_TWO =
                    "{\n" +
                    "    background: #33E776;\n" +
                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n" +
//                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/3/39/Beach_Days.jpg\");\n" +
                    "    background-size: cover;\n" +
                    "    border-radius: 8px;\n" +
                    "    color: #121212;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    padding: 12px;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "}",
            CSS_BORDER_1 =
                    "{\n" +
                    "    background: #33E776;\n" +
                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/4/4e/McCarthyBeachStatePark.jpg\");\n" +
                    "    background-size: contain;\n" +
                    "    border-radius: 8px;\n" +
                    "    color: #993333;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    padding: 12px;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "    border: 1px solid #ff0000;\n" +
                    "}",
            CSS_BORDER_2 =
                "{\n" +
                    "    background: #33E776;\n" +
                    "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n" +
                    "    background-size: cover;\n" +
                    "    border-radius: 8px;\n" +
                    "    color: #333399;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 24px;\n" +
                    "    text-align: left;\n" +
                    "    vertical-align: top;\n" +
                    "    padding: 12px;\n" +
                    "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n" +
                    "    border: 1px solid #0000ff;\n" +
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
                        // Scroll bounds and position are refreshed in removeSleek() now as well.
                        sleekCanvas.removeSleek(sleekElementAddRemove);
                    }
                };
                sleekCanvas.getHandler().postDelayed(delayAddedView3, 4000);
            }
        };
        sleekCanvas.getHandler().postDelayed(delayAddedView2, 4000);
    }

    private static final void loadUIruntimeDelayAddViews(final SleekCanvas sleekCanvas) {
        final int boxWidth = UtilPx.getPixels(sleekCanvas.getContext(), 160);
        final int boxHeight = UtilPx.getPixels(sleekCanvas.getContext(), 160);
        for (int i = 0; i < 10; i++) {
            final int finalIndex = i;
            Runnable delayAddedView = new Runnable() {
                @Override
                public void run() {
                    UtilTestSleekUI.addUIbasicSleekElement(
                            sleekCanvas,
                            "ÅÄÖ Runtime Square #" + finalIndex,
                            new CSSblockBase(CSS_STRING_5),
                            0.55f + (finalIndex / 5.0f),
                            0.55f + (finalIndex / 5.0f),
                            boxWidth,
                            boxHeight
                    );
                }
            };
            sleekCanvas.getHandler().postDelayed(delayAddedView, 1000 * i);
        }
    }

    private static final void loadUIverticalTextCentering(final SleekCanvas sleekCanvas) {

        final int boxWidth = UtilPx.getPixels(sleekCanvas.getContext(), 200);
        final int boxHeight = UtilPx.getPixels(sleekCanvas.getContext(), 200);

        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Detta är ett ÅÄÖ test komplett med åäö och yjq!",
                new CSSblockBase(CSS_STRING_5),
                0.1f,
                0.1f,
                boxWidth,
                boxHeight
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Detta är ett ÅÄÖ test komplett med åäö och yjq!",
                new CSSblockBase(CSS_STRING_6),
                0.1f,
                0.35f,
                boxWidth,
                boxHeight
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Detta är ett ÅÄÖ test komplett med åäö och yjq!",
                new CSSblockBase(CSS_STRING_7),
                0.1f,
                0.6f,
                boxWidth,
                boxHeight
        );

        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Lynx ÅÄÖ yjq Labs",
                new CSSblockBase(CSS_STRING_5),
                0.5f,
                0.1f,
                boxWidth,
                UtilPx.getPixels(sleekCanvas.getContext(), 40)
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Lynx ÅÄÖ yjq Labs",
                new CSSblockBase(CSS_STRING_6),
                0.5f,
                0.35f,
                boxWidth,
                UtilPx.getPixels(sleekCanvas.getContext(), 40)
        );
        UtilTestSleekUI.addUIbasicSleekElement(
                sleekCanvas,
                "Lynx ÅÄÖ yjq Labs",
                new CSSblockBase(CSS_STRING_7),
                0.5f,
                0.6f,
                boxWidth,
                UtilPx.getPixels(sleekCanvas.getContext(), 40)
        );
    }

    private static final void loadUIscrollYcompleteFeedUItransXYWH(final SleekCanvas sleekCanvas) {

        sleekCanvas.setBackgroundColor(0xffe8e8e8);

        UtilTestSleekUI.addUIframeRate(sleekCanvas);

        int feedItemTopMargin = UtilPx.getPixels(80);
        int feedItemHorizontalMargin = UtilPx.getPixels(80);
        int feedItemHeight = UtilPx.getPixels(400);

        SleekElement sleekFeedItem, lastSleekFeedItem = null;
        String feedItemString;
        for (int i = 1; i <= 24; i++) {

            sleekFeedItem = new SleekElement(
                    SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
            );

            final SleekElement finalFeedItem = sleekFeedItem;
            finalFeedItem.getTouchHandler().setClickAction(
                    new Runnable() {
                        long touchTs = 0;
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - touchTs < 2000) {
                                return;
                            }
                            touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 100,
                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 100,
                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 200,
                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 200,
                                    500,
                                    new ISleekDrawView() {
                                        @Override
                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() + 300,
                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() + 300,
                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() - 600,
                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() - 600,
                                                    1000,
                                                    new ISleekDrawView() {
                                                        @Override
                                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 200,
                                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 200,
                                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 400,
                                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 400,
                                                                    500,
                                                                    ISleekDrawView.NO_DRAW
                                                            ));
                                                        }
                                                    }
                                            ));
                                        }
                                    }
                            ));

//                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
//                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() + 160,
//                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() + 160,
//                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() - 160,
//                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() - 160,
//                                    500,
//                                    ISleekDrawView.NO_DRAW
//                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
//                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
//                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 160,
//                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 160,
//                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 160,
//                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 160,
//                                    500,
//                                    ISleekDrawView.NO_DRAW
//                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
//                            finalFeedItem.parentRemove(true);
//                            sleekCanvas.getHandler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    sleekCanvas.addSleek(finalFeedItem);
//                                }
//                            }, 500);
                        }
                    }
            );

            if (i % 4 == 0) {
                feedItemString = FEED_ITEM_STRING_1;
            }
            else if (i % 3 == 0) {
                feedItemString = FEED_ITEM_STRING_2;
            }
            else if (i % 2 == 0) {
                feedItemString = FEED_ITEM_STRING_3;
            }
            else {
                feedItemString = FEED_ITEM_STRING_4;
            }
            sleekFeedItem.setElementString(feedItemString);

            sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_DEBUG));
            sleekFeedItem.getLayout()
                    .x(SL.X.POS_CENTER, 0, null)
                    .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
                    .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
                    .h(SL.H.ABSOLUTE, feedItemHeight, null);
            if (lastSleekFeedItem != null) {
                sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
            }
            else {
                sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
            }
            sleekCanvas.addSleek(sleekFeedItem);

            lastSleekFeedItem = sleekFeedItem;
        }

    }

    private static final void loadUIscrollYcompleteFeedUIfade(final SleekCanvas sleekCanvas) {

        sleekCanvas.setBackgroundColor(0xffe8e8e8);

        UtilTestSleekUI.addUIframeRate(sleekCanvas);

        int feedItemTopMargin = UtilPx.getPixels(80);
        int feedItemHorizontalMargin = UtilPx.getPixels(80);
        int feedItemHeight = UtilPx.getPixels(400);

        SleekElement sleekFeedItem, lastSleekFeedItem = null;
        String feedItemString;
        for (int i = 1; i <= 24; i++) {

            sleekFeedItem = new SleekElement(
                    SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
            );

            final SleekElement finalFeedItem = sleekFeedItem;
            finalFeedItem.getTouchHandler().setClickAction(
                    new Runnable() {
                        @Override
                        public void run() {
                            finalFeedItem.setSleekAnimView(new SAVfade(
                                    finalFeedItem.getBgPaint().getAlpha(),
                                    0,
                                    500,
                                    finalFeedItem.getBgPaint(),
                                    ISleekDrawView.NO_DRAW
                            ));
                            finalFeedItem.getText().setSleekAnimView(new SAVfade(
                                    finalFeedItem.getText().getTextPaint().getAlpha(),
                                    0,
                                    500,
                                    finalFeedItem.getText().getTextPaint(),
                                    ISleekDrawView.NO_DRAW
                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            finalFeedItem.setSleekAnimView(new SAVfade(
                                    finalFeedItem.getBgPaint().getAlpha(),
                                    255,
                                    300,
                                    finalFeedItem.getBgPaint(),
                                    ISleekDrawView.NO_DRAW
                            ));
                            finalFeedItem.getText().setSleekAnimView(new SAVfade(
                                    finalFeedItem.getText().getTextPaint().getAlpha(),
                                    255,
                                    300,
                                    finalFeedItem.getText().getTextPaint(),
                                    ISleekDrawView.NO_DRAW
                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
//                            finalFeedItem.parentRemove(true);
//                            sleekCanvas.getHandler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    sleekCanvas.addSleek(finalFeedItem);
//                                }
//                            }, 500);
                        }
                    }
            );

            if (i % 4 == 0) {
                feedItemString = FEED_ITEM_STRING_1;
            }
            else if (i % 3 == 0) {
                feedItemString = FEED_ITEM_STRING_2;
            }
            else if (i % 2 == 0) {
                feedItemString = FEED_ITEM_STRING_3;
            }
            else {
                feedItemString = FEED_ITEM_STRING_4;
            }
            sleekFeedItem.setElementString(feedItemString);

            sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_DEBUG));
            sleekFeedItem.getLayout()
                    .x(SL.X.POS_CENTER, 0, null)
                    .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
                    .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
                    .h(SL.H.ABSOLUTE, feedItemHeight, null);
            if (lastSleekFeedItem != null) {
                sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
            }
            else {
                sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
            }
            sleekCanvas.addSleek(sleekFeedItem);

            lastSleekFeedItem = sleekFeedItem;
        }

    }

    private static final void loadUItextElementsWithPadding(final SleekCanvas sleekCanvas) {

        sleekCanvas.setBackgroundColor(0xffe8e8e8);

        UtilTestSleekUI.addUIframeRate(sleekCanvas);

        int feedItemTopMargin = UtilPx.getPixels(80);
        int feedItemHorizontalMargin = UtilPx.getPixels(80);
        int feedItemHeight = UtilPx.getPixels(400);

        SleekElement sleekFeedItem, lastSleekFeedItem = null;
        String feedItemString;
        for (int i = 1; i <= 24; i++) {

            sleekFeedItem = new SleekElement(
                    SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
            );

            final SleekElement finalFeedItem = sleekFeedItem;
            finalFeedItem.getTouchHandler().setClickAction(
                    new Runnable() {
                        long touchTs = 0;
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - touchTs < 2000) {
                                return;
                            }
                            touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 100,
                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 100,
                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 200,
                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 200,
                                    500,
                                    new ISleekDrawView() {
                                        @Override
                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() + 300,
                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() + 300,
                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() - 600,
                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() - 600,
                                                    1000,
                                                    new ISleekDrawView() {
                                                        @Override
                                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 200,
                                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 200,
                                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 400,
                                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 400,
                                                                    500,
                                                                    ISleekDrawView.NO_DRAW
                                                            ));
                                                        }
                                                    }
                                            ));
                                        }
                                    }
                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    }
            );

            if (i % 4 == 0) {
                feedItemString = FEED_ITEM_STRING_1;
            }
            else if (i % 3 == 0) {
                feedItemString = FEED_ITEM_STRING_2;
            }
            else if (i % 2 == 0) {
                feedItemString = FEED_ITEM_STRING_3;
            }
            else {
                feedItemString = FEED_ITEM_STRING_4;
            }
            sleekFeedItem.setElementString(feedItemString);

            sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_PADDING));
            sleekFeedItem.getLayout()
                    .x(SL.X.POS_CENTER, 0, null)
                    .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
                    .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
                    .h(SL.H.ABSOLUTE, feedItemHeight, null);
            if (lastSleekFeedItem != null) {
                sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
            }
            else {
                sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
            }
            sleekCanvas.addSleek(sleekFeedItem);

            lastSleekFeedItem = sleekFeedItem;
        }

    }

    private static final void loadUItextElementsWrappedWithPadding(final SleekCanvas sleekCanvas) {

        sleekCanvas.setBackgroundColor(0xff999999);

        UtilTestSleekUI.addUIframeRate(sleekCanvas);

        final int feedItemTopMargin = UtilPx.getPixels(60);
        final int feedItemHorizontalMargin = UtilPx.getPixels(80);
        int feedItemHeight = UtilPx.getPixels(400);

        SleekElement sleekFeedItem, lastSleekFeedItem = null;
        String feedItemString;
        for (int i = 1; i <= 24; i++) {

            sleekFeedItem = new SleekElement(
                    SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
            );

            final SleekElement finalFeedItem = sleekFeedItem;
            finalFeedItem.getTouchHandler().setClickAction(
                    new Runnable() {
                        long touchTs = 0;
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - touchTs < 1000) {
                                return;
                            }
                            touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

                            final float startX = finalFeedItem.getSleekX();
                            final float startY = finalFeedItem.getSleekY();
                            final int startW = finalFeedItem.getSleekW();
                            final int startH = finalFeedItem.getSleekH();
                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                    startX, startX - feedItemHorizontalMargin,
                                    startY, startY - feedItemTopMargin,
                                    startW, startW + feedItemHorizontalMargin + feedItemHorizontalMargin,
                                    startH, startH + feedItemTopMargin + feedItemTopMargin,
                                    500,
                                    new ISleekDrawView() {
                                        @Override
                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                    finalFeedItem.getSleekX(), startX,
                                                    finalFeedItem.getSleekY(), startY,
                                                    finalFeedItem.getSleekW(), startW,
                                                    finalFeedItem.getSleekH(), startH,
                                                    500,
                                                    ISleekDrawView.NO_DRAW
                                            ));
                                        }
                                    }
                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    }
            );

            if (i % 4 == 0) {
                feedItemString = "Kenny Powers";
                sleekFeedItem.setWrapTextWidth(true);
                sleekFeedItem.setWrapTextHeight(true);
            }
            else if (i % 3 == 0) {
                feedItemString = "Wrap: Width\n" + FEED_ITEM_STRING_2;
                sleekFeedItem.setWrapTextWidth(true);
            }
            else if (i % 2 == 0) {
                feedItemString = "Wrap: Height\n" + FEED_ITEM_STRING_3;
                sleekFeedItem.setWrapTextHeight(true);
            }
            else {
                feedItemString = "Wrap: Width + Height\n" + FEED_ITEM_STRING_4;
                sleekFeedItem.setWrapTextWidth(true);
                sleekFeedItem.setWrapTextHeight(true);
            }
            sleekFeedItem.setElementString(feedItemString);

            sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_PADDING_BOX_SHADOW));
            sleekFeedItem.createText();
            sleekFeedItem.getText().setBackgroundColor(0x990000ff);
            sleekFeedItem.getLayout()
                    .x(SL.X.POS_CENTER, 0, null)
                    .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
                    .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
                    .h(SL.H.ABSOLUTE, feedItemHeight, null);
            if (lastSleekFeedItem != null) {
                sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
            }
            else {
                sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
            }
            sleekCanvas.addSleek(sleekFeedItem);

            lastSleekFeedItem = sleekFeedItem;
        }

    }

    private static final void loadUIelementsWithBackgroundImage(final SleekCanvas sleekCanvas) {

        sleekCanvas.setBackgroundColor(0xffe8e8e8);

        UtilTestSleekUI.addUIframeRate(sleekCanvas);

        int feedItemTopMargin = UtilPx.getPixels(80);
        int feedItemHorizontalMargin = UtilPx.getPixels(80);
        int feedItemHeight = UtilPx.getPixels(400);

        SleekElement sleekFeedItem, lastSleekFeedItem = null;
        String feedItemString;
        for (int i = 1; i <= 24; i++) {

            sleekFeedItem = new SleekElement(
                    SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
            );

            final SleekElement finalFeedItem = sleekFeedItem;
            finalFeedItem.getTouchHandler().setClickAction(
                    new Runnable() {
                        long touchTs = 0;
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - touchTs < 2000) {
                                return;
                            }
                            touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 100,
                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 100,
                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 200,
                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 200,
                                    500,
                                    new ISleekDrawView() {
                                        @Override
                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() + 300,
                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() + 300,
                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() - 600,
                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() - 600,
                                                    1000,
                                                    new ISleekDrawView() {
                                                        @Override
                                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 200,
                                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 200,
                                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 400,
                                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 400,
                                                                    500,
                                                                    ISleekDrawView.NO_DRAW
                                                            ));
                                                        }
                                                    }
                                            ));
                                        }
                                    }
                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    }
            );

            if (i % 4 == 0) {
                feedItemString = FEED_ITEM_STRING_1;
            }
            else if (i % 3 == 0) {
                feedItemString = FEED_ITEM_STRING_2;
            }
            else if (i % 2 == 0) {
                feedItemString = FEED_ITEM_STRING_3;
            }
            else {
                feedItemString = FEED_ITEM_STRING_4;
            }
            sleekFeedItem.setElementString(feedItemString);

            sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_IMAGE));
            sleekFeedItem.getLayout()
                    .x(SL.X.POS_CENTER, 0, null)
                    .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
                    .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
                    .h(SL.H.ABSOLUTE, feedItemHeight, null);
            if (lastSleekFeedItem != null) {
                sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
            }
            else {
                sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
            }
            sleekCanvas.addSleek(sleekFeedItem);

            lastSleekFeedItem = sleekFeedItem;
        }
    }

    private static final void loadUIelementsWithBackgroundImageContainCover(final SleekCanvas sleekCanvas) {

        sleekCanvas.setBackgroundColor(0xffe8e8e8);

        UtilTestSleekUI.addUIframeRate(sleekCanvas);

        int feedItemTopMargin = UtilPx.getPixels(70);
        int feedItemHorizontalMargin = UtilPx.getPixels(120);
        int feedItemHeight = UtilPx.getPixels(400);

        SleekElement sleekFeedItem, lastSleekFeedItem = null;
        String feedItemString;
        for (int i = 1; i <= 24; i++) {

            sleekFeedItem = new SleekElement(
                    SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
            );

            final SleekElement finalFeedItem = sleekFeedItem;
            finalFeedItem.getTouchHandler().setClickAction(
                    new Runnable() {
                        long touchTs = 0;
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - touchTs < 2000) {
                                return;
                            }
                            touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 100,
                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 100,
                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 200,
                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 200,
                                    500,
                                    new ISleekDrawView() {
                                        @Override
                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() + 300,
                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() + 300,
                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() - 600,
                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() - 600,
                                                    1000,
                                                    new ISleekDrawView() {
                                                        @Override
                                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - 200,
                                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - 200,
                                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + 400,
                                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + 400,
                                                                    500,
                                                                    ISleekDrawView.NO_DRAW
                                                            ));
                                                        }
                                                    }
                                            ));
                                        }
                                    }
                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    }
            );

            if (i % 4 == 0) {
                feedItemString = FEED_ITEM_STRING_1;
            }
            else if (i % 3 == 0) {
                feedItemString = FEED_ITEM_STRING_2;
            }
            else if (i % 2 == 0) {
                feedItemString = FEED_ITEM_STRING_3;
            }
            else {
                feedItemString = FEED_ITEM_STRING_4;
            }
            sleekFeedItem.setElementString(feedItemString);

            if (i % 2 == 0) {
                sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER));
            }
            else {
                sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_IMAGE_CONTAIN));
            }

            sleekFeedItem.getLayout()
                    .x(SL.X.POS_CENTER, 0, null)
                    .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
                    .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
                    .h(SL.H.ABSOLUTE, feedItemHeight, null);
            if (lastSleekFeedItem != null) {
                sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
            }
            else {
                sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
            }
            sleekCanvas.addSleek(sleekFeedItem);

            lastSleekFeedItem = sleekFeedItem;
        }
    }

    private static final void loadUIelementsWithBackgroundImageCoverOnly(final SleekCanvas sleekCanvas) {

        sleekCanvas.setBackgroundColor(0xffe8e8e8);

        UtilTestSleekUI.addUIframeRate(sleekCanvas);

        int feedItemTopMargin = UtilPx.getPixels(360);
        final int feedItemWidth = UtilPx.getPixels(360);
        final int feedItemHeight = UtilPx.getPixels(360);

        SleekElement sleekFeedItem, lastSleekFeedItem = null;
        String feedItemString;
        for (int i = 1; i <= 24; i++) {

            sleekFeedItem = new SleekElement(
                    SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
            );

            final SleekElement finalFeedItem = sleekFeedItem;
            finalFeedItem.getTouchHandler().setClickAction(
                    new Runnable() {
                        long touchTs = 0;
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - touchTs < 2000) {
                                return;
                            }
                            touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - (feedItemWidth / 2.0f),
                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY(),
                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + feedItemWidth,
                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH(),
                                    500,
                                    new ISleekDrawView() {
                                        @Override
                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() + (feedItemWidth / 2.0f),
                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY(),
                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() - feedItemWidth,
                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH(),
                                                    500,
                                                    new ISleekDrawView() {
                                                        @Override
                                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX(),
                                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - (feedItemHeight / 2.0f),
                                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW(),
                                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + feedItemHeight,
                                                                    500,
                                                                    new ISleekDrawView() {
                                                                        @Override
                                                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX(),
                                                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() + (feedItemHeight / 2.0f),
                                                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW(),
                                                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() - feedItemHeight,
                                                                                    500,
                                                                                    ISleekDrawView.NO_DRAW
                                                                            ));
                                                                        }
                                                                    }
                                                            ));
                                                        }
                                                    }
                                            ));
                                        }
                                    }
                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    }
            );

            if (i % 4 == 0) {
                feedItemString = FEED_ITEM_STRING_1;
            }
            else if (i % 3 == 0) {
                feedItemString = FEED_ITEM_STRING_2;
            }
            else if (i % 2 == 0) {
                feedItemString = FEED_ITEM_STRING_3;
            }
            else {
                feedItemString = FEED_ITEM_STRING_4;
            }
            sleekFeedItem.setElementString(feedItemString);

            if (i % 2 == 0) {
                sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER));
            }
            else {
                sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER_TWO));
            }

            sleekFeedItem.getLayout()
                    .x(SL.X.POS_CENTER, 0, null)
                    .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
                    .w(SL.W.ABSOLUTE, feedItemWidth, null)
                    .h(SL.H.ABSOLUTE, feedItemHeight, null);
            if (lastSleekFeedItem != null) {
                sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
            }
            else {
                sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
            }
            sleekCanvas.addSleek(sleekFeedItem);

            lastSleekFeedItem = sleekFeedItem;
        }
    }

    private static final void loadUIbackgroundImageResizeElement(final SleekCanvas sleekCanvas) {

        sleekCanvas.setBackgroundColor(0xffe8e8e8);

        UtilTestSleekUI.addUIframeRate(sleekCanvas);

        int feedItemTopMargin = UtilPx.getPixels(200);
        final int feedItemWidth = UtilPx.getPixels(360);
        final int feedItemHeight = UtilPx.getPixels(360);

        SleekElement sleekFeedItem, lastSleekFeedItem = null;
        String feedItemString;
        for (int i = 1; i <= 24; i++) {

            sleekFeedItem = new SleekElement(
                    SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
            );

            final SleekElement finalFeedItem = sleekFeedItem;
            finalFeedItem.getTouchHandler().setClickAction(
                    new Runnable() {
                        long touchTs = 0;
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - touchTs < 2000) {
                                return;
                            }
                            touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - (feedItemWidth / 2.0f),
                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY(),
                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + feedItemWidth,
                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH(),
                                    500,
                                    new ISleekDrawView() {
                                        @Override
                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX() + (feedItemWidth / 2.0f),
                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY(),
                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW() - feedItemWidth,
                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH(),
                                                    500,
                                                    new ISleekDrawView() {
                                                        @Override
                                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX(),
                                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - (feedItemHeight / 2.0f),
                                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW(),
                                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + feedItemHeight,
                                                                    500,
                                                                    new ISleekDrawView() {
                                                                        @Override
                                                                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                                                            finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                                                                    finalFeedItem.getSleekX(), finalFeedItem.getSleekX(),
                                                                                    finalFeedItem.getSleekY(), finalFeedItem.getSleekY() + (feedItemHeight / 2.0f),
                                                                                    finalFeedItem.getSleekW(), finalFeedItem.getSleekW(),
                                                                                    finalFeedItem.getSleekH(), finalFeedItem.getSleekH() - feedItemHeight,
                                                                                    500,
                                                                                    ISleekDrawView.NO_DRAW
                                                                            ));
                                                                        }
                                                                    }
                                                            ));
                                                        }
                                                    }
                                            ));
                                        }
                                    }
                            ));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //DO NOTHING
                        }
                    }
            );

            if (i % 4 == 0) {
                feedItemString = FEED_ITEM_STRING_1;
            }
            else if (i % 3 == 0) {
                feedItemString = FEED_ITEM_STRING_2;
            }
            else if (i % 2 == 0) {
                feedItemString = FEED_ITEM_STRING_3;
            }
            else {
                feedItemString = FEED_ITEM_STRING_4;
            }
            sleekFeedItem.setElementString(feedItemString);

            if (i % 2 == 0) {
                sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER));
            }
            else {
                sleekFeedItem.addCSSblock(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER_TWO));
            }

            sleekFeedItem.getLayout()
                    .x(SL.X.POS_CENTER, 0, null)
                    .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
                    .w(SL.W.ABSOLUTE, feedItemWidth, null)
                    .h(SL.H.ABSOLUTE, feedItemHeight, null);
            if (lastSleekFeedItem != null) {
                sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
            }
            else {
                sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
            }

            sleekFeedItem.createBackgroundImage();
            sleekFeedItem.wrapBackgroundImageSize(false, true, true);

            sleekCanvas.addSleek(sleekFeedItem);

            lastSleekFeedItem = sleekFeedItem;
        }
    }

  private static final void loadUIbackgroundCoverWithBorder(final SleekCanvas sleekCanvas) {

    sleekCanvas.setBackgroundColor(0xffe8e8e8);

    UtilTestSleekUI.addUIframeRate(sleekCanvas);

    int feedItemTopMargin = UtilPx.getPixels(200);
    final int feedItemWidth = UtilPx.getPixels(360);
    final int feedItemHeight = UtilPx.getPixels(360);

    SleekElement sleekFeedItem, lastSleekFeedItem = null;
    String feedItemString;
    for (int i = 1; i <= 24; i++) {

      sleekFeedItem = new SleekElement(
          SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
      );

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(
          new Runnable() {
            long touchTs = 0;
            @Override
            public void run() {
              if (System.currentTimeMillis() - touchTs < 2000) {
                return;
              }
              touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

              finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                  finalFeedItem.getSleekX(), finalFeedItem.getSleekX() - (feedItemWidth / 2.0f),
                  finalFeedItem.getSleekY(), finalFeedItem.getSleekY(),
                  finalFeedItem.getSleekW(), finalFeedItem.getSleekW() + feedItemWidth,
                  finalFeedItem.getSleekH(), finalFeedItem.getSleekH(),
                  500,
                  new ISleekDrawView() {
                    @Override
                    public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                      finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                          finalFeedItem.getSleekX(), finalFeedItem.getSleekX() + (feedItemWidth / 2.0f),
                          finalFeedItem.getSleekY(), finalFeedItem.getSleekY(),
                          finalFeedItem.getSleekW(), finalFeedItem.getSleekW() - feedItemWidth,
                          finalFeedItem.getSleekH(), finalFeedItem.getSleekH(),
                          500,
                          new ISleekDrawView() {
                            @Override
                            public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                              finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                  finalFeedItem.getSleekX(), finalFeedItem.getSleekX(),
                                  finalFeedItem.getSleekY(), finalFeedItem.getSleekY() - (feedItemHeight / 2.0f),
                                  finalFeedItem.getSleekW(), finalFeedItem.getSleekW(),
                                  finalFeedItem.getSleekH(), finalFeedItem.getSleekH() + feedItemHeight,
                                  500,
                                  new ISleekDrawView() {
                                    @Override
                                    public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                                      finalFeedItem.setSleekAnimView(new SAVtransXYWH(
                                          finalFeedItem.getSleekX(), finalFeedItem.getSleekX(),
                                          finalFeedItem.getSleekY(), finalFeedItem.getSleekY() + (feedItemHeight / 2.0f),
                                          finalFeedItem.getSleekW(), finalFeedItem.getSleekW(),
                                          finalFeedItem.getSleekH(), finalFeedItem.getSleekH() - feedItemHeight,
                                          500,
                                          ISleekDrawView.NO_DRAW
                                      ));
                                    }
                                  }
                              ));
                            }
                          }
                      ));
                    }
                  }
              ));
            }
          },
          new Runnable() {
            @Override
            public void run() {
              //DO NOTHING
            }
          },
          new Runnable() {
            @Override
            public void run() {
              //DO NOTHING
            }
          }
      );

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      }
      else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      }
      else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      }
      else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      if (i % 2 == 0) {
        sleekFeedItem.addCSSblock(new CSSblockBase(CSS_BORDER_1));
      }
      else {
        sleekFeedItem.addCSSblock(new CSSblockBase(CSS_BORDER_2));
      }

      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.ABSOLUTE, feedItemWidth, null)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      }
      else {
        sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
      }

      sleekFeedItem.createBackgroundImage();
      sleekFeedItem.wrapBackgroundImageSize(false, true, true);

      sleekCanvas.addSleek(sleekFeedItem);

      lastSleekFeedItem = sleekFeedItem;
    }
  }

  private static final void loadUIcompleteAppUIexample1(final SleekCanvas sleekCanvas) {
      //TODO Build a simple complete App UI to test and demonstrate how easy Sleek is to use.
  }

  @Test
  public void testGeneralUI() throws Exception {

    if (mActivityRule.getActivity() == null) {
        throw new IllegalStateException("mActivityRule.getActivity() == null");
    }

    UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

    UtilTestSleekUI.setSleekActivitySleekCanvasScrollerY(mActivityRule.getActivity());
    //UtilTestSleekUI.setSleekActivitySleekCanvasScrollerXY(mActivityRule.getActivity());

    //loadUIscrollXYareas(mActivityRule.getActivity().getSleekCanvas());
    //loadUIscrollXYbasicSleekElements(mActivityRule.getActivity().getSleekCanvas());
    //loadUIensureRuntimeAddedViewsGetLoaded(mActivityRule.getActivity().getSleekCanvas());
    //loadUIruntimeDelayAddViews(mActivityRule.getActivity().getSleekCanvas());
    //loadUIverticalTextCentering(mActivityRule.getActivity().getSleekCanvas());
    //loadUIscrollYcompleteFeedUItransXYWH(mActivityRule.getActivity().getSleekCanvas());
    //loadUIscrollYcompleteFeedUIfade(mActivityRule.getActivity().getSleekCanvas());
    //loadUItextElementsWithPadding(mActivityRule.getActivity().getSleekCanvas());
    //loadUItextElementsWrappedWithPadding(mActivityRule.getActivity().getSleekCanvas());
    //loadUIelementsWithBackgroundImage(mActivityRule.getActivity().getSleekCanvas());
    //loadUIelementsWithBackgroundImageContainCover(mActivityRule.getActivity().getSleekCanvas());
    //loadUIelementsWithBackgroundImageCoverOnly(mActivityRule.getActivity().getSleekCanvas());
    //loadUIbackgroundImageResizeElement(mActivityRule.getActivity().getSleekCanvas());
    loadUIbackgroundCoverWithBorder(mActivityRule.getActivity().getSleekCanvas());

    final CountDownLatch activityPauseLatch = new CountDownLatch(1);

    mActivityRule.getActivity().setPauseListener(new Runnable() {
        @Override
        public void run() {
            activityPauseLatch.countDown();
        }
    });

    activityPauseLatch.await();
  }

  @Test
  public void testUtilDownload() throws Exception {
    final String imageUrl = "http://www.publicdomainpictures.net/pictures/30000/velka/evening-landscape-13530956185Aw.jpg";
    final String customFileName = "TestSleekUI_image1.jpg";
    final CountDownLatch downloadLatch = new CountDownLatch(4);

    // Attempt download on bg-thread
    UtilDownload.downloadUrl(
        imageUrl,
        customFileName,
        UtilDownload.EXECUTOR,
        new UtilDownload.FileDownload() {
            @Override
            public void downloadProgress(float percent) {

            }
            @Override
            public void downloadFinished(File file) {
                downloadLatch.countDown();
            }
        }
    );

    // Attempt download on bg-thread
    UtilDownload.downloadUrl(
        imageUrl,
        customFileName,
        UtilDownload.EXECUTOR,
        new UtilDownload.FileDownload() {
            @Override
            public void downloadProgress(float percent) {

            }
            @Override
            public void downloadFinished(File file) {
                downloadLatch.countDown();
            }
        }
    );

    // Attempt download on bg-thread
    UtilDownload.downloadUrl(
        imageUrl,
        customFileName,
        UtilDownload.EXECUTOR,
        new UtilDownload.FileDownload() {
            @Override
            public void downloadProgress(float percent) {

            }
            @Override
            public void downloadFinished(File file) {
                downloadLatch.countDown();
            }
        }
    );

    // Attempt download and wait 15 sec for reference to existing downloaded File.
    final File imageFile = UtilDownload.downloadUrl(
        imageUrl,
        customFileName,
        15000,
        new UtilDownload.FileDownload() {
            @Override
            public void downloadProgress(float percent) {

            }
            @Override
            public void downloadFinished(File file) {
                downloadLatch.countDown();
            }
        }
    );

    downloadLatch.await(20000, TimeUnit.MILLISECONDS);
    assertNotNull(imageFile);
    assertTrue(imageFile.getAbsolutePath().contains(customFileName));
    assertTrue(imageFile.delete());
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
  public void testCSSfeedItemPadding() {

    UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

    CSSblockBase cssBlock;
    //padding: 5px 10px 15px 20px;
    cssBlock = new CSSblockBase(CSS_PADDING_X4);
    assertEquals(UtilPx.getPixels(5), cssBlock.getPadding().top);
    assertEquals(UtilPx.getPixels(10), cssBlock.getPadding().right);
    assertEquals(UtilPx.getPixels(15), cssBlock.getPadding().bottom);
    assertEquals(UtilPx.getPixels(20), cssBlock.getPadding().left);

    //padding: 10px 5px 20px;
    cssBlock = new CSSblockBase(CSS_PADDING_X3);
    assertEquals(UtilPx.getPixels(10), cssBlock.getPadding().top);
    assertEquals(UtilPx.getPixels(5), cssBlock.getPadding().right);
    assertEquals(UtilPx.getPixels(20), cssBlock.getPadding().bottom);
    assertEquals(UtilPx.getPixels(5), cssBlock.getPadding().left);

    //padding: 10px 20px;
    cssBlock = new CSSblockBase(CSS_PADDING_X2);
    assertEquals(UtilPx.getPixels(10), cssBlock.getPadding().top);
    assertEquals(UtilPx.getPixels(20), cssBlock.getPadding().right);
    assertEquals(UtilPx.getPixels(10), cssBlock.getPadding().bottom);
    assertEquals(UtilPx.getPixels(20), cssBlock.getPadding().left);

    //padding: 10px;
    cssBlock = new CSSblockBase(CSS_PADDING_X1);
    assertEquals(UtilPx.getPixels(10), cssBlock.getPadding().top);
    assertEquals(UtilPx.getPixels(10), cssBlock.getPadding().right);
    assertEquals(UtilPx.getPixels(10), cssBlock.getPadding().bottom);
    assertEquals(UtilPx.getPixels(10), cssBlock.getPadding().left);
  }

  @Test
  public void testCSSbackgroundImageUrl() {

    UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

    CSSblockBase cssBlock = new CSSblockBase(CSS_FEED_ITEM_IMAGE);
    assertEquals(
            "https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg",
            cssBlock.getBackgroundImage()
    );
  }

  @Test
  public void testCSSbackgroundImageLocal() {

    UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

    CSSblockBase cssBlock = new CSSblockBase(CSS_FEED_ITEM_IMAGE_LOCAL);
    assertEquals(
            "sym_def_app_icon",
            cssBlock.getBackgroundImage()
    );

    int resId = UtilPx.getDefaultContext().getResources().getIdentifier(
            cssBlock.getBackgroundImage(),
            "drawable",
            "android"
    );
    assertTrue(resId > 0);
  }

}
