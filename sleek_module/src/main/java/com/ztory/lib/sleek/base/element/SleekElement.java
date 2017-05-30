package com.ztory.lib.sleek.base.element;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBaseComposite;
import com.ztory.lib.sleek.base.SleekColorArea;
import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.base.element.css.CSS;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.base.image.SleekBaseImage;
import com.ztory.lib.sleek.base.text.SleekViewText;
import com.ztory.lib.sleek.contract.ISleekCallback;
import com.ztory.lib.sleek.contract.ISleekData;
import com.ztory.lib.sleek.layout.IComputeInt;
import com.ztory.lib.sleek.layout.SL;
import com.ztory.lib.sleek.util.Calc;
import com.ztory.lib.sleek.util.UtilDownload;
import com.ztory.lib.sleek.util.UtilResources;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * NOTE:
 * Styling and layout in SleekElement is calculated as if box-sizing: border-box; is set in CSS.
 * Created by jonruna on 2017-04-07.
 */
public class SleekElement extends SleekBaseComposite {

  protected final List<CSSblock> elementCSSlist = new ArrayList<>(4);

  /** If CSS is updated at runtime, be sure to set this to true. */
  protected boolean elementCSSneedsUpdate = false;

  protected final CSSblock elementCSS = new CSSblockBase(12);
  protected long elementCSSmodifiedTs;

  protected String elementString = null;

  protected final SleekColorArea elementBackground = new SleekColorArea(
      SleekColorArea.COLOR_TRANSPARENT,
      SleekColorArea.ANTI_ALIASED_FALSE,
      SleekParam.DEFAULT_TOUCHABLE
  );
  protected SleekViewText elementText = null;

  protected Paint elementShadowBitmapPaint;
  protected final List<Bitmap> elementShadowBitmapList = new ArrayList<>();

  protected int elementBackgroundColor = SleekColorArea.COLOR_TRANSPARENT;
  protected int elementBorderRadius = 0;
  protected float elementShadowRadius = 0;
  protected float elementShadowOffsetX = 0;
  protected float elementShadowOffsetY = 0;
  protected int elementShadowColor = 0;

  protected Rect paddingRect;

  protected String elementBackgroundSize = null;

  protected boolean localElementBackgroundImageUrl;
  protected String elementBackgroundImageUrl = null;
  protected SleekBaseImage elementBackgroundImage = null;

  protected boolean wrapTextWidth = false, wrapTextHeight = false;

