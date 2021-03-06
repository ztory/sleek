package com.ztory.lib.sleek;

import static com.ztory.lib.sleek.base.SleekParam.FIXED_TOUCHABLE;
import static com.ztory.lib.sleek.base.SleekParam.TOUCHABLE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.ztory.lib.sleek.animation.SAVfade;
import com.ztory.lib.sleek.animation.SAVtransXYWH;
import com.ztory.lib.sleek.assumption.Assumeable;
import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.base.SleekColorArea;
import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.base.element.SleekCSSanim;
import com.ztory.lib.sleek.base.element.SleekElement;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.layout.SL;
import com.ztory.lib.sleek.layout.SL.H;
import com.ztory.lib.sleek.layout.SL.W;
import com.ztory.lib.sleek.layout.SL.X;
import com.ztory.lib.sleek.layout.SL.Y;
import com.ztory.lib.sleek.mapd.Mapd;
import com.ztory.lib.sleek.util.Calc;
import com.ztory.lib.sleek.util.UtilDownload;
import com.ztory.lib.sleek.util.UtilExecutor;
import com.ztory.lib.sleek.util.UtilPx;
import com.ztory.lib.sleek.util.UtilSleekLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

  private static final String FEED_ITEM_STRING_1 =
      "Now that there is the Tec-9, a crappy spray gun from South Miami. This gun is advertised as the most popular gun in American crime. Do you believe that shit? It actually says that in the little book that comes with it: the most popular gun in American crime. Like they're actually proud of that shit.",
      FEED_ITEM_STRING_2 =
          "Well, the way they make shows is, they make one show. That show's called a pilot. Then they show that show to the people who make shows, and on the strength of that one show they decide if they're going to make more shows. Some pilots get picked and become television programs. Some don't, become nothing. She starred in one of the ones that became nothing.",
      FEED_ITEM_STRING_3 =
          "Look, just because I don't be givin' no man a foot massage don't make it right for Marsellus to throw Antwone into a glass motherfuckin' house, fuckin' up the way the nigger talks. Motherfucker do that shit to me, he better paralyze my ass, 'cause I'll kill the motherfucker, know what I'm sayin'?",
      FEED_ITEM_STRING_4 =
          "Do you see any Teletubbies in here? Do you see a slender plastic tag clipped to my shirt with my name printed on it? Do you see a little Asian child with a blank expression on his face sitting outside on a mechanical helicopter that shakes when you put quarters in it? No? Well, that's what you see at a toy store. And you must think you're in a toy store, because you're here shopping for an infant named Jeb.";

  private static final String CSS_STRING_1 = "{\n"
      + "    background: #999;\n"
      + "    border-radius: 22px;\n"
      + "    box-shadow: 1px 2px 4px rgba(255, 0, 0, 1.0);\n"
      + "}", CSS_STRING_2 = "{\n"
      + "    background: #33E77699;\n"
      + "    border-radius: 8px;\n"
      + "    color: #4860E3;\n"
      + "    font-size: 16px;\n"
      + "    line-height: 30px;\n"
      + "    text-align: center;\n"
      + "    vertical-align: bottom;\n"
      + "}", CSS_STRING_3 = "{\n"
      + "    background: purple;\n"
      + "    border-radius: 8px;\n"
      + "    color: #38B0DE;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 30px;\n"
      + "    text-align: center;\n"
      + "    vertical-align: middle;\n"
      + "    box-shadow: 1px 2px 4px #38B0DE;\n"
      + "}", CSS_STRING_4 = "{\n"
      + "    background: rgba(255, 0, 0, 1.0);\n"
      + "    border-radius: 2px;\n"
      + "    color: rgba(0, 0, 255, 0.7);\n"
      + "    font-size: 20px;\n"
      + "    line-height: 30px;\n"
      + "    text-align: center;\n"
      + "    vertical-align: top;\n"
      + "    box-shadow: 0px 0px 12px rgba(0, 255, 0, 0.5);\n"
      + "}", CSS_STRING_5 = "{\n"
      + "    background: #FF5B38;\n"
      + "    border-radius: 2px;\n"
      + "    color: #38B0DE;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 30px;\n"
      + "    text-align: center;\n"
      + "    vertical-align: top;\n"
      + "    box-shadow: 0px 1px 2px #FFA03899;\n"
      + "}", CSS_STRING_6 = "{\n"
      + "    background: #FF5B38;\n"
      + "    border-radius: 2px;\n"
      + "    color: #38B0DE;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 30px;\n"
      + "    text-align: center;\n"
      + "    vertical-align: middle;\n"
      + "    box-shadow: 0px 1px 2px #FFA03899;\n"
      + "}", CSS_STRING_7 = "{\n"
      + "    background: #FF5B38;\n"
      + "    border-radius: 2px;\n"
      + "    color: #38B0DE;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 30px;\n"
      + "    text-align: center;\n"
      + "    vertical-align: bottom;\n"
      + "    box-shadow: 0px 1px 2px #FFA03899;\n"
      //offset-x | offset-y | blur-radius | color
      + "}", CSS_FEED_ITEM_DEBUG = "{\n"
      + "    background: #fdfdfd;\n"
      + "    border-radius: 3px;\n"
      + "    color: #121212;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 24px;\n"
      + "    text-align: left;\n"
      + "    vertical-align: top;\n"
      + "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n"
      + "}", CSS_PADDING_X4 =
      "{\n" + "    padding: 5px 10px 15px 20px;\n"// top | right | bottom | left
          + "}", CSS_PADDING_X3 =
      "{\n" + "    padding: 10px 5px 20px;\n" + "}",// top | left+right | bottom
      CSS_PADDING_X2 = "{\n" + "    padding: 10px 20px;\n" + "}",// top+bottom | left+right
      CSS_PADDING_X1 = "{\n" + "    padding: 10px;\n" + "}",// top+right+bottom+left
      CSS_FEED_ITEM_PADDING = "{\n"
          + "    background: #fdfdfd;\n"
          + "    border-radius: 2px;\n"
          + "    color: #121212;\n"
          + "    font-size: 20px;\n"
          + "    line-height: 24px;\n"
          + "    text-align: left;\n"
          + "    vertical-align: top;\n"
          + "    padding: 5px 10px 15px 20px;\n"
          + "}", CSS_FEED_ITEM_PADDING_BOX_SHADOW = "{\n"
      + "    background: #fdfdfd;\n"
      + "    border-radius: 2px;\n"
      + "    color: #121212;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 24px;\n"
      + "    text-align: left;\n"
      + "    vertical-align: bottom;\n"
      + "    padding: 20px 10px;\n"
      + "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n"
      + "}", CSS_FEED_ITEM_IMAGE = "{\n"
      + "    background: #33E776;\n"
      + "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n"
      + "    border-radius: 8px;\n"
      + "    color: #121212;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 24px;\n"
      + "    text-align: left;\n"
      + "    vertical-align: top;\n"
      + "    padding: 12px;\n"
      + "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n"
      + "}", CSS_FEED_ITEM_IMAGE_LOCAL = "{\n"
      + "    background: #33E776;\n"
      + "    background-image: url(\"sym_def_app_icon\");\n"
      + "    border-radius: 8px;\n"
      + "    color: #121212;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 24px;\n"
      + "    text-align: left;\n"
      + "    vertical-align: top;\n"
      + "    padding: 12px;\n"
      + "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n"
      + "}", CSS_FEED_ITEM_IMAGE_CONTAIN = "{\n"
      + "    background: #33E776;\n"
      + "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/3/39/Beach_Days.jpg\");\n"
      + "    background-size: contain;\n"
      + "    border-radius: 8px;\n"
      + "    color: #121212;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 24px;\n"
      + "    text-align: left;\n"
      + "    vertical-align: top;\n"
      + "    padding: 12px;\n"
      + "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n"
      + "}", CSS_FEED_ITEM_IMAGE_COVER = "{\n"
      + "    background: #33E776;\n"
      + "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/3/39/Beach_Days.jpg\");\n"
      + "    background-size: cover;\n"
      + "    border-radius: 8px;\n"
      + "    color: #121212;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 24px;\n"
      + "    text-align: left;\n"
      + "    vertical-align: top;\n"
      + "    padding: 12px;\n"
      + "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n"
      + "}", CSS_FEED_ITEM_IMAGE_COVER_TWO = "{\n"
      + "    background: #33E776;\n"
      + "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n"
      + "    background-size: cover;\n"
      + "    border-radius: 8px;\n"
      + "    color: #121212;\n"
      + "    font-size: 20px;\n"
      + "    line-height: 24px;\n"
      + "    text-align: left;\n"
      + "    vertical-align: top;\n"
      + "    padding: 12px;\n"
      + "    box-shadow: 0px 1px 2px rgba(0,0,0,0.2);\n"
      + "}",
      CSS_BORDER_1 = "{\n"
          + "    background: #33E776;\n"
          + "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/4/4e/McCarthyBeachStatePark.jpg\");\n"
          + "    background-size: contain;\n"
          + "    border-radius: 20px;\n"
          + "    color: #993333;\n"
          + "    font-size: 20px;\n"
          + "    line-height: 24px;\n"
          + "    text-align: left;\n"
          + "    vertical-align: top;\n"
          + "    padding: 12px;\n"
          + "    box-shadow: 0px 0px 8px rgba(0,0,255,0.6);\n"
          + "    border: 4px solid #ffff00;\n"
          + "}",
      CSS_BORDER_2 = "{\n"
          + "    background: #33E776;\n"
          + "    background-size: cover;\n"
          + "    border-radius: 8px;\n"
          + "    color: #333399;\n"
          + "    font-size: 20px;\n"
          + "    line-height: 24px;\n"
          + "    text-align: left;\n"
          + "    vertical-align: top;\n"
          + "    padding: 12px;\n"
          + "    box-shadow: 0px 0px 24px rgba(132,91,185,0.6);\n"
          + "    border: 2px solid #ff0000;\n"
          + "}",
      CSS_BORDER_3 = "{\n"
          + "    background: #33E776;\n"
          + "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n"
          + "    background-size: cover;\n"
          + "    border-radius: 8px;\n"
          + "    color: #f8f8f8;\n"
          + "    font-size: 20px;\n"
          + "    line-height: 24px;\n"
          + "    text-align: left;\n"
          + "    vertical-align: top;\n"
          + "    padding: 12px;\n"
          + "    box-shadow: 10px 10px 20px rgba(255,0,0,0.9);\n"
          + "    border: 6px solid #fff;\n"
          + "}",
      CSS_BORDER_4 = "{\n"
          + "    background: #38B0DE;\n"
          //+ "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n"
          //+ "    background-size: cover;\n"
          + "    border-radius: 4px;\n"
          + "    color: #f8f8f8;\n"
          + "    font-size: 20px;\n"
          + "    line-height: 24px;\n"
          + "    text-align: left;\n"
          + "    vertical-align: top;\n"
          + "    padding: 8px;\n"
          + "    box-shadow: 0px 1px 2px rgba(0,0,0,0.3);\n"
          + "    border: 30px solid #FFC638;\n"
          + "    text-shadow: 1px 1px 2px black;\n"
          + "}",
      CSS_BORDER_5 = "{\n"
          + "    background: #33E776;\n"
          + "    background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n"
          + "    background-size: cover;\n"
          + "    border-radius: 8px;\n"
          + "    color: #ff9999;\n"
          + "    font-size: 20px;\n"
          + "    line-height: 24px;\n"
          + "    text-align: left;\n"
          + "    vertical-align: top;\n"
          + "    padding: 12px;\n"
          + "    box-shadow: -10px -10px 20px rgba(255,0,0,0.9);\n"
          + "    border: 6px solid #fff;\n"
          + "}",
      CSS_SHADOW_OFFSET_X_POSITIVE = "{"
          + "background: #33E776;"
