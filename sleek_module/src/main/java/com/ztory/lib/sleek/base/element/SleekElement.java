package com.ztory.lib.sleek.base.element;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.SleekParent;
import com.ztory.lib.sleek.SleekPrioCounter;
import com.ztory.lib.sleek.assumption.Assumeable;
import com.ztory.lib.sleek.base.SleekBaseComposite;
import com.ztory.lib.sleek.base.SleekColorArea;
import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.base.element.css.CSS;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.base.image.SleekBaseImage;
import com.ztory.lib.sleek.base.text.SleekViewText;
import com.ztory.lib.sleek.contract.ISleekAnimRun;
import com.ztory.lib.sleek.contract.ISleekCallback;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import com.ztory.lib.sleek.layout.IComputeInt;
import com.ztory.lib.sleek.layout.SL;
import com.ztory.lib.sleek.util.Calc;
import com.ztory.lib.sleek.util.UtilDownload;
import com.ztory.lib.sleek.util.UtilExecutor;
import com.ztory.lib.sleek.util.UtilPx;
import com.ztory.lib.sleek.util.UtilResources;
import com.ztory.lib.sleek.val.ValPair;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * NOTE:
 * Styling and layout in SleekElement is calculated as if CSS is set to:
 * box-sizing: border-box;
 * background-repeat: no-repeat;
 * background-position: center;
 * Created by jonruna on 2017-04-07.
 */
public class SleekElement extends SleekBaseComposite {

  public static final String CSS_BLOCK_ANIMATION_KEY = "SleekElement.isAnimationCSSblock";

  protected static final Executor
      SHADOW_EXECUTOR = UtilExecutor.createExecutor(
          SleekElement.class.getName() + "_SHADOW_EXECUTOR", 1
      ),
      BITMAP_EXECUTOR = UtilExecutor.createExecutor(
          SleekElement.class.getName() + "_BITMAP_EXECUTOR", 1
      );

  protected Executor
      bitmapDownloadExecutor = UtilExecutor.NETWORK_QUAD,
      bitmapShadowExecutor = SHADOW_EXECUTOR,
      bitmapLoadExecutor = BITMAP_EXECUTOR;

  protected final List<CSSblock> elementCSSlist = new ArrayList<>(4);

  protected final CSSblock elementCSS = new CSSblockBase(12);

  protected String elementString = null;

  protected final SleekColorArea elementBackground = new SleekColorArea(
      SleekColorArea.COLOR_TRANSPARENT,
      SleekColorArea.ANTI_ALIASED_FALSE,
      SleekParam.DEFAULT_TOUCHABLE
  );
  protected final SleekColorArea elementBorder = new SleekColorArea(
      SleekColorArea.COLOR_TRANSPARENT,
      SleekColorArea.ANTI_ALIASED_FALSE,
      SleekParam.DEFAULT_TOUCHABLE
  );
  protected SleekViewText elementText = null;
  protected Integer elementTextShadowColor = null;
  protected Integer elementTextShadowBlurRadius = null;
  protected Integer elementTextShadowOffsetX = null;
  protected Integer elementTextShadowOffsetY = null;

  protected Paint elementShadowBitmapPaint;
  protected final List<Bitmap> elementShadowBitmapList = new ArrayList<>();
  protected volatile int shadowViewSizeW;
  protected volatile int shadowViewSizeH;

  protected int elementBackgroundColor = SleekColorArea.COLOR_TRANSPARENT;
  protected int elementBorderColor = SleekColorArea.COLOR_TRANSPARENT;
  protected final Rect elementBorderWidth = new Rect();
  protected int elementBorderRadius = 0;
  protected float elementShadowRadius = 0;
  protected float elementShadowOffsetX = 0;
  protected float elementShadowOffsetY = 0;
  protected int elementShadowColor = 0;

  protected Rect paddingRect;

  protected String elementBackgroundSize = null;
  protected boolean elementBackgroundSizeIsCover = false;

  protected boolean localElementBackgroundImageUrl;
  protected String elementBackgroundImageUrl = null;
  protected SleekBaseImage elementBackgroundImage = null;

  protected boolean wrapTextWidth = false, wrapTextHeight = false;

  private Bitmap.Config defaultBitmapConfig = Config.ARGB_8888;

  public SleekElement() {
    this(false, SleekPrioCounter.next());
  }

  public SleekElement(boolean isFixedPosition) {
    this(isFixedPosition, SleekPrioCounter.next());
  }

  public SleekElement(SleekParam sleekParam) {
    this(sleekParam.fixed, sleekParam.priority);
  }

  public SleekElement(boolean isFixedPosition, int theTouchPrio) {
    super(isFixedPosition, theTouchPrio);
    setDimensionIgnoreBounds(true);
  }