  public SleekElement(SleekParam sleekParam) {
    super(sleekParam);
    setDimensionIgnoreBounds(true);
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

  public void checkCSS() {

    if (!isAddedToParent()) {
      return;//do not apply css if element does not have a parent
    }

    // Add CSS blocks from elementCSSlist to elementCSS
    if (elementCSSneedsUpdate) {
      elementCSSneedsUpdate = false;
      if (elementCSS.size() > 0) {
        elementCSS.clear();
      }
      for (CSSblock iterBlock : elementCSSlist) {
        elementCSS.putAll(iterBlock);//will overwrite existing keys
      }
    }

    // Apply new CSS if state in elementCSS has changed
    if (elementCSSmodifiedTs != elementCSS.getModifiedTimestamp()) {
      elementCSSmodifiedTs = elementCSS.getModifiedTimestamp();
      applyCSS();
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
      elementBackground.getPaint().setColor(backgroundColor);
    }

    Integer borderRadius = elementCSS.getBorderRadius();
    if (borderRadius != null) {
      elementBorderRadius = borderRadius;
      elementBackground.getPaint().setAntiAlias(true);
      elementBackground.setRounded(borderRadius);
    }

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

    if (elementString != null) {
      createText();
      elementText.setTextString(elementString);
      elementText.initText();
    }

    paddingRect = elementCSS.getPadding();

    String oldElementBackgroundImageUrl = elementBackgroundImageUrl;
    elementBackgroundImageUrl = elementCSS.getBackgroundImage();
    if (elementBackgroundImageUrl != null) {

      createBackgroundImage();

      if (!elementBackgroundImageUrl.equals(oldElementBackgroundImageUrl)) {
        localElementBackgroundImageUrl = !elementBackgroundImageUrl.startsWith("https://")
            && !elementBackgroundImageUrl.startsWith("http://");

        reloadBackgroundImage();
      }
    } else if (elementBackgroundImage != null) {
      initBackgroundImageBitmapFetcher();
      elementBackgroundImage.setBitmap(null);
    }

    elementBackgroundSize = elementCSS.getBackgroundSize();
  }

  public void setCSSneedsUpdate() {
    elementCSSneedsUpdate = true;
    requestLayout();
  }

  public SleekElement addCSSblock(CSSblock cssBlock) {
    elementCSSlist.add(cssBlock);
    setCSSneedsUpdate();
    return this;
  }

  public SleekElement removeCSSblock(CSSblock cssBlock) {
    elementCSSlist.remove(cssBlock);
    setCSSneedsUpdate();
    return this;
  }

  public void setElementString(String theElementString) {
    elementString = theElementString;
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
    if (elementBackgroundImage != null) {
      return;
    }

    elementBackgroundImage = new SleekBaseImage(elementBorderRadius, SleekParam.DEFAULT);
    elementBackgroundImage.setFadeAnimOnLoad(false);
    elementBackgroundImage.setBitmapListener(new ISleekCallback<SleekBaseImage>() {
      @Override public void sleekCallback(SleekBaseImage sleekBaseImage) {
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

    // Reset source rect
    elementBackgroundImage.getSourceRect().set(0, 0, backgroundWidth, backgroundHeight);
    elementBackgroundImage.setShaderMatrix();

    if (elementBackgroundSize != null) {

      if (elementBackgroundSize.equals(CSS.Value.CONTAIN)) {
                /*
                contain
                A keyword that scales the image as large as possible and maintains image aspect
                ratio (image doesn't get squished). Image is letterboxed within the container.
                When the image and container have different dimensions, the empty
                areas (either top/bottom of left/right) are filled with the background-color.
                 */
        float elementRatio = Calc.divide(sleekW, sleekH);
        float bitmapRatio = Calc.divide(backgroundWidth, backgroundHeight);
        if (bitmapRatio > elementRatio) {
          backgroundWidth = sleekW;
          backgroundHeight = Calc.divideToInt(sleekW, bitmapRatio);
          backgroundY = Calc.divideToInt(sleekH - backgroundHeight, 2.0f);
        } else {
          backgroundHeight = sleekH;
          backgroundWidth = Calc.multiplyToInt(sleekH, bitmapRatio);
          backgroundX = Calc.divideToInt(sleekW - backgroundWidth, 2.0f);
        }
      } else if (elementBackgroundSize.equals(CSS.Value.COVER)) {
                /*
                cover
                A keyword that is the inverse of contain.
                Scales the image as large as possible and maintains image aspect
                ratio (image doesn't get squished). The image "covers" the entire
                width or height of the container. When the image and container have
                different dimensions, the image is clipped either left/right or top/bottom.
                 */
        float bitmapScale;
        float elementRatio = Calc.divide(sleekW, sleekH);
        float bitmapRatio = Calc.divide(backgroundWidth, backgroundHeight);
        if (bitmapRatio > elementRatio) {
          bitmapScale = Calc.divide(sleekH, bitmapH);
          //backgroundHeight = sleekH;
          backgroundWidth = Calc.multiplyToInt(sleekH, bitmapRatio);
          backgroundX = Calc.divideToInt(sleekW - backgroundWidth, 2.0f);
        } else {
          bitmapScale = Calc.divide(sleekW, bitmapW);
          //backgroundWidth = sleekW;
          backgroundHeight = Calc.divideToInt(sleekW, bitmapRatio);
          backgroundY = Calc.divideToInt(sleekH - backgroundHeight, 2.0f);
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
        backgroundWidth = sleekW;
        backgroundHeight = sleekH;
      }
    }

    if (backgroundX > elementBorderRadius || backgroundY > elementBorderRadius) {
      elementBackgroundImage.setRoundedRadius(0);
    } else {
      elementBackgroundImage.setRoundedRadius(elementBorderRadius);
    }

    elementBackgroundImage.setSleekBounds(
        backgroundX, backgroundY, backgroundWidth, backgroundHeight
    );
  }

  public void reloadBackgroundImage() {

    initBackgroundImageBitmapFetcher();

    if (elementBackgroundImageUrl != null) {
      if (loaded && addedToParent) {
        mSlkCanvas.loadSleek(elementBackgroundImage);
        checkSetLocalBackground();
      }
    }
  }

  public void initBackgroundImageBitmapFetcher() {
    if (elementBackgroundImageUrl == null || localElementBackgroundImageUrl) {
      elementBackgroundImage.setBitmapFetcher(null, null, null);//clear fetcher
    } else {
      elementBackgroundImage.setBitmapFetcher(mSlkCanvas.getHandler(), UtilDownload.EXECUTOR,
          new ISleekData<Bitmap>() {
            @Override public Bitmap getData(Sleek sleek) {
              File bmFile = UtilDownload.downloadUrl(elementBackgroundImageUrl);
              Bitmap bm = null;
              if (bmFile != null) {
                try {
                  bm = BitmapFactory.decodeStream(new FileInputStream(bmFile));
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
              return bm;
            }
          });
    }
  }

  public void checkSetLocalBackground() {

    if (elementBackgroundImageUrl != null && localElementBackgroundImageUrl) {

      // DEBUG CODE, because we have no drawable resources in sleek library.
      //            int resId = UtilPx.getDefaultContext().getResources().getIdentifier(
      //                    elementBackgroundImageUrl,
      //                    "drawable",
      //                    "android"
      //            );
      //            elementBackgroundImage.setBitmap(UtilResources.getBitmap(resId));

      Bitmap bitmapResource = UtilResources.getBitmap(
          UtilResources.getResourceIdDrawable(elementBackgroundImageUrl)
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
    getBackgroundImage().setBitmapListener(new ISleekCallback<SleekBaseImage>() {
      @Override public void sleekCallback(SleekBaseImage sleekBaseImage) {

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
              @Override public int compute(SleekCanvasInfo info) {
                int computedH = SleekElement.this.getLayout().computeSizeH(SleekElement.this, info);
                return Calc.multiplyToInt(computedH, bitmapRatio);
              }
            });
          } else if (wrapHeight) {
            SleekElement.this.getLayout().h(SL.H.COMPUTE, 0, null, 0, new IComputeInt() {
              @Override public int compute(SleekCanvasInfo info) {
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

  @Override public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

    canvas.save();
    canvas.translate(sleekX, sleekY);

    if (!drawShadowBitmap(canvas, info)) {
      elementBackground.onSleekDraw(canvas, info);
    }

    if (elementBackgroundImage != null) {
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

  @Override public void setSleekBounds(float x, float y, int w, int h) {

    final int oldW = sleekW;
    final int oldH = sleekH;

    if (elementText != null) {
      if (paddingRect != null) {
        elementText.setSleekBounds(
            paddingRect.left,
            paddingRect.top,
            w - paddingRect.left - paddingRect.right,
            h - paddingRect.top - paddingRect.bottom
        );
      } else {
        elementText.setSleekBounds(0, 0, w, h);
      }
    }

    int elementWidth = w;
    int elementHeight = h;

    if ((wrapTextWidth || wrapTextHeight) && elementText != null) {

      int paddingTopBottom = paddingRect != null ? paddingRect.top + paddingRect.bottom : 0;
      int paddingLeftRight = paddingRect != null ? paddingRect.left + paddingRect.right : 0;

      if (wrapTextWidth) {//wrap only width
        elementWidth = elementText.getSleekW() + paddingLeftRight;
      }

      if (wrapTextHeight) {//wrap only height
        elementHeight = elementText.getSleekH() + paddingTopBottom;
      }
    }

    super.setSleekBounds(x, y, elementWidth, elementHeight);
    elementBackground.setSleekBounds(0, 0, elementWidth, elementHeight);

    if (loaded && addedToParent) {

      if (elementShadowRadius > 0) {
        if (elementShadowBitmapList.size() == 0 || sleekW != oldW || sleekH != oldH) {
          setElementShadowBitmap(generateShadowBitmap());
        }
      }

      positionBackgroundImage();
    }
  }

  protected void setElementShadowBitmap(List<Bitmap> theShadowBitmapList) {
    synchronized (SleekElement.this) {

      if (elementShadowBitmapList.size() > 0) {
        for (Bitmap iterShadowBitmap : elementShadowBitmapList) {
          if (iterShadowBitmap != null) {
            iterShadowBitmap.recycle();
          }
        }
        elementShadowBitmapList.clear();
      }

      if (theShadowBitmapList != null) {
        elementShadowBitmapList.addAll(theShadowBitmapList);
      }
    }
  }

  @Override public void onSleekCanvasResize(SleekCanvasInfo info) {

    checkCSS();//checks if changes have been made to CSS properties

    super.onSleekCanvasResize(info);
  }

  @Override public void onSleekLoad(SleekCanvasInfo info) {
    super.onSleekLoad(info);
    setElementShadowBitmap(generateShadowBitmap());
    if (elementBackgroundImage != null) {
      elementBackgroundImage.onSleekLoad(info);
      checkSetLocalBackground();
    }
  }

  @Override public void onSleekUnload() {
    super.onSleekUnload();
    setElementShadowBitmap(null);
    if (elementBackgroundImage != null) {
      elementBackgroundImage.onSleekUnload();
    }
  }

  @Override public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo info) {

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

  protected boolean drawShadowBitmap(Canvas canvas, SleekCanvasInfo info) {

    //TODO Add boolean that keeps track of if (elementShadowBitmapList.size() < 4) !

    if (elementShadowBitmapList.size() < 4) {
      return false;
    }

    //TODO FIX SO THAT ALPHA ANIMATION WORKS AS IT SHOULD WITH MIDDLE AREA AND BORDER AREAS !!!!
    //TODO Maybe we can set bitmap-bg-paint to alpha==255, and shadow to delta of real...
    //TODO ... bg-alpha and its shadow-alpha ????
    //TODO NEED TO add interface that is called HasAlpha and has getAlpha and setAlpha methods.

    canvas.drawRect(
        elementBorderRadius,
        elementBorderRadius,
        sleekW - elementBorderRadius,
        sleekH - elementBorderRadius,
        elementShadowBitmapPaint
    );

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

    float leftOffset = 0;
    if (elementShadowOffsetX < 0) {
      leftOffset = elementShadowOffsetX;
    }

    float topOffset = 0;
    if (elementShadowOffsetY < 0) {
      topOffset = elementShadowOffsetY;
    }

    // Top Bitmap
    canvas.drawBitmap(
        elementShadowBitmapList.get(0),
        -elementShadowRadius + leftOffset,
        -elementShadowRadius + topOffset,
        elementShadowBitmapPaint
    );

    // Bottom Bitmap
    canvas.drawBitmap(
        elementShadowBitmapList.get(1),
        -elementShadowRadius + leftOffset,
        sleekH - elementBorderRadius,
        elementShadowBitmapPaint
    );

    // Left Bitmap
    canvas.drawBitmap(
        elementShadowBitmapList.get(2),
        -elementShadowRadius + leftOffset,
        elementBorderRadius,
        elementShadowBitmapPaint
    );

    // Right Bitmap
    canvas.drawBitmap(
        elementShadowBitmapList.get(3),
        sleekW - elementBorderRadius,
        elementBorderRadius,
        elementShadowBitmapPaint
    );

    //DEBUG Draw regular background for position reference
    //        elementBackground.getPaint().setColor(0x99ffffff);
    //        elementBackground.onSleekDraw(canvas, info);
    //        elementBackground.getPaint().setColor(elementBackgroundColor);

    return true;
  }

  protected List<Bitmap> generateShadowBitmap() {

    if (elementShadowRadius <= 0) {
      return null;
    }

        /*
        // LOW PRIO since why would we want alpha bg-elements with shadow?
        // If we implement alpha bg with shadow then shadowBitmaps need to contain "hidden" ...
        // ... shadow-edges as well, so that they shine through when y-offset is 100px for on a...
        // ... 400x400px box for example, in that example we want the shadow to be drawn behind...
        // ... background at pos Y==100px !
         */

    long timestamp = System.currentTimeMillis();

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(elementBackgroundColor);
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
    bitmapW = (int) (sleekW + cornerWidth + cornerWidth + Math.abs(elementShadowOffsetX));
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
        new RectF(cornerWidth, cornerHeight, cornerWidth + sleekW, cornerHeight + sleekH),
        elementBorderRadius,
        elementBorderRadius,
        paint
    );
    returnList.add(bitmap);
    //____________________ - END - Top Bitmap ____________________

    //____________________ -START- Bottom Bitmap ____________________
    bitmapW = (int) (sleekW + cornerWidth + cornerWidth + Math.abs(elementShadowOffsetX));
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
      canvas.translate(-elementShadowOffsetX, -sleekH - elementShadowRadius + elementBorderRadius);
    } else {
      canvas.translate(0, -sleekH - elementShadowRadius + elementBorderRadius);
    }
    canvas.drawRoundRect(
        new RectF(cornerWidth, cornerHeight, cornerWidth + sleekW, cornerHeight + sleekH),
        elementBorderRadius,
        elementBorderRadius,
        paint
    );
    returnList.add(bitmap);
    //____________________ - END - Bottom Bitmap ____________________

    //____________________ -START- Left Bitmap ____________________
    bitmapW = (int) (elementBorderRadius + cornerWidth);// - elementShadowOffsetX);
    if (elementShadowOffsetX < 0) {
      bitmapW = bitmapW - (int) elementShadowOffsetX;
    }
    bitmapH = (int) (sleekH - elementBorderRadius - elementBorderRadius);
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
        new RectF(cornerWidth, cornerHeight, cornerWidth + sleekW, cornerHeight + sleekH),
        elementBorderRadius,
        elementBorderRadius,
        paint
    );
    returnList.add(bitmap);
    //____________________ - END - Left Bitmap ____________________

    //____________________ -START- Right Bitmap ____________________
    bitmapW = (int) (elementBorderRadius + cornerWidth);
    if (elementShadowOffsetX > 0) {
      bitmapW = bitmapW + (int) elementShadowOffsetX;
    }
    bitmapH = (int) (sleekH - elementBorderRadius - elementBorderRadius);
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
        -sleekW - elementShadowRadius + elementBorderRadius,
        -cornerHeight - elementBorderRadius
    );
    canvas.drawRoundRect(
        new RectF(cornerWidth, cornerHeight, cornerWidth + sleekW, cornerHeight + sleekH),
        elementBorderRadius,
        elementBorderRadius,
        paint
    );
    returnList.add(bitmap);
    //____________________ - END - Right Bitmap ____________________

    Log.d("SleekElement",
        "SleekElement | took: " + (System.currentTimeMillis() - timestamp) + "ms");

    return returnList;
  }
}