//          + "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n"
          + "background-size: cover;"
          + "border-radius: 8px;"
          + "color: #f8f8f8;"
          + "font-size: 20px;"
          + "line-height: 24px;"
          + "text-align: left;"
          + "vertical-align: top;"
          + "padding: 12px;"
          + "box-shadow: 40px 20px 20px rgba(255,0,0,0.9);"
          + "border: 6px solid #fff;"
          + "}",
      CSS_SHADOW_OFFSET_X_NEGATIVE = "{"
          + "background: #33E776;"
//          + "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/e/e5/Beach_View_of_the_Saint_Martin%27s_Island.jpg\");\n"
          + "background-size: cover;"
          + "border-radius: 8px;"
          + "color: #f8f8f8;"
          + "font-size: 20px;"
          + "line-height: 24px;"
          + "text-align: left;"
          + "vertical-align: top;"
          + "padding: 12px;"
          + "box-shadow: -40px 20px 20px rgba(255,0,0,0.9);"
          + "border: 6px solid #fff;"
          + "}",
      CSS_TEST_1 = "{" +
          "background: #33E776;" +
          "border-radius: 8px;" +
          "border: 6px solid #fff;" +
          "box-shadow: -10px -10px 20px rgba(255,0,0,0.9);" +
          "background-size: cover;" +
          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Wallpaper-5790.jpg/1024px-Wallpaper-5790.jpg\");" +
          "}",
      CSS_TEST_2 = "{" +
          "background: #33E776;" +
          "border-radius: 8px;" +
          "border: 6px solid #fff;" +
//          "box-shadow: -10px -10px 20px rgba(255,0,0,0.9);" +
          "background-size: cover;" +
          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Cerro_Fitzroy_-_Sunrise2.jpg/768px-Cerro_Fitzroy_-_Sunrise2.jpg\");" +
          "}",
      CSS_TEST_3 = "{" +
          "background: #33E776;" +
          "border-radius: 8px;" +
//          "border: 6px solid #fff;" +
          "box-shadow: -10px -10px 20px rgba(255,0,0,0.9);" +
          "background-size: cover;" +
          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Wallpaper-5790.jpg/1024px-Wallpaper-5790.jpg\");" +
          "}",
      CSS_TEST_4 = "{" +
          "background: #33E776;" +
          "border-radius: 8px;" +
//          "border: 6px solid #fff;" +
//          "box-shadow: -10px -10px 20px rgba(255,0,0,0.9);" +
          "background-size: cover;" +
          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Wallpaper-5790.jpg/1024px-Wallpaper-5790.jpg\");" +
          "}",
      CSS_TEST_5 = "{" +
          "background: #33E776;" +
          "border-radius: 8px;" +
          "border: 6px solid #fff;" +
          "box-shadow: -10px -10px 20px rgba(255,0,0,0.9);" +
//          "background-size: cover;" +
//          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Wallpaper-5790.jpg/1024px-Wallpaper-5790.jpg\");" +
          "}",
      CSS_TEST_6 = "{" +
          "background: #33E776;" +
          "border-radius: 8px;" +
          "border: 6px solid #fff;" +
//          "box-shadow: -10px -10px 20px rgba(255,0,0,0.9);" +
//          "background-size: cover;" +
//          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Cerro_Fitzroy_-_Sunrise2.jpg/768px-Cerro_Fitzroy_-_Sunrise2.jpg\");" +
          "}",
      CSS_TEST_7 = "{" +
          "background: #33E776;" +
          "border-radius: 8px;" +
//          "border: 6px solid #fff;" +
          "box-shadow: -10px -10px 20px rgba(255,0,0,0.9);" +
//          "background-size: cover;" +
//          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Wallpaper-5790.jpg/1024px-Wallpaper-5790.jpg\");" +
          "}",
      CSS_TEST_8 = "{" +
          "background: #33E776;" +
          "border-radius: 8px;" +