  public void setClickAction(
      final CSSblock pressedCSS,
      final CSSblock clickedCSS,
      final Assumeable<SleekCSSanim> clickAction
  ) {
    getBackground().getTouchHandler().setClickAction(
        false,
        new Runnable() { @Override public void run() {
          removeCSSnoRefresh(pressedCSS);//clear previous click animation CSS
          removeCSSnoRefresh(clickedCSS);//clear previous click animation CSS
          addCSStransition(pressedCSS);
        }}, new Runnable() { @Override public void run() {
          removeCSStransition(pressedCSS);
        }}, new Runnable() { @Override public void run() {
          SleekCSSanim clickCSSanim = addCSStransition(clickedCSS);
          clickCSSanim.setDuration(SleekCSSanim.ANIM_DURATION_SHORT_HALF)
              .setDoneListener(new ISleekDrawView() {
                @Override
                public void drawView(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {
                  removeCSStransition(pressedCSS, clickedCSS)
                      .setDuration(SleekCSSanim.ANIM_DURATION_SHORT_HALF);
                }
              });
          if (clickAction != null) {
            clickAction.assume(clickCSSanim);
          }
        }}
    );
  }

  public void setWrapTextWidth(boolean shouldWrapTextWidth) {
    wrapTextWidth = shouldWrapTextWidth;
    if (elementText != null) {
      elementText.setWrapWidth(wrapTextWidth);
    }
  }

  public void setWrapTextHeight(boolean shouldWrapTextHeight) {
    wrapTextHeight = shouldWrapTextHeight;
    if (elementText != null) {
      elementText.setWrapHeight(wrapTextHeight);
    }
  }

  @Override
  public void onSleekParentAdd(SleekCanvas sleekCanvas, SleekParent composite) {
    super.onSleekParentAdd(sleekCanvas, composite);

    if (elementBackgroundImage != null) {
      elementBackgroundImage.onSleekParentAdd(sleekCanvas, SleekElement.this);
    }

    requestLayout();// Needs to be called in order for children to call their onSleekCanvasResize().
  }

  @Override
  public void onSleekParentRemove(SleekCanvas sleekCanvas, SleekParent composite) {
    super.onSleekParentRemove(sleekCanvas, composite);

    if (elementBackgroundImage != null) {
      elementBackgroundImage.onSleekParentRemove(sleekCanvas, SleekElement.this);
    }
  }

  public void refreshCSS() {

    if (elementCSS.size() > 0) {
      elementCSS.clear();
    }

    for (CSSblock iterBlock : elementCSSlist) {
      elementCSS.putAll(iterBlock);//will overwrite existing keys
    }

    applyCSS();
    reloadTextState();
    resetSleekBounds();// refreshes text padding and background-image positioning
    if (addedToParent && loaded) {
      reloadShadowBitmap(false);
    }
  }

  /**
   * NOTE: If applyCSS() has not been called prior to this then padding will not be initialized.
   * @return a Rect with padding values
   */
  public Rect getPadding() {
    if (paddingRect != null) {
      return paddingRect;
    } else {
      return new Rect();
    }
  }

  public void applyCSS() {

    // No need to apply CSS properties if there are none
    if (elementCSS.size() == 0) {
      return;
    }

    Integer backgroundColor = elementCSS.getBackgroundColor();
    if (backgroundColor != null) {
      elementBackgroundColor = backgroundColor;
    } else {
      elementBackgroundColor = SleekColorArea.COLOR_TRANSPARENT;
    }
    elementBackground.getPaint().setColor(elementBackgroundColor);

    Integer borderColor = elementCSS.getBorderColor();
    if (borderColor != null) {
      elementBorderColor = borderColor;
    } else {
      elementBorderColor = SleekColorArea.COLOR_TRANSPARENT;
    }
    elementBorder.getPaint().setColor(elementBorderColor);

    Rect borderWidth = elementCSS.getBorderWidth();
    if (borderWidth != null) {
      elementBorderWidth.set(borderWidth);
    } else {
      elementBorderWidth.set(0, 0, 0, 0);
    }

    Integer borderRadius = elementCSS.getBorderRadius();
    if (borderRadius != null) {
      elementBorderRadius = borderRadius;
    } else {
      elementBorderRadius = 0;
    }

    if (elementBorderRadius > elementBorderWidth.left) {
      elementBackground.getPaint().setAntiAlias(true);
      elementBackground.setRounded(elementBorderRadius - elementBorderWidth.left);
    }
    else {
      elementBackground.getPaint().setAntiAlias(false);
      elementBackground.setRounded(0);
    }
    elementBorder.getPaint().setAntiAlias(elementBorderRadius > 0);
    elementBorder.setRounded(elementBorderRadius);

    Integer boxShadowBlurRadius = elementCSS.getBoxShadowBlurRadius();
    if (boxShadowBlurRadius != null && boxShadowBlurRadius > 0) {
      Integer boxShadowOffsetX = elementCSS.getBoxShadowOffsetX();
      Integer boxShadowOffsetY = elementCSS.getBoxShadowOffsetY();
      Integer boxShadowColor = elementCSS.getBoxShadowColor();

      if (boxShadowOffsetX != null && boxShadowOffsetY != null && boxShadowColor != null) {
        elementShadowRadius = boxShadowBlurRadius;
        elementShadowOffsetX = boxShadowOffsetX;
        elementShadowOffsetY = boxShadowOffsetY;
        elementShadowColor = boxShadowColor;
        elementShadowBitmapPaint = new Paint();
        elementShadowBitmapPaint.setColor(elementBackgroundColor);
        elementShadowBitmapPaint.setAntiAlias(true);
      } else {
        elementShadowRadius = 0;
      }
    } else {
      elementShadowRadius = 0;
    }

    Integer color = elementCSS.getColor();
    if (color != null) {
      createText();
      elementText.setTextColor(color);
    }

    Integer fontSize = elementCSS.getFontSize();
    if (fontSize != null) {
      createText();
      elementText.setTextSize(fontSize);
    }

    Integer lineHeight = elementCSS.getLineHeight();
    if (lineHeight != null) {
      createText();
      elementText.setTextLineHeight(lineHeight);
    } else if (fontSize != null) {
      //if fontSize is set then use that as line-height if line-height is not specified
      elementText.setTextLineHeight(fontSize);
    }

    String textAlign = elementCSS.getTextAlign();
    if (lineHeight != null) {
      createText();
      if (textAlign.equals("center")) {
        elementText.setTextAlignInt(SleekViewText.ALIGN_CENTER);
      } else if (textAlign.equals("right")) {
        elementText.setTextAlignInt(SleekViewText.ALIGN_RIGHT);
      } else if (textAlign.equals("left")) {
        elementText.setTextAlignInt(SleekViewText.ALIGN_LEFT);
      }
    }

    String verticalAlign = elementCSS.getVerticalAlign();
    if (verticalAlign != null) {
      createText();
      if (verticalAlign.equals("middle")) {
        elementText.setTextAlignVertInt(SleekViewText.ALIGN_CENTER);
      } else if (verticalAlign.equals("bottom")) {
        elementText.setTextAlignVertInt(SleekViewText.ALIGN_BOTTOM);
      } else if (verticalAlign.equals("top")) {
        elementText.setTextAlignVertInt(SleekViewText.ALIGN_TOP);
      }
    }

    elementTextShadowColor = elementCSS.getTextShadowColor();
    if (elementTextShadowColor != null && elementTextShadowColor != SleekColorArea.COLOR_TRANSPARENT) {
      elementTextShadowBlurRadius = elementCSS.getTextShadowBlurRadius();
      elementTextShadowOffsetX = elementCSS.getTextShadowOffsetX();
      elementTextShadowOffsetY = elementCSS.getTextShadowOffsetY();
    } else {
      elementTextShadowColor = null;
      elementTextShadowBlurRadius = null;
      elementTextShadowOffsetX = null;
      elementTextShadowOffsetY = null;
    }

    paddingRect = elementCSS.getPadding();

    String oldElementBackgroundImageUrl = elementBackgroundImageUrl;
    elementBackgroundImageUrl = elementCSS.getBackgroundImage();
    if (elementBackgroundImageUrl != null) {
      if (!elementBackgroundImageUrl.equals(oldElementBackgroundImageUrl)) {
        localElementBackgroundImageUrl = !elementBackgroundImageUrl.startsWith("https://")
            && !elementBackgroundImageUrl.startsWith("http://");
        createBackgroundImage();
        reloadBackgroundImage();
      }
    } else if (elementBackgroundImage != null) {
      initBackgroundImageBitmapFetcher();
      elementBackgroundImage.onSleekUnload();
    }

    elementBackgroundSize = elementCSS.getBackgroundSize();
    elementBackgroundSizeIsCover = CSS.Value.COVER.equals(elementBackgroundSize);
  }

  public Interpolator getDefaultInterpolator() {
    if (VERSION.SDK_INT >= 21) {
      return new PathInterpolator(0.785f, 0.135f, 0.150f, 0.860f);// easeInOutCirc
    } else {
      return new AccelerateDecelerateInterpolator();
    }
  }

  public SleekCSSanim addCSStransition(CSSblock... cssBlockArray) {
    SleekCSSanim cssAnimation = new SleekCSSanim(this, SleekCSSanim.ADD_CSS, cssBlockArray);
    cssAnimation.setInterpolator(getDefaultInterpolator());
    setSleekAnimView(cssAnimation);
    return cssAnimation;
  }

  public SleekCSSanim removeCSStransition(CSSblock... cssBlockArray) {
    SleekCSSanim cssAnimation = new SleekCSSanim(this, SleekCSSanim.REMOVE_CSS, cssBlockArray);
    cssAnimation.setInterpolator(getDefaultInterpolator());
    setSleekAnimView(cssAnimation);
    return cssAnimation;
  }

  public SleekElement addCSSnoRefresh(CSSblock cssBlock) {
    elementCSSlist.add(cssBlock);
    return this;
  }

  public SleekElement addCSS(CSSblock... cssBlocks) {
    for (CSSblock iterBlock : cssBlocks) {
      elementCSSlist.add(iterBlock);
    }
    refreshCSS();
    return this;
  }

  public boolean removeCSSnoRefresh(CSSblock cssBlock) {
    return elementCSSlist.remove(cssBlock);
  }

  public boolean removeCSS(CSSblock cssBlock) {
    boolean removedItem = elementCSSlist.remove(cssBlock);
    refreshCSS();
    return removedItem;
  }

  public int removeCSSblocksRaw(CSSblock... cssBlock) {
    int removedBlocks = 0;
    for (CSSblock iterBlock : cssBlock) {
      if (removeCSSnoRefresh(iterBlock)) {
        removedBlocks++;
      }
    }
    return removedBlocks;
  }

  public boolean containsCSSblock(CSSblock cssBlock) {
    return elementCSSlist.contains(cssBlock);
  }

  public int removeAnimationCSSblocks() {
    int removedBlocks = 0;
    for (int i = elementCSSlist.size() - 1 ; i >= 0; i--) {
      if (elementCSSlist.get(i).get(CSS_BLOCK_ANIMATION_KEY) != null) {
        elementCSSlist.remove(i);
        removedBlocks++;
      }
    }
    return removedBlocks;
  }

  public void setElementString(String theElementString) {
    elementString = theElementString;
    createText();
    elementText.setTextString(elementString);
    reloadTextState();
    invalidateSafe();
  }

  public String getElementString() {
    return elementString;
  }

  public Paint getBgPaint() {
    if (elementShadowRadius > 0) {
      return elementShadowBitmapPaint;
    } else {
      return elementBackground.getPaint();
    }
  }

  public SleekColorArea getBackground() {
    return elementBackground;
  }

  public void createText() {
    if (elementText != null) {
      return;
    }
    elementText = new SleekViewText(SleekParam.DEFAULT);
    elementText.setWrapWidth(wrapTextWidth);
    elementText.setWrapHeight(wrapTextHeight);
  }

  public SleekViewText getText() {
    return elementText;
  }

  public void createBackgroundImage() {
    // Default to fade anim for remote images
    createBackgroundImage(!localElementBackgroundImageUrl);
  }

  public void createBackgroundImage(boolean fadeAnimOnLoad) {
    if (elementBackgroundImage != null) {
      return;
    }

    elementBackgroundImage = new SleekBaseImage(elementBorderRadius, SleekParam.DEFAULT);
    elementBackgroundImage.setFadeAnimOnLoad(fadeAnimOnLoad);
    elementBackgroundImage.setBitmapListener(new ISleekCallback<SleekBaseImage>() {
      @Override
      public void sleekCallback(SleekBaseImage sleekBaseImage) {
        positionBackgroundImage();
      }
    });
  }

  public void positionBackgroundImage() {

    if (elementBackgroundImage == null || elementBackgroundImage.getBitmap() == null) {
      return;
    }

    //NOTE: Background logic for "cover" and "contain" is calculated as if this CSS was set:
    //      background-position: center center;

    final int bitmapW = elementBackgroundImage.getBitmap().getWidth();
    final int bitmapH = elementBackgroundImage.getBitmap().getHeight();
    int backgroundX = 0;
    int backgroundY = 0;
    int backgroundWidth = bitmapW;
    int backgroundHeight = bitmapH;
    final int imgContainerW = elementBackground.getSleekW();
    final int imgContainerH = elementBackground.getSleekH();

    if (!elementBackgroundSizeIsCover) {
      // Reset source rect
      elementBackgroundImage.getSourceRect().set(0, 0, backgroundWidth, backgroundHeight);
      elementBackgroundImage.setShaderMatrix();
    }

    if (elementBackgroundSize != null) {

      if (elementBackgroundSizeIsCover) {
        /*
        cover
        A keyword that is the inverse of contain.
        Scales the image as large as possible and maintains image aspect
        ratio (image doesn't get squished). The image "covers" the entire
        width or height of the container. When the image and container have
        different dimensions, the image is clipped either left/right or top/bottom.
         */
        float bitmapScale;
        float elementRatio = Calc.divide(imgContainerW, imgContainerH);
        float bitmapRatio = Calc.divide(backgroundWidth, backgroundHeight);
        if (bitmapRatio > elementRatio) {
          bitmapScale = Calc.divide(imgContainerH, bitmapH);
          backgroundWidth = Calc.multiplyToInt(imgContainerH, bitmapRatio);
          backgroundX = (int) Calc.divide(imgContainerW - backgroundWidth, 2.0f);
        } else {
          bitmapScale = Calc.divide(imgContainerW, bitmapW);
          backgroundHeight = Calc.divideToInt(imgContainerW, bitmapRatio);
          backgroundY = (int) Calc.divide(imgContainerH - backgroundHeight, 2.0f);
        }

        int sourceTop = Calc.multiplyToInt(Calc.divide(backgroundY, bitmapScale), -1.0f);
        int sourceBottom = bitmapH - sourceTop;
        int sourceLeft = Calc.multiplyToInt(Calc.divide(backgroundX, bitmapScale), -1.0f);
        int sourceRight = bitmapW - sourceLeft;
        elementBackgroundImage.getSourceRect().set(
            sourceLeft, sourceTop, sourceRight, sourceBottom
        );
        elementBackgroundImage.setShaderMatrix();

        //Reset drawing position
        backgroundX = 0;
        backgroundY = 0;

        // Reset drawing size to element size
        backgroundWidth = imgContainerW;
        backgroundHeight = imgContainerH;
      } else if (elementBackgroundSize.equals(CSS.Value.CONTAIN)) {
        /*
        contain
        A keyword that scales the image as large as possible and maintains image aspect
        ratio (image doesn't get squished). Image is letterboxed within the container.
        When the image and container have different dimensions, the empty
        areas (either top/bottom of left/right) are filled with the background-color.
         */
        float elementRatio = Calc.divide(imgContainerW, imgContainerH);
        float bitmapRatio = Calc.divide(backgroundWidth, backgroundHeight);
        if (bitmapRatio > elementRatio) {
          backgroundWidth = imgContainerW;
          backgroundHeight = (int) Calc.divide(imgContainerW, bitmapRatio);
          backgroundY = (int) Calc.divide(imgContainerH - backgroundHeight, 2.0f);
        } else {
          backgroundHeight = imgContainerH;
          backgroundWidth = (int) Calc.multiply(imgContainerH, bitmapRatio);
          backgroundX = (int) Calc.divide(imgContainerW - backgroundWidth, 2.0f);
        }
      }
    }

    if (backgroundX > 0) {// Used for dynamic-corner-radius on CONTAIN background-image
      float borderRadiusModif = elementBackground.getRoundedRadius() - backgroundX;
      if (borderRadiusModif < 0) {
        borderRadiusModif = 0;
      }
      elementBackgroundImage.setRoundedRadius(borderRadiusModif);
    } else if (backgroundY > 0) {// Used for dynamic-corner-radius on CONTAIN background-image
      float borderRadiusModif = elementBackground.getRoundedRadius() - backgroundY;
      if (borderRadiusModif < 0) {
        borderRadiusModif = 0;
      }
      elementBackgroundImage.setRoundedRadius(borderRadiusModif);
    } else {
      if (elementBorderColor == SleekColorArea.COLOR_TRANSPARENT) {
        elementBackgroundImage.setRoundedRadius(elementBorderRadius);
      } else if (elementBorderRadius > elementBorderWidth.left) {
        elementBackgroundImage.setRoundedRadius(elementBorderRadius - elementBorderWidth.left);
      } else {
        elementBackgroundImage.setRoundedRadius(0);
      }
    }

    elementBackgroundImage.setSleekBounds(
        backgroundX + elementBorderWidth.left,
        backgroundY + elementBorderWidth.top,
        backgroundWidth,
        backgroundHeight
    );
  }

  public void reloadBackgroundImage() {

    initBackgroundImageBitmapFetcher();

    if (elementBackgroundImageUrl != null) {
      if (loaded && addedToParent) {
        elementBackgroundImage.onSleekUnload();
        mSlkCanvas.loadSleek(elementBackgroundImage);
        checkSetLocalBackground();
      }
    }
  }

  public void setDefaultBitmapConfig(Bitmap.Config config) {
    defaultBitmapConfig = config;
  }

  public Bitmap.Config getDefaultBitmapConfig() {
    return defaultBitmapConfig;
  }

  //TODO THIS IS DEBUG CODE
//  private static final AtomicInteger debugCounter = new AtomicInteger(0);
//  private final double randomDbl = debugCounter.incrementAndGet();

  protected File getDownloadedBitmapFile() {
    return UtilDownload.downloadUrl(elementBackgroundImageUrl);
    //TODO THIS IS DEBUG CODE
    //return UtilDownload.downloadUrl("http://lorempixel.com/400/400/people/" + "?" + randomDbl);
  }

  protected volatile File bitmapFile = null;

  protected Runnable bitmapLoaderRun = new Runnable() {
    @Override
    public void run() {

      final File finalBitmapFile = bitmapFile;
      bitmapFile = null;

      if (finalBitmapFile == null) {
        return;
      }

      if ((isSleekLoadable() && !isSleekLoaded()) || !isAddedToParent()) {
        return;
      }

      // https://stackoverflow.com/questions/3117429/garbage-collector-in-android
      System.gc();

      // Ensure one fullscreen ARGB_8888 bitmap can be loaded into memory
      int minRequiredMemory = getCanvasWidth() * getCanvasHeight() * 8;
      while (UtilDownload.getAvailableMemory() < minRequiredMemory) {

        Log.d("SleekElement memstats",
            "memstats WAITING | avail: " + UtilDownload.getAvailableMemory() +
                " | minRequiredMemory: " + minRequiredMemory
        );

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        if ((isSleekLoadable() && !isSleekLoaded()) || !isAddedToParent()) {
          return;
        }
      }

      Bitmap bitmap = null;
      FileInputStream fis = null;
      try {

        fis = new FileInputStream(finalBitmapFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = getDefaultBitmapConfig();
        bitmap = BitmapFactory.decodeStream(fis, null, options);
        fis.close();
      } catch (Exception e) {
        UtilDownload.closeSafe(fis);
        e.printStackTrace();
      }

      if ((isSleekLoadable() && !isSleekLoaded()) || !isAddedToParent()) {
        //bitmap.recycle();
        bitmap = null;
      }

      if (bitmap != null) {
        // Attempt to prepare bitmap for drawing in order to prevent...
        // ...FPS-lag when drawn for first time in the Android UI.
        // https://developer.android.com/reference/android/graphics/Bitmap.html#prepareToDraw()
        bitmap.prepareToDraw();

//        try {
//          System.gc();
//          Thread.sleep(34);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
      }

      final Bitmap finalBitmap = bitmap;
      addPreDrawSafe(mSlkCanvas, new ISleekAnimRun() {
        @Override
        public void run(SleekCanvasInfo info) {
          elementBackgroundImage.setBitmap(finalBitmap);
        }
      });
    }
  };

  protected Runnable bitmapFetcherRun = new Runnable() {
    @Override
    public void run() {

      bitmapFile = null;

      if ((isSleekLoadable() && !isSleekLoaded()) || !isAddedToParent()) {
        return;
      }

      final File bmFile = getDownloadedBitmapFile();

      if (bmFile == null) {
        return;
      }

      if ((isSleekLoadable() && !isSleekLoaded()) || !isAddedToParent()) {
        return;
      }

      bitmapFile = bmFile;

      bitmapLoadExecutor.execute(bitmapLoaderRun);
    }
  };

  public void initBackgroundImageBitmapFetcher() {
    if (elementBackgroundImageUrl == null || localElementBackgroundImageUrl) {
      elementBackgroundImage.setBitmapFetcher(null, null, null);//clear fetcher
    } else {
      elementBackgroundImage.setBitmapFetcher(
          bitmapDownloadExecutor,
          bitmapFetcherRun
      );
    }
  }

  public void checkSetLocalBackground() {

    if (elementBackgroundImageUrl != null && localElementBackgroundImageUrl) {

      // DEBUG CODE, because we have no drawable resources in sleek library.
//      int resId = UtilPx.getDefaultContext().getResources().getIdentifier(
//          elementBackgroundImageUrl,
//          "drawable",
//          "android"
//      );
//      elementBackgroundImage.setBitmap(UtilResources.getBitmap(resId));

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inPreferredConfig = getDefaultBitmapConfig();
      Bitmap bitmapResource = BitmapFactory.decodeResource(
          UtilPx.getDefaultContext().getResources(),
          UtilResources.getResourceIdDrawable(elementBackgroundImageUrl),
          options
      );
      elementBackgroundImage.setBitmap(bitmapResource);
    }
  }

  public SleekBaseImage getBackgroundImage() {
    return elementBackgroundImage;
  }

  public void wrapBackgroundImageSize(
      final boolean wrapWidth,
      final boolean wrapHeight,
      final boolean executeSleekCanvasResize
  ) {
    createBackgroundImage();//ensure background image is created if null
    getBackgroundImage().setBitmapListener(new ISleekCallback<SleekBaseImage>() {
      @Override
      public void sleekCallback(SleekBaseImage sleekBaseImage) {

        if (sleekBaseImage.getBitmap() != null) {
          final int bitmapWidth = sleekBaseImage.getBitmap().getWidth();
          final int bitmapHeight = sleekBaseImage.getBitmap().getHeight();
          final float bitmapRatio = Calc.divide(bitmapWidth, bitmapHeight);
          if (wrapWidth && wrapHeight) {
            SleekElement.this.getLayout()
                .w(SL.W.ABSOLUTE, bitmapWidth, null)
                .h(SL.H.ABSOLUTE, bitmapHeight, null);
          } else if (wrapWidth) {
            SleekElement.this.getLayout().w(SL.W.COMPUTE, 0, null, 0, new IComputeInt() {
              @Override
              public int compute(SleekCanvasInfo info) {
                int computedH = SleekElement.this.getLayout().computeSizeH(SleekElement.this, info);
                return Calc.multiplyToInt(computedH, bitmapRatio);
              }
            });
          } else if (wrapHeight) {
            SleekElement.this.getLayout().h(SL.H.COMPUTE, 0, null, 0, new IComputeInt() {
              @Override
              public int compute(SleekCanvasInfo info) {
                int computedW = SleekElement.this.getLayout().computeSizeW(SleekElement.this, info);
                return Calc.divideToInt(computedW, bitmapRatio);
              }
            });
          }

          if (executeSleekCanvasResize) {
            SleekElement.this.executeSleekCanvasResize();
          } else {
            SleekElement.this.requestLayout();
          }
        }
      }
    });
  }

  public CSSblock getCSS() {
    return elementCSS;
  }

  public void drawBorder(Canvas canvas, SleekCanvasInfo info) {
    if (elementBorderColor == SleekColorArea.COLOR_TRANSPARENT) {
      return;
    }
    elementBorder.onSleekDraw(canvas, info);
  }

  @Override
  public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

    canvas.save();
    canvas.translate(sleekX, sleekY);

    //TODO DEBUG
//    drawShadowBitmap(canvas, info);
//    elementBorder.getPaint().setColor(0x99ff0000);
//    drawBorder(canvas, info);
    //TODO DEBUG

    if (!drawShadowBitmap(canvas, info)) {
      drawBorder(canvas, info);
      elementBackground.onSleekDraw(canvas, info);
      if (elementBackgroundImage != null) {
        elementBackgroundImage.onSleekDraw(canvas, info);
      }
    } else if (elementBackgroundImage != null) {
      elementBackgroundImage.onSleekDraw(canvas, info);
    }

    if (elementText != null) {
      elementText.onSleekDraw(canvas, info);
    }

    for (Sleek iterView : views) {
      //if (iterView.shouldBeDrawn()) iterView.draw(canvas, info);// dont need to check this, if this view is loaded it should draw its children
      iterView.onSleekDraw(canvas, info);
    }

    canvas.restore();
  }

  public void reloadTextState() {
    if (elementText != null && elementString != null) {
      elementText.initText();
      reloadTextShadow();
    }
  }

  protected void reloadTextShadow() {
    if (elementTextShadowColor != null
        && elementTextShadowColor != SleekColorArea.COLOR_TRANSPARENT
        ) {
      elementText.getTextPaint().setShadowLayer(
          elementTextShadowBlurRadius,
          elementTextShadowOffsetX,
          elementTextShadowOffsetY,
          elementTextShadowColor
      );
    } else {
      elementText.getTextPaint().clearShadowLayer();
    }
  }

  public void resetSleekBounds() {
    setSleekBounds(getSleekX(), getSleekY(), getSleekW(), getSleekH());
  }

  @Override
  public void setSleekBounds(float x, float y, int w, int h) {

    if (elementText != null) {
      final int textOldW = elementText.getSleekW();
      final int textOldH = elementText.getSleekH();
      if (paddingRect != null) {
        elementText.setSleekBounds(
            paddingRect.left + elementBorderWidth.left,
            paddingRect.top + elementBorderWidth.top,
            w - paddingRect.left - paddingRect.right
                - elementBorderWidth.left - elementBorderWidth.right,
            h - paddingRect.top - paddingRect.bottom
                - elementBorderWidth.top - elementBorderWidth.bottom
        );
      } else {
        elementText.setSleekBounds(
            elementBorderWidth.left,
            elementBorderWidth.top,
            w - elementBorderWidth.left - elementBorderWidth.right,
            h - elementBorderWidth.top - elementBorderWidth.bottom
        );
      }

      if (elementString != null) {
        // Refresh text bounds if size has changed
        if (textOldW != elementText.getSleekW() || textOldH != elementText.getSleekH()) {
          //Log.d("SleekElement", "SleekElement | Refresh text bounds if size has changed");
          elementText.setMaxWrapWidth(elementText.getSleekW());
          elementText.setMaxWrapHeight(elementText.getSleekH());
          reloadTextState();
        }
      }
    }

    int elementWidth = w;
    int elementHeight = h;

    if ((wrapTextWidth || wrapTextHeight) && elementText != null) {

      int paddingTopBottom = paddingRect != null ? paddingRect.top + paddingRect.bottom : 0;
      int paddingLeftRight = paddingRect != null ? paddingRect.left + paddingRect.right : 0;

      paddingTopBottom = paddingTopBottom + elementBorderWidth.top + elementBorderWidth.bottom;
      paddingLeftRight = paddingLeftRight + elementBorderWidth.left + elementBorderWidth.right;

      if (wrapTextWidth) {//wrap only width
        elementWidth = elementText.getSleekW() + paddingLeftRight;
      }

      if (wrapTextHeight) {//wrap only height
        elementHeight = elementText.getSleekH() + paddingTopBottom;
      }
    }

    super.setSleekBounds(x, y, elementWidth, elementHeight);

    elementBorder.setSleekBounds(
        0,
        0,
        elementWidth,
        elementHeight
    );
    elementBackground.setSleekBounds(
        elementBorder.getSleekX() + elementBorderWidth.left,
        elementBorder.getSleekY() + elementBorderWidth.top,
        elementBorder.getSleekW() - elementBorderWidth.left - elementBorderWidth.right,
        elementBorder.getSleekH() - elementBorderWidth.top - elementBorderWidth.bottom
    );

    if (loaded && addedToParent) {
      positionBackgroundImage();
    }
  }

  protected void setElementShadowBitmap(List<Bitmap> theShadowBitmapList) {

    if (elementShadowBitmapList.size() > 0) {
      for (Bitmap iterShadowBitmap : elementShadowBitmapList) {
        if (iterShadowBitmap != null) {
          //iterShadowBitmap.recycle();
        }
      }
      elementShadowBitmapList.clear();
    }

    if (theShadowBitmapList != null) {
      elementShadowBitmapList.addAll(theShadowBitmapList);
    }
  }

  @Override
  public void onSleekCanvasResize(SleekCanvasInfo info) {
    super.onSleekCanvasResize(info);
  }

  public void reloadShadowBitmap(boolean allowBgThreadLoad) {
    if (elementShadowRadius > 0) {
      if (allowBgThreadLoad) {

        //TODO Maybe exeute on UI-thread if view is within viewport when this method is called ?

        bitmapShadowExecutor.execute(new Runnable() {
          @Override
          public void run() {

            if ((isSleekLoadable() && !isSleekLoaded()) || !isAddedToParent()) {
              return;
            }

            final List<Bitmap> shadowBitmapList = generateShadowBitmap();

            if ((isSleekLoadable() && !isSleekLoaded()) || !isAddedToParent()) {
              return;
            }

            addPreDrawSafe(mSlkCanvas, new ISleekAnimRun() {
              @Override
              public void run(SleekCanvasInfo info) {
                if (loaded && addedToParent) {
                  setElementShadowBitmap(shadowBitmapList);
                }
              }
            });
          }
        });
      } else {
        setElementShadowBitmap(generateShadowBitmap());
      }
    }
    else {
      setElementShadowBitmap(null);
    }
  }

  @Override
  public void onSleekLoad(SleekCanvasInfo info) {

    final boolean alreadyLoaded = loaded;

    super.onSleekLoad(info);

    if (alreadyLoaded) {
      return;
    }

    reloadShadowBitmap(true);

    if (elementBackgroundImage != null) {
      elementBackgroundImage.onSleekLoad(info);
      checkSetLocalBackground();
    }
  }

  @Override
  public void onSleekUnload() {
    super.onSleekUnload();
    setElementShadowBitmap(null);
    if (elementBackgroundImage != null) {
      elementBackgroundImage.onSleekUnload();
    }
    bitmapFile = null;
  }

  @Override
  public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo info) {

    if (touchHandler.onSleekTouchEvent(event, info)) {
      return true;
    }

    event.offsetLocation(-sleekX, -sleekY);

    touchEventReturn = false;

    if (touchable) {
      for (int i = views.size() - 1; i >= 0; i--) {
        if (views.get(i).isSleekTouchable() && views.get(i).onSleekTouchEvent(event, info)) {
          touchEventReturn = true;
          break;
        }
      }
    }

    if (!touchEventReturn) {
      touchEventReturn = elementBackground.getTouchHandler().onSleekTouchEvent(event, info);
    }

    event.offsetLocation(sleekX, sleekY);

    return touchEventReturn;
  }

