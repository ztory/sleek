package com.ztory.lib.sleek.base.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.base.SleekColorArea;
import com.ztory.lib.sleek.base.SleekParam;

/**
 * Simple SleekBase subclass for displaying text, also has an optional background color.
 * Created by jonruna on 09/10/14.
 */
public class SleekViewText extends SleekBase {

    public static final int
            ALIGN_LEFT = SleekText.ALIGN_LEFT,
            ALIGN_RIGHT = SleekText.ALIGN_RIGHT,
            ALIGN_CENTER = SleekText.ALIGN_CENTER,
            ALIGN_TOP = 3,
            ALIGN_BOTTOM = 4;

    protected static final int
            TEXT_SIZE_DEFAULT = 24,
            BG_COLOR_HIDDEN = 0x00000000;

    protected SleekColorArea mSleekColorArea;
    protected SleekText mSleekText;

    protected boolean
            bgInitialized = false,
            textInitialized = false,
            textInitializedSize = false,
            mWrapWidth = false,
            mWrapHeight = false;

    protected int mMaxWrapWidth = -1, mMaxWrapHeight = -1;

    protected String textString = "";
    protected int textColor = 0xff000000;
    protected float textSize = TEXT_SIZE_DEFAULT;
    protected float textLineHeight = SleekText.LINE_HEIGHT_DYNAMIC;
    protected Typeface textTypeface = Typeface.DEFAULT;
    protected int textAlignInt = ALIGN_LEFT;
    protected int textAlignVertInt = ALIGN_CENTER;

    protected FontMetrics fontMetrics = null;

//    public SleekViewText() {
//        this(
//                SleekViewText.FIXED_POSITION_FALSE,
//                SleekViewText.TOUCHABLE_FALSE,
//                SleekViewText.LOADABLE_FALSE,
//                SleekViewText.TOUCH_PRIO_DEFAULT
//        );
//    }

    public SleekViewText(SleekParam sleekParam) {
        this(sleekParam.fixed, sleekParam.touchable, sleekParam.loadable, sleekParam.priority);
    }

    public SleekViewText(
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio
    ) {
        super(isFixedPosition, isTouchable, isLoadable, theTouchPrio);

        mSleekColorArea = new SleekColorArea(
                BG_COLOR_HIDDEN,
                SleekColorArea.ANTI_ALIASED_FALSE,
                SleekColorArea.FIXED_POSITION_FALSE,
                SleekColorArea.TOUCHABLE_FALSE,
                isLoadable,
                0
        );

        mSleekText = new SleekText(isLoadable);
        mSleekText.setTextDrawCareAboutBounds(false);
    }

    public SleekViewText setWrapWidth(boolean theWrapWidth) {
        mWrapWidth = theWrapWidth;
        return this;
    }

    public SleekViewText setWrapHeight(boolean theWrapHeight) {
        mWrapHeight = theWrapHeight;
        return this;
    }

    public void setMaxWrapWidth(int theMaxWrapWidth) {
        mMaxWrapWidth = theMaxWrapWidth;
    }

    public void setMaxWrapHeight(int theMaxWrapHeight) {
        mMaxWrapHeight = theMaxWrapHeight;
    }

    /**
     * Set bounds of SleekViewText, the text and background will be within these bounds.
     * @param x coordinate
     * @param y coordinate
     * @param w size
     * @param h size
     */
    @Override
    public void setSleekBounds(float x, float y, int w, int h) {

        if (mMaxWrapWidth == -1) {
            setMaxWrapWidth(w);
        }

        if (mMaxWrapHeight == -1) {
            setMaxWrapHeight(h);
        }

        innerSetSleekPosAndSize(x, y, w, h, true);
    }

    protected void innerSetSleekPosAndSize(float x, float y, int w, int h, boolean checkWrappedBounds) {
        super.setSleekBounds(x, y, w, h);

        mSleekColorArea.setSleekBounds(x, y, w, h);

        refreshTextBounds(checkWrappedBounds);
    }