//          "border: 6px solid #fff;" +
//          "box-shadow: -10px -10px 20px rgba(255,0,0,0.9);" +
//          "background-size: cover;" +
//          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Wallpaper-5790.jpg/1024px-Wallpaper-5790.jpg\");" +
          "}";

  @Rule
  public ActivityTestRule<SleekActivity> mActivityRule =
      new ActivityTestRule<>(SleekActivity.class, true, true);

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
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        null,
        new CSSblockBase(CSS_STRING_1),
        0.1f,
        0.1f,
        160,
        160
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Hej!\nDetta är en ny rad.\nOch en till rad?!\n+1\n+2",
        new CSSblockBase(CSS_STRING_2),
        0.8f,
        0.1f,
        600,
        UtilPx.getPixels(sleekCanvas.getContext(), 150)
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Uno\nDos\nTres\nQuattro\nCinco",
        new CSSblockBase(CSS_STRING_3),
        0.15f,
        0.4f,
        600,
        UtilPx.getPixels(sleekCanvas.getContext(), 150)
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Boom Box",
        new CSSblockBase(CSS_STRING_4),
        0.15f,
        0.25f,
        UtilPx.getPixels(sleekCanvas.getContext(), 80),
        UtilPx.getPixels(sleekCanvas.getContext(), 80)
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
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
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Offscreen #1\nHej!\nDetta är en text!",
        new CSSblockBase(CSS_STRING_2),
        0.95f,
        0.1f,
        600,
        UtilPx.getPixels(sleekCanvas.getContext(), 150)
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Offscreen #2\nHola muchacho hallå vill du LATTJO?!",
        new CSSblockBase(CSS_STRING_2),
        0.2f,
        0.95f,
        600,
        UtilPx.getPixels(sleekCanvas.getContext(), 150)
    );
    SleekElement sleekElement =
        new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(SleekCanvas.STICKY_TOUCH_PRIO
            + 10));
    sleekElement.setElementString("Corner Box");
    sleekElement.addCSS(new CSSblockBase(CSS_STRING_2));
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
        UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
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

    final SleekElement sleekElementAddRemove =
        new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(SleekCanvas.STICKY_TOUCH_PRIO
            + 10));
    sleekElementAddRemove.setElementString("Add Remove Box");
    sleekElementAddRemove.addCSS(new CSSblockBase(CSS_STRING_3));
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
          UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
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

    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Detta är ett ÅÄÖ test komplett med åäö och yjq!",
        new CSSblockBase(CSS_STRING_5),
        0.1f,
        0.1f,
        boxWidth,
        boxHeight
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Detta är ett ÅÄÖ test komplett med åäö och yjq!",
        new CSSblockBase(CSS_STRING_6),
        0.1f,
        0.35f,
        boxWidth,
        boxHeight
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Detta är ett ÅÄÖ test komplett med åäö och yjq!",
        new CSSblockBase(CSS_STRING_7),
        0.1f,
        0.6f,
        boxWidth,
        boxHeight
    );

    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Lynx ÅÄÖ yjq Labs",
        new CSSblockBase(CSS_STRING_5),
        0.5f,
        0.1f,
        boxWidth,
        UtilPx.getPixels(sleekCanvas.getContext(), 40)
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
        "Lynx ÅÄÖ yjq Labs",
        new CSSblockBase(CSS_STRING_6),
        0.5f,
        0.35f,
        boxWidth,
        UtilPx.getPixels(sleekCanvas.getContext(), 40)
    );
    UtilTestSleekUI.addUIbasicSleekElement(sleekCanvas,
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

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
        long touchTs = 0;

        @Override
        public void run() {
          if (System.currentTimeMillis() - touchTs < 2000) {
            return;
          }
          touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
              finalFeedItem.getSleekX() - 100,
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekY() - 100,
              finalFeedItem.getSleekW(),
              finalFeedItem.getSleekW() + 200,
              finalFeedItem.getSleekH(),
              finalFeedItem.getSleekH() + 200,
              500,
              new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                  finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                      finalFeedItem.getSleekX() + 300,
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekY() + 300,
                      finalFeedItem.getSleekW(),
                      finalFeedItem.getSleekW() - 600,
                      finalFeedItem.getSleekH(),
                      finalFeedItem.getSleekH() - 600,
                      1000,
                      new ISleekDrawView() {
                        @Override
                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekX() - 200,
                              finalFeedItem.getSleekY(),
                              finalFeedItem.getSleekY() - 200,
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekW() + 400,
                              finalFeedItem.getSleekH(),
                              finalFeedItem.getSleekH() + 400,
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
      }, new Runnable() {
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
      }, new Runnable() {
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
      });

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_DEBUG));
      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
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

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
        @Override
        public void run() {
          finalFeedItem.setSleekAnimView(new SAVfade(finalFeedItem.getBgPaint().getAlpha(),
              0,
              500,
              finalFeedItem.getBgPaint(),
              ISleekDrawView.NO_DRAW
          ));
          finalFeedItem.getText()
              .setSleekAnimView(new SAVfade(finalFeedItem.getText().getTextPaint().getAlpha(),
                  0,
                  500,
                  finalFeedItem.getText().getTextPaint(),
                  ISleekDrawView.NO_DRAW
              ));
        }
      }, new Runnable() {
        @Override
        public void run() {
          finalFeedItem.setSleekAnimView(new SAVfade(finalFeedItem.getBgPaint().getAlpha(),
              255,
              300,
              finalFeedItem.getBgPaint(),
              ISleekDrawView.NO_DRAW
          ));
          finalFeedItem.getText()
              .setSleekAnimView(new SAVfade(finalFeedItem.getText().getTextPaint().getAlpha(),
                  255,
                  300,
                  finalFeedItem.getText().getTextPaint(),
                  ISleekDrawView.NO_DRAW
              ));
        }
      }, new Runnable() {
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
      });

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_DEBUG));
      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
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

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
        long touchTs = 0;

        @Override
        public void run() {
          if (System.currentTimeMillis() - touchTs < 2000) {
            return;
          }
          touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
              finalFeedItem.getSleekX() - 100,
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekY() - 100,
              finalFeedItem.getSleekW(),
              finalFeedItem.getSleekW() + 200,
              finalFeedItem.getSleekH(),
              finalFeedItem.getSleekH() + 200,
              500,
              new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                  finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                      finalFeedItem.getSleekX() + 300,
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekY() + 300,
                      finalFeedItem.getSleekW(),
                      finalFeedItem.getSleekW() - 600,
                      finalFeedItem.getSleekH(),
                      finalFeedItem.getSleekH() - 600,
                      1000,
                      new ISleekDrawView() {
                        @Override
                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekX() - 200,
                              finalFeedItem.getSleekY(),
                              finalFeedItem.getSleekY() - 200,
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekW() + 400,
                              finalFeedItem.getSleekH(),
                              finalFeedItem.getSleekH() + 400,
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
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      });

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_PADDING));
      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
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

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
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
          finalFeedItem.setSleekAnimView(new SAVtransXYWH(startX,
              startX - feedItemHorizontalMargin,
              startY,
              startY - feedItemTopMargin,
              startW,
              startW + feedItemHorizontalMargin + feedItemHorizontalMargin,
              startH,
              startH + feedItemTopMargin + feedItemTopMargin,
              500,
              new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                  finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                      startX,
                      finalFeedItem.getSleekY(),
                      startY,
                      finalFeedItem.getSleekW(),
                      startW,
                      finalFeedItem.getSleekH(),
                      startH,
                      500,
                      ISleekDrawView.NO_DRAW
                  ));
                }
              }
          ));
        }
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      });

      if (i % 4 == 0) {
        feedItemString = "Kenny Powers";
        sleekFeedItem.setWrapTextWidth(true);
        sleekFeedItem.setWrapTextHeight(true);
      } else if (i % 3 == 0) {
        feedItemString = "Wrap: Width\n" + FEED_ITEM_STRING_2;
        sleekFeedItem.setWrapTextWidth(true);
      } else if (i % 2 == 0) {
        feedItemString = "Wrap: Height\n" + FEED_ITEM_STRING_3;
        sleekFeedItem.setWrapTextHeight(true);
      } else {
        feedItemString = "Wrap: Width + Height\n" + FEED_ITEM_STRING_4;
        sleekFeedItem.setWrapTextWidth(true);
        sleekFeedItem.setWrapTextHeight(true);
      }
      sleekFeedItem.setElementString(feedItemString);

      sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_PADDING_BOX_SHADOW));
      sleekFeedItem.createText();
      sleekFeedItem.getText().setBackgroundColor(0x990000ff);
      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
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

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
        long touchTs = 0;

        @Override
        public void run() {
          if (System.currentTimeMillis() - touchTs < 2000) {
            return;
          }
          touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
              finalFeedItem.getSleekX() - 100,
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekY() - 100,
              finalFeedItem.getSleekW(),
              finalFeedItem.getSleekW() + 200,
              finalFeedItem.getSleekH(),
              finalFeedItem.getSleekH() + 200,
              500,
              new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                  finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                      finalFeedItem.getSleekX() + 300,
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekY() + 300,
                      finalFeedItem.getSleekW(),
                      finalFeedItem.getSleekW() - 600,
                      finalFeedItem.getSleekH(),
                      finalFeedItem.getSleekH() - 600,
                      1000,
                      new ISleekDrawView() {
                        @Override
                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekX() - 200,
                              finalFeedItem.getSleekY(),
                              finalFeedItem.getSleekY() - 200,
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekW() + 400,
                              finalFeedItem.getSleekH(),
                              finalFeedItem.getSleekH() + 400,
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
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      });

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_IMAGE));
      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
        sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
      }
      sleekCanvas.addSleek(sleekFeedItem);

      lastSleekFeedItem = sleekFeedItem;
    }
  }

  private static final void loadUIelementsWithBackgroundImageContainCover(
      final SleekCanvas sleekCanvas
  ) {

    sleekCanvas.setBackgroundColor(0xffe8e8e8);

    UtilTestSleekUI.addUIframeRate(sleekCanvas);

    int feedItemTopMargin = UtilPx.getPixels(70);
    int feedItemHorizontalMargin = UtilPx.getPixels(120);
    int feedItemHeight = UtilPx.getPixels(400);

    SleekElement sleekFeedItem, lastSleekFeedItem = null;
    String feedItemString;
    for (int i = 1; i <= 24; i++) {

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
        long touchTs = 0;

        @Override
        public void run() {
          if (System.currentTimeMillis() - touchTs < 2000) {
            return;
          }
          touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
              finalFeedItem.getSleekX() - 100,
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekY() - 100,
              finalFeedItem.getSleekW(),
              finalFeedItem.getSleekW() + 200,
              finalFeedItem.getSleekH(),
              finalFeedItem.getSleekH() + 200,
              500,
              new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                  finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                      finalFeedItem.getSleekX() + 300,
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekY() + 300,
                      finalFeedItem.getSleekW(),
                      finalFeedItem.getSleekW() - 600,
                      finalFeedItem.getSleekH(),
                      finalFeedItem.getSleekH() - 600,
                      1000,
                      new ISleekDrawView() {
                        @Override
                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekX() - 200,
                              finalFeedItem.getSleekY(),
                              finalFeedItem.getSleekY() - 200,
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekW() + 400,
                              finalFeedItem.getSleekH(),
                              finalFeedItem.getSleekH() + 400,
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
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      });

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      if (i % 2 == 0) {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER));
      } else {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_IMAGE_CONTAIN));
      }

      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.PERCENT_CANVAS, feedItemHorizontalMargin + feedItemHorizontalMargin, null, 1.0f)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
        sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
      }
      sleekCanvas.addSleek(sleekFeedItem);

      lastSleekFeedItem = sleekFeedItem;
    }
  }

  private static final void loadUIelementsWithBackgroundImageCoverOnly(
      final SleekCanvas sleekCanvas
  ) {

    sleekCanvas.setBackgroundColor(0xffe8e8e8);

    UtilTestSleekUI.addUIframeRate(sleekCanvas);

    int feedItemTopMargin = UtilPx.getPixels(360);
    final int feedItemWidth = UtilPx.getPixels(360);
    final int feedItemHeight = UtilPx.getPixels(360);

    SleekElement sleekFeedItem, lastSleekFeedItem = null;
    String feedItemString;
    for (int i = 1; i <= 24; i++) {

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
        long touchTs = 0;

        @Override
        public void run() {
          if (System.currentTimeMillis() - touchTs < 2000) {
            return;
          }
          touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
              finalFeedItem.getSleekX() - (feedItemWidth / 2.0f),
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekW(),
              finalFeedItem.getSleekW() + feedItemWidth,
              finalFeedItem.getSleekH(),
              finalFeedItem.getSleekH(),
              500,
              new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                  finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                      finalFeedItem.getSleekX() + (feedItemWidth / 2.0f),
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekW(),
                      finalFeedItem.getSleekW() - feedItemWidth,
                      finalFeedItem.getSleekH(),
                      finalFeedItem.getSleekH(),
                      500,
                      new ISleekDrawView() {
                        @Override
                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekY(),
                              finalFeedItem.getSleekY() - (feedItemHeight / 2.0f),
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekH(),
                              finalFeedItem.getSleekH() + feedItemHeight,
                              500,
                              new ISleekDrawView() {
                                @Override
                                public void drawView(
                                    Sleek sleek,
                                    Canvas canvas,
                                    SleekCanvasInfo info
                                ) {
                                  finalFeedItem
                                      .setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                                          finalFeedItem.getSleekX(),
                                          finalFeedItem.getSleekY(),
                                          finalFeedItem.getSleekY() + (feedItemHeight / 2.0f),
                                          finalFeedItem.getSleekW(),
                                          finalFeedItem.getSleekW(),
                                          finalFeedItem.getSleekH(),
                                          finalFeedItem.getSleekH() - feedItemHeight,
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
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      });

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      if (i % 2 == 0) {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER));
      } else {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER_TWO));
      }

      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.ABSOLUTE, feedItemWidth, null)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
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

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
        long touchTs = 0;

        @Override
        public void run() {
          if (System.currentTimeMillis() - touchTs < 2000) {
            return;
          }
          touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
              finalFeedItem.getSleekX() - (feedItemWidth / 2.0f),
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekW(),
              finalFeedItem.getSleekW() + feedItemWidth,
              finalFeedItem.getSleekH(),
              finalFeedItem.getSleekH(),
              500,
              new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                  finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                      finalFeedItem.getSleekX() + (feedItemWidth / 2.0f),
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekW(),
                      finalFeedItem.getSleekW() - feedItemWidth,
                      finalFeedItem.getSleekH(),
                      finalFeedItem.getSleekH(),
                      500,
                      new ISleekDrawView() {
                        @Override
                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekY(),
                              finalFeedItem.getSleekY() - (feedItemHeight / 2.0f),
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekH(),
                              finalFeedItem.getSleekH() + feedItemHeight,
                              500,
                              new ISleekDrawView() {
                                @Override
                                public void drawView(
                                    Sleek sleek,
                                    Canvas canvas,
                                    SleekCanvasInfo info
                                ) {
                                  finalFeedItem
                                      .setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                                          finalFeedItem.getSleekX(),
                                          finalFeedItem.getSleekY(),
                                          finalFeedItem.getSleekY() + (feedItemHeight / 2.0f),
                                          finalFeedItem.getSleekW(),
                                          finalFeedItem.getSleekW(),
                                          finalFeedItem.getSleekH(),
                                          finalFeedItem.getSleekH() - feedItemHeight,
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
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      });

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      if (i % 2 == 0) {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER));
      } else {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_FEED_ITEM_IMAGE_COVER_TWO));
      }

      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.ABSOLUTE, feedItemWidth, null)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
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

      sleekFeedItem =
          new SleekElement(SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext()));

      final SleekElement finalFeedItem = sleekFeedItem;
      finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
        long touchTs = 0;

        @Override
        public void run() {
          if (System.currentTimeMillis() - touchTs < 2000) {
            return;
          }
          touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();

          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
              finalFeedItem.getSleekX() - (feedItemWidth / 2.0f),
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekY(),
              finalFeedItem.getSleekW(),
              finalFeedItem.getSleekW() + feedItemWidth,
              finalFeedItem.getSleekH(),
              finalFeedItem.getSleekH(),
              500,
              new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

                  finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                      finalFeedItem.getSleekX() + (feedItemWidth / 2.0f),
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekY(),
                      finalFeedItem.getSleekW(),
                      finalFeedItem.getSleekW() - feedItemWidth,
                      finalFeedItem.getSleekH(),
                      finalFeedItem.getSleekH(),
                      500,
                      new ISleekDrawView() {
                        @Override
                        public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                          finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekX(),
                              finalFeedItem.getSleekY(),
                              finalFeedItem.getSleekY() - (feedItemHeight / 2.0f),
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekW(),
                              finalFeedItem.getSleekH(),
                              finalFeedItem.getSleekH() + feedItemHeight,
                              500,
                              new ISleekDrawView() {
                                @Override
                                public void drawView(
                                    Sleek sleek,
                                    Canvas canvas,
                                    SleekCanvasInfo info
                                ) {
                                  finalFeedItem
                                      .setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                                          finalFeedItem.getSleekX(),
                                          finalFeedItem.getSleekY(),
                                          finalFeedItem.getSleekY() + (feedItemHeight / 2.0f),
                                          finalFeedItem.getSleekW(),
                                          finalFeedItem.getSleekW(),
                                          finalFeedItem.getSleekH(),
                                          finalFeedItem.getSleekH() - feedItemHeight,
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
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      }, new Runnable() {
        @Override
        public void run() {
          //DO NOTHING
        }
      });

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      if (i % 3 == 0) {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_BORDER_3));
      } else if (i % 2 == 0) {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_BORDER_1));
      } else {
        sleekFeedItem.addCSS(new CSSblockBase(CSS_BORDER_2));
      }

      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.ABSOLUTE, feedItemWidth, null)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
        sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
      }

      sleekFeedItem.createBackgroundImage();
      sleekFeedItem.wrapBackgroundImageSize(false, true, true);

      sleekCanvas.addSleek(sleekFeedItem);

      lastSleekFeedItem = sleekFeedItem;
    }
  }

  private static void setSimpleBoundAnimation(
      final SleekBase finalFeedItem, final int feedItemWidth, final int feedItemHeight
  ) {
    finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
        finalFeedItem.getSleekX() - (feedItemWidth / 2.0f),
        finalFeedItem.getSleekY(),
        finalFeedItem.getSleekY(),
        finalFeedItem.getSleekW(),
        finalFeedItem.getSleekW() + feedItemWidth,
        finalFeedItem.getSleekH(),
        finalFeedItem.getSleekH(),
        1000,
        new ISleekDrawView() {
          @Override
          public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

            finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                finalFeedItem.getSleekX() + (feedItemWidth / 2.0f),
                finalFeedItem.getSleekY(),
                finalFeedItem.getSleekY(),
                finalFeedItem.getSleekW(),
                finalFeedItem.getSleekW() - feedItemWidth,
                finalFeedItem.getSleekH(),
                finalFeedItem.getSleekH(),
                1000,
                new ISleekDrawView() {
                  @Override
                  public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                    finalFeedItem.setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                        finalFeedItem.getSleekX(),
                        finalFeedItem.getSleekY(),
                        finalFeedItem.getSleekY() - (feedItemHeight / 2.0f),
                        finalFeedItem.getSleekW(),
                        finalFeedItem.getSleekW(),
                        finalFeedItem.getSleekH(),
                        finalFeedItem.getSleekH() + feedItemHeight,
                        1000,
                        new ISleekDrawView() {
                          @Override
                          public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                            finalFeedItem
                                .setSleekAnimView(new SAVtransXYWH(finalFeedItem.getSleekX(),
                                    finalFeedItem.getSleekX(),
                                    finalFeedItem.getSleekY(),
                                    finalFeedItem.getSleekY() + (feedItemHeight / 2.0f),
                                    finalFeedItem.getSleekW(),
                                    finalFeedItem.getSleekW(),
                                    finalFeedItem.getSleekH(),
                                    finalFeedItem.getSleekH() - feedItemHeight,
                                    1000,
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

  private static final void loadUIwithCSSblocks(
      final SleekCanvas sleekCanvas,
      final int viewWidthDP,
      final int viewHeightDP,
      final boolean enableAnimationOnTouch,
      final boolean viewWrapImageHeight,
      final CSSblock cssBlock1,
      final CSSblock cssBlock2,
      final CSSblock cssBlock3,
      final CSSblock cssBlock4
  ) {

    sleekCanvas.getHandler().post(new Runnable() {
      @Override
      public void run() {
        sleekCanvas.setBackgroundColor(0xffe8e8e8);
      }
    });

    UtilTestSleekUI.addUIframeRate(sleekCanvas);

    int feedItemTopMargin = UtilPx.getPixels((int) (viewHeightDP / 2.0f));
    final int feedItemWidth = UtilPx.getPixels(viewWidthDP);
    final int feedItemHeight = UtilPx.getPixels(viewHeightDP);

    SleekElement sleekFeedItem, lastSleekFeedItem = null;
    String feedItemString;
    for (int i = 1; i <= 40; i++) {
      sleekFeedItem = new SleekElement(
          SleekParam.DEFAULT_TOUCHABLE.newPriority(sleekCanvas.getDrawPrioNext())
      );
      final SleekElement finalFeedItem = sleekFeedItem;
      if (enableAnimationOnTouch) {
        finalFeedItem.getTouchHandler().setClickAction(new Runnable() {
          long touchTs = 0;

          @Override
          public void run() {
            if (System.currentTimeMillis() - touchTs < 4000) {
              return;
            }
            touchTs = finalFeedItem.getTouchHandler().getLastTouchDown();
            setSimpleBoundAnimation(finalFeedItem, feedItemWidth, feedItemHeight);
//            finalFeedItem.setSleekBounds(
//                finalFeedItem.getSleekX() - 200,
//                finalFeedItem.getSleekY() - 200,
//                finalFeedItem.getSleekW() + 400,
//                finalFeedItem.getSleekH() + 400
//            );
          }
        }, new Runnable() {
          @Override
          public void run() {
            //DO NOTHING
          }
        }, new Runnable() {
          @Override
          public void run() {
            //DO NOTHING
          }
        });
      }

      if (i % 4 == 0) {
        feedItemString = FEED_ITEM_STRING_1;
      } else if (i % 3 == 0) {
        feedItemString = FEED_ITEM_STRING_2;
      } else if (i % 2 == 0) {
        feedItemString = FEED_ITEM_STRING_3;
      } else {
        feedItemString = FEED_ITEM_STRING_4;
      }
      sleekFeedItem.setElementString(feedItemString);

      if (i % 4 == 0) {
        sleekFeedItem.addCSS(cssBlock1);
      } else if (i % 3 == 0) {
        sleekFeedItem.addCSS(cssBlock2);
      } else if (i % 2 == 0) {
        sleekFeedItem.addCSS(cssBlock3);
      } else {
        sleekFeedItem.addCSS(cssBlock4);
      }

      sleekFeedItem.getLayout()
          .x(SL.X.POS_CENTER, 0, null)
          .y(SL.Y.ABSOLUTE, feedItemTopMargin, null)
          .w(SL.W.ABSOLUTE, feedItemWidth, null)
          .h(SL.H.ABSOLUTE, feedItemHeight, null);
      if (lastSleekFeedItem != null) {
        sleekFeedItem.getLayout().y(SL.Y.SOUTH_OF, feedItemTopMargin, lastSleekFeedItem);
      } else {
        sleekFeedItem.getLayout().y(SL.Y.ABSOLUTE, feedItemTopMargin, null);
      }

      if (viewWrapImageHeight) {
        sleekFeedItem.wrapBackgroundImageSize(false, true, true);
      }

      sleekCanvas.addSleek(sleekFeedItem);
      lastSleekFeedItem = sleekFeedItem;
    }
  }

  private static final String
//      CSS_TOOLBAR = "{" +
//          "background: #d8d8d8;" +
//          "background-color: #d8d8d8;" +
//          "background-image: url(\"https://example.com/example.png\");" +
//          "background-size: cover;" +
//          "border-radius: 22px;" +
//          "border: 1px solid #0000ff;" +
//          "box-shadow: 1px 2px 4px rgba(120, 130, 140, 0.5);" +
//          "padding: 5px 10px 15px 20px;" +
//          "color: #666;" +
//          "font-size: 10px;" +
//          "line-height: 46px;" +
//          "text-align: center;" +
//          "vertical-align: middle;" +
//          "text-shadow: 1px 1px 2px black;" +
//          "}",
      CSS_TOOLBAR = "{" +
          "background-color: #101010;" +
//          "background-image: url(\"https://example.com/example.png\");" +
//          "background-size: cover;" +
//          "border-radius: 22px;" +
          "border: 1px solid #202020;" +
          "box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);" +
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
          "line-height: 50px;" +
          "text-shadow: 1px 1px 8px #ff0000;" +
          "}",
      CSS_BTN_SETTINGS = "{" +
          "background-color: #222;" +
//          "background-image: url(\"https://example.com/example.png\");" +
//          "background-size: cover;" +
          "border-radius: 30px;" +
          "border: 1px solid #38B0DE;" +
          "box-shadow: 0px 0px 4px #38B0DEdd;" +
          "padding: 0px 0px 5px 0px;" +
          "color: #eee;" +
          "font-size: 20px;" +
          "line-height: 20px;" +
          "text-align: center;" +
          "vertical-align: middle;" +
//          "text-shadow: 0px 0px 2px #38B0DE;" +
          "}",
      CSS_BTN_PROFILE = "{" +
          "background-color: #ff0000;" +
//          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/9/9d/Flag_of_Arizona.svg/2000px-Flag_of_Arizona.svg.png\");" +
          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/Flag_of_Flanders.svg/1024px-Flag_of_Flanders.svg.png\");" +
          "background-size: cover;" +
          "border-radius: 30px;" +
          "border: 1px solid #38B0DE;" +
          "box-shadow: 0px 0px 4px #38B0DEdd;" +
//          "padding: 0px 0px 0px 30px;" +
//          "color: #eee;" +
//          "font-size: 20px;" +
//          "line-height: 20px;" +
//          "text-align: center;" +
//          "vertical-align: middle;" +
//          "text-shadow: 0px 0px 2px #38B0DE;" +
          "}",
      CSS_BTN_IMAGE = "{" +
          "background-color: #33E776;" +
//          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/9/9d/Flag_of_Arizona.svg/2000px-Flag_of_Arizona.svg.png\");" +
//          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/Flag_of_Flanders.svg/1024px-Flag_of_Flanders.svg.png\");" +
          "background-size: cover;" +
          "border-radius: 30px;" +
          "border: 1px solid #38B0DE;" +
          "box-shadow: 0px 0px 4px #38B0DEdd;" +
          "padding: 0px 0px 2px 0px;" +
          "color: #333;" +
          "font-size: 14px;" +
          "line-height: 14px;" +
          "text-align: center;" +
          "vertical-align: middle;" +
//          "text-shadow: 0px 0px 2px #38B0DE;" +
          "}",
      CSS_BTN_ACTIVE = "{" +
          "background-color: #4860E3;" +
          "border-radius: 4px;" +
          "border: 1px solid #FFA038;" +
          "box-shadow: 0px 0px 4px #FFA038;" +
          "}",
      CSS_YELLOW_BACKGROUND = "{" +
          "background-color: #FFC638;" +
          "}",
      CSS_FLAG_BG_IMG = "{" +
          "background-size: cover;" +
          "background-image: url(\"https://upload.wikimedia.org/wikipedia/commons/thumb/9/9d/Flag_of_Arizona.svg/2000px-Flag_of_Arizona.svg.png\");" +
          "}",
      CSS_CELL_BASIC = "{" +
          "background-color: #e8e8e8;" +
//          "background-image: url(\"https://example.com/example.png\");" +
          "background-size: cover;" +
          "border-radius: 6px;" +
          "border: 1px solid #121212;" +
          "box-shadow: 0px 0px 20px rgba(30, 68, 210, 0.7);" +
          "padding: 10px;" +
          "color: #121212;" +
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
          "box-shadow: 1px 2px 14px rgba(255, 0, 0, 0.9);" +//"box-shadow: 1px 2px 4px rgba(120, 130, 140, 0.5);" +
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

  private static final void loadUIcompleteAppUIexample2(final SleekCanvas slkc) {

    final List<SleekElement> sleekElementList = new ArrayList<>(20);

    final CSSblock toolbarCSS = new CSSblockBase(CSS_TOOLBAR);
    final CSSblock toolbarActiveCSS = new CSSblockBase(CSS_TOOLBAR_ACTIVE);

    final SleekElement toolbar = new SleekElement(SleekElement.FIXED_POSITION_TRUE);
    toolbar.setElementString("Sleek" + "\nMore power to the UI");
    toolbar.addCSS(toolbarCSS);
    toolbar.getLayout()// X and W are stretched outside screen to hide WEST / EAST border+shadow
        .x(X.ABSOLUTE, -UtilPx.getPixels(10), null)
        .w(W.PERCENT_CANVAS, -UtilPx.getPixels(20), null, 1.0f)
        .h(H.ABSOLUTE, UtilPx.getPixels(102), null);
    toolbar.getBackground().getTouchHandler().setClickAction(
        new Runnable() { @Override public void run() {
          toolbar.addCSStransition(toolbarActiveCSS);
        }}, new Runnable() { @Override public void run() {
          toolbar.removeCSStransition(toolbarActiveCSS);
        }}, new Runnable() { @Override public void run() {
          if (sleekElementList.get(0).isAddedToParent()) {
            slkc.removeSleek(sleekElementList);
          } else {
            slkc.addSleek(sleekElementList);
          }
        }}
    );
    slkc.addSleek(toolbar);

    SleekElement iterElement;
    for (int i = 0; i < 20; i++) {
      iterElement = getSleekElementCellBasic();
      iterElement.setElementString("Cell #" + i + "\nThis cell is mucho cool!\nCell Basic FTW!");
      sleekElementList.add(iterElement);
    }

    UtilSleekLayout.initVerticalListLayout(
        sleekElementList,
        UtilPx.getPixels(140),
        UtilPx.getPixels(40)
    );
    slkc.addSleek(sleekElementList);

    //TODO CONTINUE BUILD APP UI HERE !!!!

    //TODO FIX LIST/GRID OF SCROLLABLE VIEWS, MAKE SURE AS LITTLE+CLEAR CODE AS POSSIBLE !!!!

    //TODO FIX TOP-/BOTTOM-MARGIN FOR SleekScrollXY so that EdgeEffects are drawn on correct pos !!
  }

  private static void setSleekElementCellBasicLayout(
      SleekBase layoutParent,
      SleekElement sleekElement
  ) {
    if (layoutParent == null) {// First view
      sleekElement.getLayout()
          .x(X.POS_CENTER, 0, null)
          .y(Y.ABSOLUTE, UtilPx.getPixels(140), null)
          .w(W.ABSOLUTE, UtilPx.getPixels(400), null)
          .h(H.ABSOLUTE, UtilPx.getPixels(300), null);
    } else {
      sleekElement.getLayout()
          .x(X.POS_CENTER, 0, layoutParent)
          .y(Y.SOUTH_OF, UtilPx.getPixels(40), layoutParent)
          .w(W.MATCH_PARENT, 0, layoutParent)
          .h(H.MATCH_PARENT, 0, layoutParent);
    }
  }

  private static SleekElement getSleekElementCellBasic() {
    final SleekElement sleekElement = new SleekElement();
    sleekElement.addCSS(cellBasicCSS);
//    sleekElement.getTouchHandler().setClickAction(
//        false,
//        new Runnable() { @Override public void run() {
//          sleekElement.removeCSSnoRefresh(cellBasicPressedCSS);//clear previous click animation CSS
//          sleekElement.removeCSSnoRefresh(cellBasicClickedCSS);//clear previous click animation CSS
//          sleekElement.addCSStransition(cellBasicPressedCSS);
//        }}, new Runnable() { @Override public void run() {
//          sleekElement.removeCSStransition(cellBasicPressedCSS);
//        }}, new Runnable() { @Override public void run() {
//          sleekElement.addCSStransition(cellBasicClickedCSS)
//              .setDuration(SleekCSSanim.ANIM_DURATION_SHORT_HALF)
//              .setDoneListener(new ISleekDrawView() {
//                @Override
//                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
//                  sleekElement.removeCSStransition(cellBasicPressedCSS, cellBasicClickedCSS)
//                      .setDuration(SleekCSSanim.ANIM_DURATION_SHORT_HALF);
//                }
//              });
//        }}
//    );
    sleekElement.setClickAction(
        cellBasicPressedCSS,
        cellBasicClickedCSS,
        new Assumeable<SleekCSSanim>() { @Override public void assume(SleekCSSanim cssAnimation) {
          cssAnimation.setDuration(1000);
          if (sleekElement.getSleekW() != UtilPx.getPixels(400)) {
            cssAnimation.setGoalW(UtilPx.getPixels(400));
          } else {
            cssAnimation.setGoalW(sleekElement.getSleekW() + UtilPx.getPixels(100));
          }
        }}
    );
    sleekElement.getLayout()
        .w(W.ABSOLUTE, UtilPx.getPixels(400), null)
        .h(H.ABSOLUTE, UtilPx.getPixels(300), null);
    return sleekElement;
  }

  private static final void loadUIcompleteAppUIexample1(final SleekCanvas slkc) {
    //TODO Build a simple complete App UI to test and demonstrate how easy Sleek is to use.

    final CSSblock toolbarCSS = new CSSblockBase(CSS_TOOLBAR);
    final CSSblock toolbarActiveCSS = new CSSblockBase(CSS_TOOLBAR_ACTIVE);
    final CSSblock activeCSS = new CSSblockBase(CSS_BTN_ACTIVE);
    final CSSblock yellowBgCSS = new CSSblockBase(CSS_YELLOW_BACKGROUND);
    final CSSblock flagBgImgCSS = new CSSblockBase(CSS_FLAG_BG_IMG);

    final SleekElement toolbar = new SleekElement(FIXED_TOUCHABLE.prio(slkc.getNextPrio()));
    //toolbar.setElementString("Sleek" + "\nMore power to the UI");
    toolbar.setElementString(
        "1 DP = " + (UtilPx.getDefaultContext().getResources().getDisplayMetrics().density) + " px"
        + "\nSleek" + "\nMore power to the UI"
        + "\n+1" + "\n+2" + "\n+3" + "\n+4"
    );
//    toolbar.setElementString("First Line ÅÄÖyjq\nMiddle Line ÅÄÖyjq\nLast Line ÅÄÖyjq");
//    toolbar.setElementString("Sleek" + "\nMore power to the UI" + "\n+1");
    toolbar.addCSS(toolbarCSS);
    toolbar.getLayout()// X and W are stretched outside screen to hide WEST / EAST border+shadow
        .x(X.ABSOLUTE, -UtilPx.getPixels(10), null)
//        .y(Y.ABSOLUTE, UtilPx.getPixels(100), null)
        .w(W.PERCENT_CANVAS, -UtilPx.getPixels(20), null, 1.0f)
        .h(H.ABSOLUTE, UtilPx.getPixels(102), null);
    toolbar.getBackground().getTouchHandler().setClickAction(
        new Runnable() { @Override public void run() {
//          toolbar.setElementString("\u2605 Sleek \u2605 TOUCH");
          toolbar.addCSStransition(toolbarActiveCSS)
              .setDuration(SleekCSSanim.ANIM_DURATION_MEDIUM);
        }}, new Runnable() { @Override public void run() {
//          toolbar.setElementString("\u2605 Sleek \u2605");
          toolbar.removeCSStransition(toolbarActiveCSS)
              .setDuration(SleekCSSanim.ANIM_DURATION_MEDIUM);
        }}, new Runnable() { @Override public void run() {

        }}
    );
    slkc.addSleek(toolbar);

    final int btnSize = UtilPx.getPixels(60);
    final int btnSpacing = UtilPx.getPixels(20);

    final SleekElement btnSettings = new SleekElement(TOUCHABLE);
    btnSettings.setElementString("α");
    btnSettings.addCSS(new CSSblockBase(CSS_BTN_SETTINGS));
    btnSettings.getLayout()
        .x(X.PARENT_RIGHT, toolbar.getPadding().right + btnSpacing, toolbar.getBackground())
//        .y(Y.POS_CENTER, 0, toolbar.getBackground())
        .y(Y.PARENT_BOTTOM, -Calc.divideToInt(btnSize, 2.0f), toolbar.getBackground())
        .w(W.ABSOLUTE, btnSize, null)
        .h(H.ABSOLUTE, btnSize, null);
    btnSettings.getTouchHandler().setClickAction(
        new Runnable() { @Override public void run() {
          btnSettings.addCSStransition(activeCSS);
        }}, new Runnable() { @Override public void run() {
          btnSettings.removeCSStransition(activeCSS);
        }}, new Runnable() { @Override public void run() {

        }}
    );
    toolbar.addSleek(btnSettings);

    final SleekElement btnProfile = new SleekElement(TOUCHABLE);
    btnProfile.addCSS(new CSSblockBase(CSS_BTN_PROFILE));
    btnProfile.getLayout()
        .x(X.WEST_OF, btnSpacing, btnSettings)
//        .y(Y.POS_CENTER, 0, toolbar.getBackground())
        .y(Y.PARENT_BOTTOM, -Calc.divideToInt(btnSize, 2.0f), toolbar.getBackground())
        .w(W.ABSOLUTE, btnSize, null)
        .h(H.ABSOLUTE, btnSize, null);
    btnProfile.getTouchHandler().setClickAction(
        new Runnable() { @Override public void run() {
          btnProfile.addCSS(activeCSS);
        }}, new Runnable() { @Override public void run() {
          btnProfile.removeCSS(activeCSS);
        }}, new Runnable() { @Override public void run() {

        }}
    );
    toolbar.addSleek(btnProfile);

    final SleekElement btnImage = new SleekElement(TOUCHABLE);
//    btnImage.setWrapTextWidth(true);
//    btnImage.setWrapTextHeight(true);
    btnImage.setElementString("Image");
    btnImage.addCSS(new CSSblockBase(CSS_BTN_IMAGE));
//    btnImage.createBackgroundImage();
//    btnImage.getBackgroundImage().setFadeAnimOnLoad(false);
    btnImage.getLayout()
        .x(X.WEST_OF, btnSpacing, btnProfile)
//        .y(Y.POS_CENTER, 0, toolbar.getBackground())
        .y(Y.PARENT_BOTTOM, -Calc.divideToInt(btnSize, 2.0f), toolbar.getBackground())
        .w(W.ABSOLUTE, btnSize, null)
        .h(H.ABSOLUTE, btnSize, null);
    btnImage.getTouchHandler().setClickAction(
        new Runnable() { @Override public void run() {

          //TODO If we were to use CSS "transition: all 0.5s ease-out;"
          //TODO then when we add CSSblock we need to get a SleekCSSanim object back...
          //TODO ...but what should happen if "transition"-property is NOT set, then we...
          //TODO ...should NOT get a SleekCSSanim-object returned... THINK ABOUT THIS!

          btnImage.setElementString("");
          btnImage.addCSStransition(flagBgImgCSS, activeCSS, yellowBgCSS)
              .setGoalX(btnProfile.getSleekX() - btnProfile.getSleekW() - btnSpacing - btnSize)
              .setGoalW(btnProfile.getSleekW() + btnSize)
              .setGoalH(btnProfile.getSleekH() + btnSize)
              .setDuration(SleekCSSanim.ANIM_DURATION_MEDIUM);
        }}, new Runnable() { @Override public void run() {
          btnImage.setElementString("Image");
          btnImage.removeCSStransition(flagBgImgCSS, activeCSS, yellowBgCSS)
              .setGoalX(btnProfile.getSleekX() - btnProfile.getSleekW() - btnSpacing)
              .setGoalW(btnProfile.getSleekW())
              .setGoalH(btnProfile.getSleekH())
              .setDuration(SleekCSSanim.ANIM_DURATION_MEDIUM);
        }}, new Runnable() { @Override public void run() {

        }}
    );
    toolbar.addSleek(btnImage);

    final SleekElement dummyElement1 = new SleekElement(TOUCHABLE);
    dummyElement1.addCSS(toolbarCSS);
    dummyElement1.addCSS(new CSSblockBase("{vertical-align: top;}"));
    dummyElement1.setElementString("ÅÄÖyjq\n123456\nHej!\nDetta är ett SleekElement!\n+1\n+2\n+3\n+4");
    dummyElement1.getLayout()// X and W are stretched outside screen to hide WEST / EAST border+shadow
        .x(X.ABSOLUTE, 0, null)
        .y(Y.ABSOLUTE, UtilPx.getPixels(140), null)
        .w(W.PERCENT_CANVAS, 0, null, 0.5f)
        .h(H.ABSOLUTE, UtilPx.getPixels(102), null);
    dummyElement1.getBackground().getTouchHandler().setClickAction(
        new Runnable() { @Override public void run() {
          dummyElement1.addCSStransition(toolbarActiveCSS).setDuration(600);
        }}, new Runnable() { @Override public void run() {
          dummyElement1.removeCSStransition(toolbarActiveCSS).setDuration(600);
        }}, new Runnable() { @Override public void run() {

        }}
    );
    slkc.addSleek(dummyElement1);

    final SleekElement dummyElement2 = new SleekElement(TOUCHABLE.prio(slkc.getNextPrio()));
    dummyElement2.addCSS(toolbarCSS);
    dummyElement2.addCSS(new CSSblockBase("{vertical-align: bottom;}"));
    dummyElement2.setElementString("ÅÄÖyjq\n123456\nHej!\nDetta är ett SleekElement!\n+1\n+2\n+3\n+4");
    dummyElement2.getLayout()// X and W are stretched outside screen to hide WEST / EAST border+shadow
        .x(X.EAST_OF, 0, dummyElement1)
        .y(Y.ABSOLUTE, UtilPx.getPixels(140), null)
        .w(W.PERCENT_CANVAS, 0, null, 0.5f)
        .h(H.ABSOLUTE, UtilPx.getPixels(102), null);
    dummyElement2.getBackground().getTouchHandler().setClickAction(
        new Runnable() { @Override public void run() {
          dummyElement2.addCSStransition(toolbarActiveCSS).setDuration(600);
        }}, new Runnable() { @Override public void run() {
          dummyElement2.removeCSStransition(toolbarActiveCSS).setDuration(600);
        }}, new Runnable() { @Override public void run() {

        }}
    );
    slkc.addSleek(dummyElement2);

    final SleekElement referenceElement = new SleekElement(TOUCHABLE.prio(slkc.getNextPrio()));
    referenceElement.addCSS(toolbarCSS);
    referenceElement.addCSS(new CSSblockBase("{padding: 0px;}"));
//    referenceElement.addCSS(new CSSblockBase("{vertical-align: top;}"));
//    referenceElement.addCSS(new CSSblockBase("{vertical-align: bottom;}"));
    referenceElement.addCSS(new CSSblockBase("{vertical-align: middle;}"));
    referenceElement.setElementString(FEED_ITEM_STRING_3);
//    referenceElement.setElementString("Padding: " + referenceElement.getPadding().left);
    referenceElement.getLayout()
        .x(X.POS_CENTER, 0, toolbar)
        .y(Y.SOUTH_OF, UtilPx.getPixels(50), dummyElement2)
        .w(W.ABSOLUTE, UtilPx.getPixels(300), null)
        .h(H.ABSOLUTE, UtilPx.getPixels(300), null);
//        .w(W.PERCENT_CANVAS, UtilPx.getPixels(100), null, 1.0f)
//        .h(H.COMPUTE, 0, null, 0, new IComputeInt() {
//          @Override
//          public int compute(SleekCanvasInfo info) {
//            return info.width - UtilPx.getPixels(100);
//          }
//        });
    referenceElement.getBackground().getTouchHandler().setClickAction(
        new Runnable() { @Override public void run() {
          referenceElement.addCSStransition(toolbarActiveCSS).setDuration(500);
        }}, new Runnable() { @Override public void run() {
          referenceElement.removeCSStransition(toolbarActiveCSS).setDuration(500);
        }}, new Runnable() { @Override public void run() {

        }}
    );
    slkc.addSleek(referenceElement);

    SleekColorArea referenceArea = new SleekColorArea(
        0xff4860E3,
        true,
        TOUCHABLE.prio(slkc.getNextPrio())
    );
    referenceArea.getLayout()
        .x(X.PARENT_LEFT, 0, referenceElement)
        .y(Y.SOUTH_OF, UtilPx.getPixels(50), referenceElement)
        .w(W.MATCH_PARENT, 0, referenceElement)
        .h(H.MATCH_PARENT, 0, referenceElement);
    slkc.addSleek(referenceArea);

    SleekColorArea bottomArea = new SleekColorArea(
        0xffFF5B38,
        true,
        TOUCHABLE.prio(slkc.getNextPrio())
    );
    bottomArea.getLayout()
        .x(X.ABSOLUTE, UtilPx.getPixels(50), null)
        .y(Y.SOUTH_OF, UtilPx.getPixels(50), referenceArea)
        .w(W.PERCENT_CANVAS, UtilPx.getPixels(100), null, 1.0f)
        .h(H.ABSOLUTE, UtilPx.getPixels(50), null);
    slkc.addSleek(bottomArea);

  }

  @Test
  public void testCompleteAppUI() throws Exception {

    if (mActivityRule.getActivity() == null) {
      throw new IllegalStateException("mActivityRule.getActivity() == null");
    }

    // Load App UI
    mActivityRule.getActivity().getUiHandler().post(new Runnable() {
      @Override
      public void run() {

        UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

        // Load SleekCanvas subclass
        //SleekCanvas sleekCanvas = new SleekCanvasExampleOne(mActivityRule.getActivity());
        SleekCanvas sleekCanvas = new SleekCanvasExampleTwo(mActivityRule.getActivity());

        mActivityRule.getActivity().setSleekCanvas(sleekCanvas);
        mActivityRule.getActivity().setContentView(sleekCanvas);
      }
    });

    final CountDownLatch activityPauseLatch = new CountDownLatch(1);
    mActivityRule.getActivity().setPauseListener(new Runnable() {
      @Override
      public void run() {
        activityPauseLatch.countDown();
      }
    });
    activityPauseLatch.await();
  }

  //@Test
  public void testCompleteAppUI_OLD() throws Exception {

    if (mActivityRule.getActivity() == null) {
      throw new IllegalStateException("mActivityRule.getActivity() == null");
    }

    UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());
    UtilTestSleekUI.setSleekActivitySleekCanvasScrollerY(mActivityRule.getActivity());
    UtilTestSleekUI.addUIframeRate(mActivityRule.getActivity().getSleekCanvas());

    // Load App UI
    mActivityRule.getActivity().getUiHandler().post(new Runnable() {
      @Override
      public void run() {
        //loadUIcompleteAppUIexample1(mActivityRule.getActivity().getSleekCanvas());
        loadUIcompleteAppUIexample2(mActivityRule.getActivity().getSleekCanvas());
      }
    });

    final CountDownLatch activityPauseLatch = new CountDownLatch(1);
    mActivityRule.getActivity().setPauseListener(new Runnable() {
      @Override
      public void run() {
        activityPauseLatch.countDown();
      }
    });
    activityPauseLatch.await();
  }

  //@Test
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
    //loadUIbackgroundCoverWithBorder(mActivityRule.getActivity().getSleekCanvas());
    loadUIwithCSSblocks(mActivityRule.getActivity().getSleekCanvas(),
        240,// viewWidthDP
        240,// viewHeightDP
        true,// enableAnimationOnTouch
        false,// viewWrapImageHeight
        new CSSblockBase(CSS_BORDER_1),
        new CSSblockBase(CSS_BORDER_2),
        new CSSblockBase(CSS_BORDER_3),
        new CSSblockBase(CSS_BORDER_4)
    );
//    loadUIwithCSSblocks(mActivityRule.getActivity().getSleekCanvas(),
//        160,// viewWidthDP
//        160,// viewHeightDP
//        true,// enableAnimationOnTouch
//        false,// viewWrapImageHeight
//        new CSSblockBase(CSS_BORDER_3),
//        new CSSblockBase(CSS_BORDER_5),
//        new CSSblockBase(CSS_BORDER_3),
//        new CSSblockBase(CSS_BORDER_5)
//    );
//    loadUIwithCSSblocks(mActivityRule.getActivity().getSleekCanvas(),
//        160,// viewWidthDP
//        160,// viewHeightDP
//        true,// enableAnimationOnTouch
//        false,// viewWrapImageHeight
//        new CSSblockBase(CSS_TEST_1),
//        new CSSblockBase(CSS_TEST_2),
//        new CSSblockBase(CSS_TEST_3),
//        new CSSblockBase(CSS_TEST_4)
//    );
//    loadUIwithCSSblocks(mActivityRule.getActivity().getSleekCanvas(),
//        160,// viewWidthDP
//        160,// viewHeightDP
//        true,// enableAnimationOnTouch
//        false,// viewWrapImageHeight
//        new CSSblockBase(CSS_TEST_5),
//        new CSSblockBase(CSS_TEST_6),
//        new CSSblockBase(CSS_TEST_7),
//        new CSSblockBase(CSS_TEST_8)
//    );
//    loadUIwithCSSblocks(mActivityRule.getActivity().getSleekCanvas(),
//        160,// viewWidthDP
//        160,// viewHeightDP
//        true,// enableAnimationOnTouch
//        false,// viewWrapImageHeight
//        new CSSblockBase(CSS_TEST_8),
//        new CSSblockBase(CSS_SHADOW_OFFSET_X_NEGATIVE),
//        new CSSblockBase(CSS_SHADOW_OFFSET_X_POSITIVE),
//        new CSSblockBase(CSS_TEST_8)
//    );

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
    final String imageUrl =
        "http://www.publicdomainpictures.net/pictures/30000/velka/evening-landscape-13530956185Aw.jpg";
    final String customFileName = "TestSleekUI_image1.jpg";
    final CountDownLatch downloadLatch = new CountDownLatch(4);

    // Attempt download on bg-thread
    UtilDownload.downloadUrl(imageUrl,
        customFileName,
        UtilExecutor.NETWORK_OCTA,
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
    UtilDownload.downloadUrl(imageUrl,
        customFileName,
        UtilExecutor.NETWORK_OCTA,
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
    UtilDownload.downloadUrl(imageUrl,
        customFileName,
        UtilExecutor.NETWORK_OCTA,
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
    final File imageFile =
        UtilDownload.downloadUrl(imageUrl, customFileName, 15000, new UtilDownload.FileDownload() {
          @Override
          public void downloadProgress(float percent) {

          }

          @Override
          public void downloadFinished(File file) {
            downloadLatch.countDown();
          }
        });

    downloadLatch.await(20000, TimeUnit.MILLISECONDS);
    assertNotNull(imageFile);
    assertTrue(imageFile.getAbsolutePath().contains(customFileName));
    assertTrue(imageFile.delete());
  }

  @Test
  public void testCSSborder() {

    UtilPx.setDefaultContext(mActivityRule.getActivity().getApplicationContext());

    CSSblockBase cssBlock1 = new CSSblockBase(CSS_BORDER_1);
    assertNotNull(cssBlock1.getBorderWidth());
    assertEquals(UtilPx.getPixels(4), cssBlock1.getBorderWidth().top);
    assertEquals(UtilPx.getPixels(4), cssBlock1.getBorderWidth().right);
    assertEquals(UtilPx.getPixels(4), cssBlock1.getBorderWidth().bottom);
    assertEquals(UtilPx.getPixels(4), cssBlock1.getBorderWidth().left);
    assertEquals(0xffffff00, cssBlock1.getBorderColor().intValue());

    CSSblockBase cssBlock2 = new CSSblockBase(CSS_BORDER_2);
    assertNotNull(cssBlock2.getBorderWidth());
    assertEquals(UtilPx.getPixels(2), cssBlock2.getBorderWidth().top);
    assertEquals(UtilPx.getPixels(2), cssBlock2.getBorderWidth().right);
    assertEquals(UtilPx.getPixels(2), cssBlock2.getBorderWidth().bottom);
    assertEquals(UtilPx.getPixels(2), cssBlock2.getBorderWidth().left);
    assertEquals(0xffff0000, cssBlock2.getBorderColor().intValue());
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
    assertEquals("sym_def_app_icon", cssBlock.getBackgroundImage());

    int resId = UtilPx.getDefaultContext()
        .getResources()
        .getIdentifier(cssBlock.getBackgroundImage(), "drawable", "android");
    assertTrue(resId > 0);
  }
}