  protected final Rect shadowSrcRect = new Rect();
  protected final RectF shadowDstRectF = new RectF();

  protected boolean drawShadowBitmap(Canvas canvas, SleekCanvasInfo info) {

    if (elementShadowBitmapList.size() < 4) {
      return false;
    }

    //TODO FIX SO THAT ALPHA ANIMATION WORKS AS IT SHOULD WITH MIDDLE AREA AND BORDER AREAS !!!!
    //TODO Maybe we can set bitmap-bg-paint to alpha==255, and shadow to delta of real...
    //TODO ... bg-alpha and its shadow-alpha ????
    //TODO NEED TO add interface that is called HasAlpha and has getAlpha and setAlpha methods.

    final boolean drawBgRectBeforeShadow = elementBorderColor == SleekColorArea.COLOR_TRANSPARENT;

    if (drawBgRectBeforeShadow) {
      canvas.drawRect(
          elementBorderRadius,
          elementBorderRadius,
          sleekW - elementBorderRadius,
          sleekH - elementBorderRadius,
          elementShadowBitmapPaint
      );
    }

    /*
    // LOW PRIO since why would we want alpha bg-elements with shadow?
    // If we implement alpha bg with shadow then shadowBitmaps need to contain "hidden" ...
    // ... shadow-edges as well, so that they shine through.
    // Draw middle area of shadow, if bg has alpha we need to draw this because otherwise...
    // ...there is a "hole" in the middle since shadow will only be drawn along edges.
    float shadowBgTopOffset = elementShadowOffsetY > 0 ? elementShadowOffsetY : 0;
    float shadowBgBottomOffset = elementShadowOffsetY < 0 ? elementShadowOffsetY : 0;
    float shadowBgLeftOffset = elementShadowOffsetX > 0 ? elementShadowOffsetX : 0;
    float shadowBgRightOffset = elementShadowOffsetX < 0 ? elementShadowOffsetX : 0;
    elementBackground.getPaint().setColor(0x99ff00ff);
    canvas.drawRect(
            elementBorderRadius + shadowBgLeftOffset,
            elementBorderRadius + shadowBgTopOffset,
            sleekW - elementBorderRadius + shadowBgRightOffset,
            sleekH - elementBorderRadius + shadowBgBottomOffset,
            elementBackground.getPaint()//using elementBackground.getPaint to have correct Alpha
    );
    elementBackground.getPaint().setColor(elementBackgroundColor);
    */

    //TODO REFACTOR CODE IN THIS METHOD TO BE EASIER TO READ !

    float leftOffset = 0;
    if (elementShadowOffsetX < 0) {
      leftOffset = elementShadowOffsetX;
    }

    float topOffset = 0;
    if (elementShadowOffsetY < 0) {
      topOffset = elementShadowOffsetY;
    }

    //int stretchedWidthDelta = sleekW - shadowViewSizeW;
    int stretchedHeightDelta = sleekH - shadowViewSizeH;
    Bitmap currentBitmap;
    int currentBitmapW;
    int currentBitmapH;
    final Bitmap leftBitmap = elementShadowBitmapList.get(2);
    final int leftBitmapW = leftBitmap.getWidth();
    final int leftBitmapH = leftBitmap.getHeight();
    final Bitmap rightBitmap = elementShadowBitmapList.get(3);
    final int rightBitmapW = rightBitmap.getWidth();
    final int rightBitmapH = rightBitmap.getHeight();
    final int westCornerWidth;
    final int eastCornerWidth;
    final int rightEdgeModifier;
    if (rightBitmapW > leftBitmapW) {
      westCornerWidth = rightBitmapW + elementBorderWidth.left;
      eastCornerWidth = rightBitmapW + elementBorderWidth.right;
      rightEdgeModifier = elementBorderWidth.right;
    } else {
      westCornerWidth = leftBitmapW + elementBorderWidth.left;
      eastCornerWidth = leftBitmapW + elementBorderWidth.right;
      rightEdgeModifier = leftBitmapW - rightBitmapW + elementBorderWidth.right;
    }

    if (elementBorderRadius > 0
        && elementBorderRadius + elementBorderRadius >= sleekW - elementBorderWidth.left - elementBorderWidth.right
        && elementBorderRadius + elementBorderRadius >= sleekH
        ) {// Special case for round SleekElement instances, does not draw WEST and EAST bitmaps.
      //DRAW unscaled Top-Bitmap
      canvas.drawBitmap(
          elementShadowBitmapList.get(0),
          -elementShadowRadius + leftOffset,
          -elementShadowRadius + topOffset,
          elementShadowBitmapPaint
      );
      //DRAW unscaled Bottom-Bitmap
      canvas.drawBitmap(
          elementShadowBitmapList.get(1),
          -elementShadowRadius + leftOffset,
          sleekH - elementBorderRadius,
          elementShadowBitmapPaint
      );
    } else {
      if (elementBorderRadius > 0
          && elementBorderRadius + elementBorderRadius >= sleekW - elementBorderWidth.left - elementBorderWidth.right
          ) {

        // Draw unscaled TOP and BOTTOM bitmaps, but WEST and EAST will be drawn in regular way.

        //DRAW unscaled Top-Bitmap
        canvas.drawBitmap(
            elementShadowBitmapList.get(0),
            -elementShadowRadius + leftOffset,
            -elementShadowRadius + topOffset,
            elementShadowBitmapPaint
        );
        //DRAW unscaled Bottom-Bitmap
        canvas.drawBitmap(
            elementShadowBitmapList.get(1),
            -elementShadowRadius + leftOffset,
            sleekH - elementBorderRadius,
            elementShadowBitmapPaint
        );
      } else {
        // Top Bitmap Init
        currentBitmap = elementShadowBitmapList.get(0);
        currentBitmapW = currentBitmap.getWidth();
        currentBitmapH = currentBitmap.getHeight();
        // Top-Left-Corner Bitmap
        shadowSrcRect.set(0, 0, westCornerWidth, currentBitmapH);
        shadowDstRectF.set(
            -elementShadowRadius + leftOffset,
            -elementShadowRadius + topOffset,
            -elementShadowRadius + leftOffset + westCornerWidth,
            -elementShadowRadius + topOffset + currentBitmapH
        );
        canvas.drawBitmap(
            currentBitmap,
            shadowSrcRect,
            shadowDstRectF,
            elementShadowBitmapPaint
        );
        // Top-Right-Corner Bitmap
        shadowSrcRect.set(currentBitmapW - eastCornerWidth, 0, currentBitmapW, currentBitmapH);
        shadowDstRectF.set(
            sleekW - elementBorderRadius - rightEdgeModifier,
            -elementShadowRadius + topOffset,
            sleekW + eastCornerWidth - elementBorderRadius - rightEdgeModifier,
            -elementShadowRadius + topOffset + currentBitmapH
        );
        canvas.drawBitmap(
            currentBitmap,
            shadowSrcRect,
            shadowDstRectF,
            elementShadowBitmapPaint
        );
        // Top-Center Bitmap
        shadowSrcRect.set(westCornerWidth, 0, currentBitmapW - eastCornerWidth, currentBitmapH);
        shadowDstRectF.set(
            -elementShadowRadius + leftOffset + westCornerWidth,
            -elementShadowRadius + topOffset,
            sleekW - elementBorderRadius - rightEdgeModifier,
            -elementShadowRadius + topOffset + currentBitmapH
        );
        canvas.drawBitmap(
            currentBitmap,
            shadowSrcRect,
            shadowDstRectF,
            elementShadowBitmapPaint
        );

        // Bottom Bitmap Init
        currentBitmap = elementShadowBitmapList.get(1);
        currentBitmapW = currentBitmap.getWidth();
        currentBitmapH = currentBitmap.getHeight();
        // Bottom-Left-Corner Bitmap
        shadowSrcRect.set(0, 0, westCornerWidth, currentBitmapH);
        shadowDstRectF.set(
            -elementShadowRadius + leftOffset,
            sleekH - elementBorderRadius,
            -elementShadowRadius + leftOffset + westCornerWidth,
            sleekH - elementBorderRadius + currentBitmapH
        );
        canvas.drawBitmap(
            currentBitmap,
            shadowSrcRect,
            shadowDstRectF,
            elementShadowBitmapPaint
        );
        // Bottom-Right-Corner Bitmap
        shadowSrcRect.set(currentBitmapW - eastCornerWidth, 0, currentBitmapW, currentBitmapH);
        shadowDstRectF.set(
            sleekW - elementBorderRadius - rightEdgeModifier,
            sleekH - elementBorderRadius,
            sleekW + eastCornerWidth - elementBorderRadius - rightEdgeModifier,
            sleekH - elementBorderRadius + currentBitmapH
        );
        canvas.drawBitmap(
            currentBitmap,
            shadowSrcRect,
            shadowDstRectF,
            elementShadowBitmapPaint
        );
        // Bottom-Center Bitmap
        shadowSrcRect.set(westCornerWidth, 0, currentBitmapW - eastCornerWidth, currentBitmapH);
        shadowDstRectF.set(
            -elementShadowRadius + leftOffset + westCornerWidth,
            sleekH - elementBorderRadius,
            sleekW - elementBorderRadius - rightEdgeModifier,
            sleekH - elementBorderRadius + currentBitmapH
        );
        canvas.drawBitmap(
            currentBitmap,
            shadowSrcRect,
            shadowDstRectF,
            elementShadowBitmapPaint
        );
      }

      // Left Bitmap
      currentBitmap = leftBitmap;
      currentBitmapW = leftBitmapW;
      currentBitmapH = leftBitmapH;
      shadowSrcRect.set(0, 0, currentBitmapW, currentBitmapH);
      shadowDstRectF.set(
          -elementShadowRadius + leftOffset,
          elementBorderRadius,
          -elementShadowRadius + leftOffset + currentBitmapW,
          elementBorderRadius + currentBitmapH + stretchedHeightDelta
      );
      canvas.drawBitmap(
          currentBitmap,
          shadowSrcRect,
          shadowDstRectF,
          elementShadowBitmapPaint
      );
      //DEBUG DRAW unscaled Left-Bitmap
//    canvas.drawBitmap(
//        elementShadowBitmapList.get(2),
//        -elementShadowRadius + leftOffset,
//        elementBorderRadius,
//        elementShadowBitmapPaint
//    );

      // Right Bitmap
      currentBitmap = rightBitmap;
      currentBitmapW = rightBitmapW;
      currentBitmapH = rightBitmapH;
      shadowSrcRect.set(0, 0, currentBitmapW, currentBitmapH);
      shadowDstRectF.set(
          sleekW - elementBorderRadius,
          elementBorderRadius,
          sleekW - elementBorderRadius + currentBitmapW,
          elementBorderRadius + currentBitmapH + stretchedHeightDelta
      );
      canvas.drawBitmap(
          currentBitmap,
          shadowSrcRect,
          shadowDstRectF,
          elementShadowBitmapPaint
      );
      //DEBUG DRAW unscaled Right-Bitmap
//    canvas.drawBitmap(
//        elementShadowBitmapList.get(3),
//        sleekW - elementBorderRadius,
//        elementBorderRadius,
//        elementShadowBitmapPaint
//    );
    }



    if (!drawBgRectBeforeShadow) {

      // Draw border-rect
      elementShadowBitmapPaint.setColor(elementBorderColor);
      canvas.drawRoundRect(
          new RectF(
              elementBorderRadius,
              elementBorderRadius,
              sleekW - elementBorderRadius,
              sleekH - elementBorderRadius
          ),
          elementBackground.getRoundedRadius(),
          elementBackground.getRoundedRadius(),
          elementShadowBitmapPaint
      );

      // If background-size==COVER and isBitmapLoaded==true then drawing this results in edge-bleed
      if (!elementBackgroundSizeIsCover
          || elementBackgroundImage == null
          || !elementBackgroundImage.isBitmapLoaded()
          || elementBackgroundImage.getPaint().getAlpha() != 255
          ) {
        // Draw background-rect
        elementShadowBitmapPaint.setColor(elementBackgroundColor);
        canvas.drawRoundRect(
            new RectF(
                elementBorderWidth.left,
                elementBorderWidth.top,
                sleekW - elementBorderWidth.right,
                sleekH - elementBorderWidth.bottom
            ),
            elementBackground.getRoundedRadius(),
            elementBackground.getRoundedRadius(),
            elementShadowBitmapPaint
        );
      }
    }

    //DEBUG DRAW regular background for position reference
//    elementBackground.getPaint().setColor(0x99ffffff);
//    elementBackground.onSleekDraw(canvas, info);
//    elementBackground.getPaint().setColor(elementBackgroundColor);

    return true;
  }

