package com.ztory.lib.sleek;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ztory.lib.sleek.base.element.css.CSSblock;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

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
                           "    background: #38B0DE;\n" +
                           "    color: #666;\n" +
                           "    border-radius: 22px;\n" +
                           "    margin: 0 0 8px 0;\n" +
                           "    font-size: 10px;\n" +
                           "    width: 46px;\n" +
                           "    height: 46px;\n" +
                           "    line-height: 46px;\n" +
                           "    text-align: center;\n" +
                           "}",
            CSS_STRING_2 = "{\n" +
                           "    background: #33E776;\n" +
                           "    color: #666;\n" +
                           "    border-radius: 22px;\n" +
                           "    margin: 0 0 8px 0;\n" +
                           "    font-size: 10px;\n" +
                           "    width: 46px;\n" +
                           "    height: 46px;\n" +
                           "    line-height: 46px;\n" +
                           "    text-align: center;\n" +
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
        UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas, new CSSblock(CSS_STRING_1), 0.1f, 0.1f);
        UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas, new CSSblock(CSS_STRING_2), 0.5f, 0.1f);
        UtilTestSleekUI.reloadUI(sleekCanvas);
    }

    @Test
    public void testGeneralUI() throws Exception {

        if (mActivityRule.getActivity() == null) {
            throw new IllegalStateException("mActivityRule.getActivity() == null");
        }

        //loadUIscrollXYareas(mActivityRule.getActivity().getSleekCanvas());
        loadUIscrollXYbasicSleekElements(mActivityRule.getActivity().getSleekCanvas());

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
