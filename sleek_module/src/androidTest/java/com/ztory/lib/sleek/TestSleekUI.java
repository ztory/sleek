package com.ztory.lib.sleek;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestSleekUI {

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
        sleekCanvas.reloadScrollEdges();
    }

    @Test
    public void testGeneralUI() throws Exception {

        if (mActivityRule.getActivity() == null) {
            throw new IllegalStateException("mActivityRule.getActivity() == null");
        }

        loadUIscrollXYareas(mActivityRule.getActivity().getSleekCanvas());

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