  protected List<Bitmap> generateShadowBitmap() {
    ValPair<Point, List<Bitmap>> shadowWHandShadowBitmap = generateShadowBitmap(
        elementShadowRadius,
        sleekW,
        sleekH,
        elementBorderColor,
        elementBorderRadius,
        elementBackgroundColor,
        elementShadowOffsetX,
        elementShadowOffsetY,
        elementShadowColor
    );
    if (shadowWHandShadowBitmap != null) {
      shadowViewSizeW = shadowWHandShadowBitmap.valueOne.x;
      shadowViewSizeH = shadowWHandShadowBitmap.valueOne.y;
      return shadowWHandShadowBitmap.valueTwo;
    }
    return null;
  }

  protected static ValPair<Point, List<Bitmap>> generateShadowBitmap(
      final float elementShadowRadius,
      final int sleekW,
      final int sleekH,
      final int elementBorderColor,
      final int elementBorderRadius,
      final int elementBackgroundColor,
      final float elementShadowOffsetX,
      final float elementShadowOffsetY,
      final int elementShadowColor
  ) {

    if (elementShadowRadius <= 0) {
      return null;
    }

    //TODO Refactor Shadow Bitmaps:
    //TODO Change so that corners and sides are generated instead of TOP/BOTTOM/LEFT/RIGHT ...
    //TODO ...this way we can have large enough corners to get glow in both X-/Y-axis for when...
    //TODO ...we stretch the shadow. As it is now large offset in Y-axis will result in...
    //TODO ...stretched corner-glow when corner-radius ends outside of TOP or BOTTOM Bitmap.

    //TODO BONUS TASK AFTER "Refactor Shadow Bitmaps":
    //TODO Move out drawShadowOnBitmap() method from generateShadowBitmap() in SleekElement,
    //TODO   so that its easy to customize if you want to draw shadow with RenderScript in a
    //TODO   subclass, or some other totally *WILD* way of drawing shadows!

    /*
    // LOW PRIO since why would we want alpha bg-elements with shadow?
    // If we implement alpha bg with shadow then shadowBitmaps need to contain "hidden" ...
    // ... shadow-edges as well, so that they shine through when y-offset is 100px for on a...
    // ... 400x400px box for example, in that example we want the shadow to be drawn behind...
    // ... background at pos Y==100px !
     */

    final int shadowSleekW = sleekW;
    final int shadowSleekH = sleekH;

//    long timestamp = System.currentTimeMillis();

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.FILL);
    if (elementBorderColor != SleekColorArea.COLOR_TRANSPARENT) {
      paint.setColor(elementBorderColor);
    } else {
      paint.setColor(elementBackgroundColor);
    }
    paint.setShadowLayer(
        elementShadowRadius,
        elementShadowOffsetX,
        elementShadowOffsetY,
        elementShadowColor
    );