    protected void refreshTextBounds(boolean checkWrappedBounds) {

        if (!textInitialized) {
            return;
        }

        if (checkWrappedBounds) {
            mSleekText.setMaxSize(sleekW, sleekH);
        }

        //http://stackoverflow.com/questions/27631736/meaning-of-top-ascent-baseline-descent-bottom-and-leading-in-androids-font
        float textTop = fontMetrics.top;
        float textBottom = fontMetrics.bottom;

        //Fallback textLineHeight to be equal to textSize
        if (textLineHeight == SleekText.LINE_HEIGHT_DYNAMIC) {
            textLineHeight = textSize;
        }

//        Log.d("SleekViewText",
//                "SleekViewText" +
//                " | textTop: " + textTop +
//                " | textBottom: " + textBottom
//        );

        //________________ -START- calc Y ________________
        float calcTextY;
        int linesAboveOne = mSleekText.getLineCount() - 1;
        if (textAlignVertInt == ALIGN_TOP) {
            int topAscentDiff = Math.round(Math.abs(fontMetrics.top) - Math.abs(fontMetrics.ascent));
            calcTextY = sleekY + (textLineHeight / 2.0f) + fontMetrics.bottom + topAscentDiff;
        }
        else if (textAlignVertInt == ALIGN_BOTTOM) {
            calcTextY = sleekY + sleekH - textBottom;

            if (linesAboveOne > 0) {
                calcTextY -= (textLineHeight) * linesAboveOne;
            }
        }
        else {// center text inside of sleekH
            calcTextY = sleekY + Math.round((sleekH - textTop - textBottom) / 2.0f);

            if (linesAboveOne > 0) {
                calcTextY -= Math.round((textLineHeight / 2.0f) * linesAboveOne);
            }
        }
        //________________ - END - calc Y ________________

        //________________ -START- calc X ________________
        float calcTextX = sleekX;

        if (textAlignInt == ALIGN_CENTER) {
            calcTextX = sleekX + Math.round(sleekW / 2.0f);
        }
        else if (textAlignInt == ALIGN_RIGHT) {
            calcTextX = sleekX + sleekW;
        }
        //________________ - END - calc X ________________

        mSleekText.setSleekBounds(
                calcTextX,
                calcTextY,
                0,//dont update size, calling setMaxSize() above
                0//dont update size, calling setMaxSize() above
        );

        if (
                checkWrappedBounds &&
                (mWrapWidth || mWrapHeight)
                ) {

            float wrapX = getSleekX();
            float wrapY = getSleekY();
            int wrapW = getSleekW();
            int wrapH = getSleekH();

            if (mWrapWidth) {
                wrapW = mSleekText.getSleekW();
            }

            if (mWrapHeight) {
                wrapH = mSleekText.getSleekH();

                if (wrapH < textLineHeight) {
                    wrapH = (int) textLineHeight;
                }
            }

            if (textAlignInt == ALIGN_LEFT) {

            }
            else if (textAlignInt == ALIGN_RIGHT) {

            }
            else {//ALIGN_CENTER

            }

            innerSetSleekPosAndSize(
                    wrapX,
                    wrapY,
                    wrapW,
                    wrapH,
                    false
            );
        }

        textInitializedSize = true;
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        if (bgInitialized) {
            mSleekColorArea.drawView(mSleekColorArea, canvas, info);
        }

        if (textInitializedSize) {
            mSleekText.drawRaw(canvas, info);
        }
    }

    /**
     * Sets the background color of this view,
     * mainly used to see that the bounds of this view are correct.
     * @param color
     */
    public void setBackgroundColor(int color) {
        mSleekColorArea.getPaint().setColor(color);

        bgInitialized = color != BG_COLOR_HIDDEN;
    }

    public Paint getBackgroundPaint() {
        return mSleekColorArea.getPaint();
    }

    /**
     * Call this when you want to commit changes to the SleekViewText's text.
     * For example if you want to change the text you call setTextString() and then initText().
     */
    public void initText() {
        mSleekText.prepareText(
                textString,
                textColor,
                textSize,
                textLineHeight,
                textTypeface,
                textAlignInt,
                SleekText.ANTI_ALIAS_ON,
                SleekText.BITMAP_CACHE_OFF
        );

        fontMetrics = mSleekText.getTextPaint().getFontMetrics();

        textInitialized = true;

        if (textInitializedSize) {
            innerSetSleekPosAndSize(
                    getSleekX(),
                    getSleekY(),
                    mMaxWrapWidth,
                    mMaxWrapHeight,
                    true
            );
        }

        requestLayout();
    }

    public String getTextString() {
        return textString;
    }

    public SleekViewText setTextString(String theTextString) {
        if (theTextString != null) {
            textString = theTextString;
        } else {
            textString = "";
        }
        return this;
    }

    public int getTextColor() {
        return textColor;
    }

    public SleekViewText setTextColor(int theTextColor) {
        textColor = theTextColor;
        return this;
    }

    public float getTextSize() {
        return textSize;
    }

    public SleekViewText setTextSize(float theTextSize) {
        textSize = theTextSize;
        return this;
    }

    public float getTextLineHeight() {
        return textLineHeight;
    }

    public SleekViewText setTextLineHeight(float theTextLineHeight) {
        textLineHeight = theTextLineHeight;
        return this;
    }

    public Typeface getTextTypeface() {
        return textTypeface;
    }

    public SleekViewText setTextTypeface(Typeface theTextTypeface) {
        textTypeface = theTextTypeface;
        return this;
    }

    public int getTextAlignInt() {
        return textAlignInt;
    }

    public SleekViewText setTextAlignInt(int theTextAlignInt) {
        textAlignInt = theTextAlignInt;
        return this;
    }

    public int getTextAlignVertInt() {
        return textAlignVertInt;
    }

    public SleekViewText setTextAlignVertInt(int theTextAlignVertInt) {
        textAlignVertInt = theTextAlignVertInt;
        return this;
    }

    @Override
    public void onSleekLoad(SleekCanvasInfo info) {
        super.onSleekLoad(info);

        mSleekColorArea.onSleekLoad(info);
        mSleekText.onSleekLoad(info);
    }

    @Override
    public void onSleekUnload() {
        super.onSleekUnload();

        mSleekColorArea.onSleekUnload();
        mSleekText.onSleekUnload();
    }

    public Paint getTextPaint() {
        return mSleekText.getTextPaint();
    }
}
