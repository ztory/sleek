package com.ztory.lib.sleek.base.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

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

/**
 * Created by jonruna on 2017-04-07.
 */
public class SleekElement extends SleekBaseComposite {

    protected final List<CSSblock> elementCSSlist = new ArrayList<>(4);

    /** If CSS is updated at runtime, be sure to set this to true. */
    protected boolean elementCSSneedsUpdate = false;

    protected final CSSblock elementCSS = new CSSblockBase(12);
    protected long elementCSSmodifiedTs;

    protected String elementString = null;

    protected SleekColorArea elementBackground = null;
    protected SleekViewText elementText = null;

    protected Paint elementShadowBitmapPaint;
    protected Bitmap elementShadowBitmap;

    protected int elementBackgroundColor = SleekColorArea.COLOR_TRANSPARENT;
    protected float elementShadowRadius = 0;
    protected float elementShadowOffsetX = 0;
    protected float elementShadowOffsetY = 0;
    protected int elementShadowColor = 0;

    public SleekElement(SleekParam sleekParam) {
        super(sleekParam);

        setDimensionIgnoreBounds(true);

        //TODO DO WE NEED TO KEEP elementBackground ELEMENT NON-NULL FOR POSITIONING OF CHILDREN ??
    }

    private Bitmap generateShadowBitmap() {

        elementShadowBitmapPaint = new Paint();
        elementShadowBitmapPaint.setAntiAlias(true);

        //long timestamp = System.currentTimeMillis();

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
        int borderRadius = elementCSS.getBorderRadius();
        canvas.drawRoundRect(
                new RectF(
                        elementShadowRadius + elementShadowRadius,
                        elementShadowRadius + elementShadowRadius,
                        elementShadowRadius + elementShadowRadius + sleekW,
                        elementShadowRadius + elementShadowRadius + sleekH
                ),
                borderRadius,
                borderRadius,
                paint
        );

        //Log.d("SleekElement", "SleekElement | took: " + (System.currentTimeMillis() - timestamp) + "ms");

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
        }

        Integer boxShadowBlurRadius = elementCSS.getBoxShadowBlurRadius();
        if (boxShadowBlurRadius != null) {
            elementShadowRadius = boxShadowBlurRadius;
            elementShadowOffsetX = elementCSS.getBoxShadowOffsetX();
            elementShadowOffsetY = elementCSS.getBoxShadowOffsetY();
            elementShadowColor = elementCSS.getBoxShadowColor();
        }
        else {// Only init elementBackground if boxShadowBlurRadius == null

            if (backgroundColor != null) {
                createBackground();
                elementBackground.getPaint().setColor(backgroundColor);
            }

            Integer borderRadius = elementCSS.getBorderRadius();
            if (borderRadius != null) {
                createBackground();
                elementBackground.getPaint().setAntiAlias(true);
                elementBackground.setRounded(borderRadius);
            }
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

    protected void createBackground() {
        if (elementBackground != null) {
            return;
        }
        elementBackground = new SleekColorArea(
                SleekColorArea.COLOR_TRANSPARENT,
                SleekColorArea.ANTI_ALIASED_FALSE,
                SleekParam.DEFAULT
        );
        elementBackground.setSleekBounds(0, 0, sleekW, sleekH);
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

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {
        super.setSleekBounds(x, y, w, h);

        if (elementBackground != null) {
            elementBackground.setSleekBounds(0, 0, w, h);
        }

        if (elementText != null) {
            elementText.setSleekBounds(0, 0, w, h);
        }
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
        else if (elementBackground != null) {
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
    public void onSleekCanvasResize(SleekCanvasInfo info) {

        checkCSS();//checks if changes have been made to CSS properties

        if (elementShadowRadius > 0) {
            elementShadowBitmap = generateShadowBitmap();
        }
        else if (elementShadowBitmap != null) {
            elementShadowBitmap.recycle();
            elementShadowBitmap = null;
        }

        super.onSleekCanvasResize(info);
    }

    @Override
    public void onSleekLoad(SleekCanvasInfo info) {
        if (elementShadowRadius > 0) {
            elementShadowBitmap = generateShadowBitmap();
        }
        super.onSleekLoad(info);
    }

    @Override
    public void onSleekUnload() {
        super.onSleekUnload();
        if (elementShadowBitmap != null) {
            elementShadowBitmap.recycle();
            elementShadowBitmap = null;
        }
    }

}