    List<Bitmap> returnList = new ArrayList<>(4);
    int bitmapW, bitmapH;
    Bitmap bitmap;
    Canvas canvas;
    final float cornerWidth = elementShadowRadius;
    final float cornerHeight = elementShadowRadius;
    float translateX, translateY;

    //____________________ -START- Top Bitmap ____________________
    bitmapW = (int) (shadowSleekW + cornerWidth + cornerWidth + Math.abs(elementShadowOffsetX));
    bitmapH = (int) (elementBorderRadius + cornerHeight);
    if (elementShadowOffsetY < 0) {
      bitmapH = bitmapH - (int) elementShadowOffsetY;
    }
    if (bitmapW < 1) {
      bitmapW = 1;
    }
    if (bitmapH < 1) {
      bitmapH = 1;
    }
    bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
    bitmap.eraseColor(Color.TRANSPARENT);
    //bitmap.eraseColor(0x66ff00ff);
    canvas = new Canvas(bitmap);
    translateX = 0;
    translateY = 0;
    if (elementShadowOffsetX < 0) {
      translateX = -elementShadowOffsetX;
    }

    if (elementShadowOffsetY < 0) {
      translateY = -elementShadowOffsetY;
    }
    canvas.translate(translateX, translateY);
    canvas.drawRoundRect(
        new RectF(cornerWidth, cornerHeight, cornerWidth + shadowSleekW, cornerHeight + shadowSleekH),
        elementBorderRadius,
        elementBorderRadius,
        paint
    );
    bitmap.prepareToDraw();
    returnList.add(bitmap);
    //____________________ - END - Top Bitmap ____________________

