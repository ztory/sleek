package com.ztory.lib.sleek.base.element;

import android.graphics.Canvas;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBaseComposite;
import com.ztory.lib.sleek.base.SleekColorArea;
import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.base.element.css.CSSblock;
import com.ztory.lib.sleek.base.text.SleekViewText;
import com.ztory.lib.sleek.util.UtilPx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonruna on 2017-04-07.
 */
public class SleekElement extends SleekBaseComposite {

    protected final List<CSSblock> elementCSSlist = new ArrayList<>(4);

    /** If CSS is updated at runtime, be sure to set this to true. */
    protected boolean elementCSSneedsUpdate = false;

    protected final CSSblock elementCSS = new CSSblock(12);
    protected long elementCSSmodifiedTs;

    protected String elementString = null;

    protected SleekColorArea elementBackground = null;
    protected SleekViewText elementText = null;

    public SleekElement(SleekParam sleekParam) {
        super(sleekParam);
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
            createBackground();
            elementBackground.getPaint().setColor(backgroundColor);
        }

        Integer borderRadius = elementCSS.getBorderRadius();
        if (borderRadius != null) {
            createBackground();
            elementBackground.getPaint().setAntiAlias(true);
            elementBackground.setRounded(UtilPx.getPixels(mSlkCanvas.getContext(), borderRadius));
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
        elementBackground.setSleekBounds(sleekX, sleekY, sleekW, sleekH);
    }

    public SleekColorArea getBackground() {
        return elementBackground;
    }

    protected void createText() {
        if (elementText != null) {
            return;
        }
        elementText = new SleekViewText(SleekParam.DEFAULT);
        elementText.setSleekBounds(sleekX, sleekY, sleekW, sleekH);
    }

    public SleekViewText getText() {
        return elementText;
    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {
        super.setSleekBounds(x, y, w, h);

        if (elementBackground != null) {
            elementBackground.setSleekBounds(x, y, w, h);
        }

        if (elementText != null) {
            elementText.setSleekBounds(x, y, w, h);
        }
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        canvas.save();
        canvas.translate(sleekX, sleekY);

        if (elementBackground != null) {
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
        super.onSleekCanvasResize(info);
    }

}
