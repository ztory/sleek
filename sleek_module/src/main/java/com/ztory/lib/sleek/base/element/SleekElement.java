package com.ztory.lib.sleek.base.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBaseComposite;
import com.ztory.lib.sleek.base.SleekColorArea;
import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.base.text.SleekViewText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jonruna on 2017-04-07.
 */
public class SleekElement extends SleekBaseComposite {

    protected static final Executor SLEEK_ELEMENT_EXECUTOR;
    static {
        SLEEK_ELEMENT_EXECUTOR = new ThreadPoolExecutor(
                3,
                3,
                4L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    private final AtomicInteger mCount = new AtomicInteger(1);
                    public Thread newThread(Runnable runnable) {
                        return new Thread(
                                runnable,
                                "SleekElement Thread #" + mCount.getAndIncrement()
                        );
                    }
                }
        );
        if (Build.VERSION.SDK_INT >= 9) {
            ((ThreadPoolExecutor) SLEEK_ELEMENT_EXECUTOR).allowCoreThreadTimeOut(true);
        }
    }

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

    protected boolean activeGenerateShadowTask = false;
    protected Paint elementShadowBitmapPaint;
    protected volatile Bitmap elementShadowBitmap;

    protected int elementBackgroundColor = SleekColorArea.COLOR_TRANSPARENT;
    protected int elementBorderRadius = 0;
    protected float elementShadowRadius = 0;
    protected float elementShadowOffsetX = 0;
    protected float elementShadowOffsetY = 0;
    protected int elementShadowColor = 0;

    public SleekElement(SleekParam sleekParam) {
        super(sleekParam);

        setDimensionIgnoreBounds(true);

        //TODO DO WE NEED TO KEEP elementBackground ELEMENT NON-NULL FOR POSITIONING OF CHILDREN ??

        //TODO KEEP elementBackground, because for SleekElements without a box-shadow it is more...
        //TODO ...lightweight to just draw a SleekColorArea then to generate a bitmap and keep...
        //TODO ...memory for all background-drawing. CREATE a fina elementBackground when...
        //TODO ...SleekElement is instantiated, so that it always exists.

        //TODO HOW TO HANDLE elementBackground.getPaint() and elementShadowBitmapPaint, when...
        //TODO ...someone wants to animate the alpha of the background of this view, which...
        //TODO ... Paint object should be returned? Can we use elementBackground Paint to draw...
        //TODO ...the elementShadowBitmap ????

    }

    private Bitmap generateShadowBitmap() {

        if (elementShadowRadius <= 0) {
            return null;
        }

        //TODO LARGE SleekElements EQUALS LARGE BITMAPS EQUALS PERFORMANCE HIT! Optimize this !!!!

        //TODO MAYBE MOVE BITMAP GENERATION TO BACKGROUND THREAD ????

        long timestamp = System.currentTimeMillis();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(elementBackgroundColor);
        paint.setShadowLayer(elementShadowRadius, elementShadowOffsetX, elementShadowOffsetY, elementShadowColor);

        Bitmap bitmap = Bitmap.createBitmap(
                (int) (sleekW + elementShadowRadius + elementShadowRadius + elementShadowRadius + elementShadowRadius + elementShadowOffsetX),
                (int) (sleekH + elementShadowRadius + elementShadowRadius + elementShadowRadius + elementShadowRadius + elementShadowOffsetY),
                Bitmap.Config.ARGB_8888
        );
        bitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRoundRect(
                new RectF(
                        elementShadowRadius + elementShadowRadius,
                        elementShadowRadius + elementShadowRadius,
                        elementShadowRadius + elementShadowRadius + sleekW,
                        elementShadowRadius + elementShadowRadius + sleekH
                ),
                elementBorderRadius,
                elementBorderRadius,
                paint
        );

        Log.d("SleekElement", "SleekElement | took: " + (System.currentTimeMillis() - timestamp) + "ms");

        return bitmap;
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
            }
            else {
                elementShadowRadius = 0;
            }
        }
        else {
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
            }
            else if (textAlign.equals("right")) {
                elementText.setTextAlignInt(SleekViewText.ALIGN_RIGHT);
            }
            else if (textAlign.equals("left")) {
                elementText.setTextAlignInt(SleekViewText.ALIGN_LEFT);
            }
        }

        String verticalAlign = elementCSS.getVerticalAlign();
        if (verticalAlign != null) {
            createText();
            if (verticalAlign.equals("middle")) {
                elementText.setTextAlignVertInt(SleekViewText.ALIGN_CENTER);
            }
            else if (verticalAlign.equals("bottom")) {
                elementText.setTextAlignVertInt(SleekViewText.ALIGN_BOTTOM);
            }
            else if (verticalAlign.equals("top")) {
                elementText.setTextAlignVertInt(SleekViewText.ALIGN_TOP);
            }
        }

        if (elementString != null) {
            createText();
            elementText.setTextString(elementString);
            elementText.initText();
        }


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

    public SleekColorArea getBackground() {
        return elementBackground;
    }

    protected void createText() {
        if (elementText != null) {
            return;
        }
        elementText = new SleekViewText(SleekParam.DEFAULT);
        elementText.setSleekBounds(0, 0, sleekW, sleekH);
        //elementText.setBackgroundColor(0x66000000);
        //elementText.setTextAlignVertInt(SleekViewText.ALIGN_TOP);
    }

    public SleekViewText getText() {
        return elementText;
    }

    public CSSblock getCSS() {
        return elementCSS;
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        canvas.save();
        canvas.translate(sleekX, sleekY);

        if (elementShadowBitmap != null) {
            canvas.drawBitmap(
                    elementShadowBitmap,
                    -elementShadowRadius - elementShadowRadius,
                    -elementShadowRadius - elementShadowRadius,
                    elementShadowBitmapPaint
            );
        }
        else {
            elementBackground.onSleekDraw(canvas, info);
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

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {
        super.setSleekBounds(x, y, w, h);

        elementBackground.setSleekBounds(0, 0, w, h);

        if (elementText != null) {
            elementText.setSleekBounds(0, 0, w, h);
        }
    }

//    protected void updateShadowBitmap() {
//        if (elementShadowRadius > 0) {
//            elementShadowBitmap = generateShadowBitmap();
//        }
//        else if (elementShadowBitmap != null) {
//            elementShadowBitmap.recycle();
//            elementShadowBitmap = null;
//        }
//    }

    protected void setElementShadowBitmap(Bitmap theShadowBitmap) {
        synchronized (SleekElement.this) {
            if (elementShadowBitmap != null) {
                elementShadowBitmap.recycle();
            }
            elementShadowBitmap = theShadowBitmap;
        }
    }

    protected void unloadElementShadowBitmap() {
        setElementShadowBitmap(null);
    }

    protected void loadElementShadowBitmap() {

        if (elementShadowRadius <= 0) {
            return;
        }

        if (elementShadowBitmap != null) {
            return;
        }

        synchronized (SleekElement.this) {
            if (activeGenerateShadowTask) {
                return;
            }
            activeGenerateShadowTask = true;
        }

        if (elementShadowBitmapPaint == null) {
            elementShadowBitmapPaint = new Paint();
            elementShadowBitmapPaint.setAntiAlias(true);
        }

        SLEEK_ELEMENT_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {

//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                Bitmap shadowBitmap = generateShadowBitmap();

                synchronized (SleekElement.this) {
                    activeGenerateShadowTask = false;
                }

                if (!loaded) {
                    if (shadowBitmap != null) {
                        shadowBitmap.recycle();
                    }
                    return;
                }

                setElementShadowBitmap(shadowBitmap);
                invalidateSafe();
            }
        });
    }

    @Override
    public void onSleekCanvasResize(SleekCanvasInfo info) {

        checkCSS();//checks if changes have been made to CSS properties

        super.onSleekCanvasResize(info);

        unloadElementShadowBitmap();
        loadElementShadowBitmap();
    }

    @Override
    public void onSleekLoad(SleekCanvasInfo info) {
        super.onSleekLoad(info);
        loadElementShadowBitmap();
    }

    @Override
    public void onSleekUnload() {
        super.onSleekUnload();
        unloadElementShadowBitmap();
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

}