    //____________________ -START- Bottom Bitmap ____________________
    bitmapW = (int) (shadowSleekW + cornerWidth + cornerWidth + Math.abs(elementShadowOffsetX));
    bitmapH = (int) (elementBorderRadius + cornerHeight);
    if (elementShadowOffsetY > 0) {
      bitmapH = bitmapH + (int) elementShadowOffsetY;
    }
    if (bitmapW < 1) {
      bitmapW = 1;
    }
    if (bitmapH < 1) {
      bitmapH = 1;
    }
    bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
    bitmap.eraseColor(Color.TRANSPARENT);
    //bitmap.eraseColor(0x66ff00ff);
    canvas = new Canvas(bitmap);
    if (elementShadowOffsetX < 0) {
      canvas.translate(-elementShadowOffsetX, -shadowSleekH - elementShadowRadius + elementBorderRadius);
    } else {
      canvas.translate(0, -shadowSleekH - elementShadowRadius + elementBorderRadius);
    }
    canvas.drawRoundRect(
        new RectF(cornerWidth, cornerHeight, cornerWidth + shadowSleekW, cornerHeight + shadowSleekH),
        elementBorderRadius,
        elementBorderRadius,
        paint
    );
    bitmap.prepareToDraw();
    returnList.add(bitmap);
    //____________________ - END - Bottom Bitmap ____________________

    //____________________ -START- Left Bitmap ____________________
    bitmapW = (int) (elementBorderRadius + cornerWidth);// - elementShadowOffsetX);
    if (elementShadowOffsetX < 0) {
      bitmapW = bitmapW - (int) elementShadowOffsetX;
    }
    bitmapH = (int) (shadowSleekH - elementBorderRadius - elementBorderRadius);
    if (bitmapW < 1) {
      bitmapW = 1;
    }
    if (bitmapH < 1) {
      bitmapH = 1;
    }
    bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
    bitmap.eraseColor(Color.TRANSPARENT);
    //bitmap.eraseColor(0x99ff0000);
    canvas = new Canvas(bitmap);
    if (elementShadowOffsetX < 0) {
      canvas.translate(-elementShadowOffsetX, -cornerHeight - elementBorderRadius);
    } else {
      canvas.translate(0, -cornerHeight - elementBorderRadius);
    }
    canvas.drawRoundRect(
        new RectF(cornerWidth, cornerHeight, cornerWidth + shadowSleekW, cornerHeight + shadowSleekH),
        elementBorderRadius,
        elementBorderRadius,
        paint
    );
    bitmap.prepareToDraw();
    returnList.add(bitmap);
    //____________________ - END - Left Bitmap ____________________

    //____________________ -START- Right Bitmap ____________________
    bitmapW = (int) (elementBorderRadius + cornerWidth);
    if (elementShadowOffsetX > 0) {
      bitmapW = bitmapW + (int) elementShadowOffsetX;
    }
    bitmapH = (int) (shadowSleekH - elementBorderRadius - elementBorderRadius);
    if (bitmapW < 1) {
      bitmapW = 1;
    }
    if (bitmapH < 1) {
      bitmapH = 1;
    }
    bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
    bitmap.eraseColor(Color.TRANSPARENT);
    //bitmap.eraseColor(0x66000000);
    canvas = new Canvas(bitmap);
    //canvas.translate(0, - elementBorderRadius - elementBorderRadius);
    canvas.translate(
        -shadowSleekW - elementShadowRadius + elementBorderRadius,
        -cornerHeight - elementBorderRadius
    );
    canvas.drawRoundRect(
        new RectF(cornerWidth, cornerHeight, cornerWidth + shadowSleekW, cornerHeight + shadowSleekH),
        elementBorderRadius,
        elementBorderRadius,
        paint
    );
    bitmap.prepareToDraw();
    returnList.add(bitmap);
    //____________________ - END - Right Bitmap ____________________

//    shadowViewSizeW = shadowSleekW;
//    shadowViewSizeH = shadowSleekH;

//    Log.d("SleekElement",
//        "SleekElement | generateShadowBitmap() took: "
//            + (System.currentTimeMillis() - timestamp) + "ms"
//    );

    //return returnList;
    return new ValPair<>(new Point(shadowSleekW, shadowSleekH), returnList);
  }
}
