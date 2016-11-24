package com.ztory.lib.sleek.base.text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.SleekParent;
import com.ztory.lib.sleek.contract.ISleekResize;
import com.ztory.lib.sleek.layout.SleekLayout;

import java.util.ArrayList;

/**
 * Class for working with and displaying text in the Sleek-framework.
 * Created by jonruna on 09/10/14.
 */
public class SleekText implements Sleek {

    public static final int
            ALIGN_LEFT = 0,
            ALIGN_RIGHT = 1,
            ALIGN_CENTER = 2,
            MAX_WIDTH_UNLIMITED = -1,
            MAX_HEIGHT_UNLIMITED = -1,
            LINE_HEIGHT_DYNAMIC = -1;

    public static final boolean
            ANTI_ALIAS_ON = true,
            ANTI_ALIAS_OFF = false,
            BITMAP_CACHE_ON = true,
            BITMAP_CACHE_OFF = false;

    protected ISleekResize canvasResize;

    protected boolean useBitmapCache;
    protected boolean updatedBitmapCache;
    protected Bitmap bitmapCache;

    protected float[] drawLinesY = new float[0];
    protected String[] stringLines;
    protected final Object stringLinesLock = new Object();
    protected String textString;
    protected int textAlignInt;
    protected int maxWidth, maxHeight;
    protected float relativeMaxHeight;
    protected float fixedLineHeight;

    protected float posX, posY;

    protected Paint textPaint;

    protected RectF bounds = new RectF();

    protected float lineHeight;

    protected boolean sleekLoadable, sleekLoaded;

    public SleekText() {
        this(false);
    }

    public SleekText(boolean theSleekLoadable) {
        sleekLoadable = theSleekLoadable;
    }

    protected static Align getAlign(int alignInt) {
        switch (alignInt) {
        case ALIGN_LEFT:
            return Align.LEFT;
        case ALIGN_RIGHT:
            return Align.RIGHT;
        case ALIGN_CENTER:
            return Align.CENTER;
        }
        return Align.RIGHT;
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    public void setText(
            String theTextString,
            int textColor,
            int theMaxWidth,
            int theMaxHeight,
            float textSize,
            float theFixedLineHeight,
            Typeface tf,
            int theTextAlignInt,
            boolean antiAlias,
            boolean theUseBitmapCache
            ) {

        prepareText(
                theTextString,
                textColor,
                textSize,
                theFixedLineHeight,
                tf,
                theTextAlignInt,
                antiAlias,
                theUseBitmapCache
        );

        setMaxHeight(theMaxHeight);
        setMaxWidth(theMaxWidth);
    }

    public void prepareText(
            String theTextString,
            int textColor,
            float textSize,
            float theFixedLineHeight,
            Typeface tf,
            int theTextAlignInt,
            boolean antiAlias,
            boolean theUseBitmapCache
    ) {
        useBitmapCache = theUseBitmapCache;
        updatedBitmapCache = false;
        bitmapCache = null;

        textString = theTextString;

        if (textString == null) {
            textString = "";
        }

        textAlignInt = theTextAlignInt;

        fixedLineHeight = theFixedLineHeight;

        textPaint = new Paint();
        if (tf != null) textPaint.setTypeface(tf);
        textPaint.setAntiAlias(antiAlias);
        textPaint.setTextAlign(getAlign(textAlignInt));
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
    }

    protected boolean isLegalDimension(int dimension) {
        return dimension != 0;//need to allow -1 value
    }

    protected String lastLegalString;

    public boolean setMaxSize(int theMaxWidth, int theMaxHeight) {

        if (
                textString.equals(lastLegalString) &&
                maxWidth == theMaxWidth &&
                maxHeight == theMaxHeight
                ) {
            return false;//no need for computation if it is the same dimensions
        }

        if (
                !isLegalDimension(theMaxWidth) ||
                !isLegalDimension(theMaxHeight)
                ) {
            return false;//block setting values to 0 or below
        }

        lastLegalString = textString;

        setMaxHeight(theMaxHeight);
        setMaxWidth(theMaxWidth);

        return true;
    }

    protected void setMaxHeight(int theMaxHeight) {

        if (!isLegalDimension(theMaxHeight)) {
            return;//block setting values to 0 or below
        }

        maxHeight = theMaxHeight;
        textDrawCheckHeight = maxHeight > 0;
    }

    public void setMaxWidth(int theMaxWidth) {

        if (!isLegalDimension(theMaxWidth)) {
            return;//block setting values to 0 or below
        }

        maxWidth = theMaxWidth;
        splitText(maxWidth);
        calcBounds();
        loadRelativeMaxHeight();
    }

    public void updateTextString(String text){
        textString = text;
        splitText(maxWidth);
        calcBounds();
        loadRelativeMaxHeight();
    }

    public String getTextString() {
        return textString;
    }

    protected boolean textDrawCareAboutBounds = true;

    public void setTextDrawCareAboutBounds(boolean theTextDrawCareAboutBounds) {
        textDrawCareAboutBounds = theTextDrawCareAboutBounds;
    }

    protected void loadRelativeMaxHeight() {
        if (useBitmapCache) {
            relativeMaxHeight = maxHeight;
        }
        else {
            relativeMaxHeight = posY + maxHeight;

            if (textDrawCareAboutBounds) {
                textDrawX = (float) Math.floor((posX - bounds.left) + 0.5);
                textDrawY = (float) Math.floor((posY - bounds.top) + 0.5);
            }
            else {
                textDrawX = posX;
                textDrawY = posY;
            }

            for (int i = 0; i < drawLinesY.length; i++) {
                drawLinesY[i] = textDrawY + (float) Math.floor((lineHeight * i) + 0.5);
            }
        }
    }

    protected ArrayList<String> getMeasuredText(String lineString, Paint linePaint, int maxWidth) {

        ArrayList<SleekMeasuredText> measureTextList = new ArrayList<>();
        SleekMeasuredText measureText = null;

        int iterPos = 0;

        int indexOfWhitespace = lineString.indexOf(' ', iterPos);

        ArrayList<String> lineList = new ArrayList<>();

        if (linePaint.measureText(lineString) < maxWidth) {
            lineList.add(lineString);
            return lineList;
        }

        while (indexOfWhitespace > -1) {
            measureText = new SleekMeasuredText();
            measureText.text = lineString.substring(iterPos, indexOfWhitespace + 1);
            measureText.width = linePaint.measureText(measureText.text);

            measureTextList.add(measureText);

            iterPos = indexOfWhitespace + 1;

            indexOfWhitespace = lineString.indexOf(' ', iterPos);
        }

        if (iterPos < lineString.length()) {

            measureText = new SleekMeasuredText();
            measureText.text = lineString.substring(iterPos, lineString.length());
            measureText.width = linePaint.measureText(measureText.text);

            measureTextList.add(measureText);
        }

        String addLine = "";
        float lineWidth = 0.0f;

        ArrayList<String> partWordList;

        for (SleekMeasuredText iterText : measureTextList) {

            if (
                    lineWidth > 0.0f &&
                    iterText.width + lineWidth > maxWidth
                    ) {
                lineList.add(addLine);
                addLine = "";
                lineWidth = 0.0f;
            }

            if (iterText.width <= maxWidth) {
                addLine += iterText.text;
                lineWidth += iterText.width;
            }
            else {// Single word larger than maxWidth, split up word

                if (lineList.size() > 0 && lineList.get(lineList.size() - 1).equals(" ")) {
                    lineList.remove(lineList.size() - 1);//remove last line
                }

                partWordList = getSplitLinesFromWord(iterText.text, linePaint, maxWidth);

                for (int i = 0; i < partWordList.size() - 1; i++) {
                    lineList.add(partWordList.get(i));
                }

                addLine = partWordList.get(partWordList.size() - 1);
                lineWidth = linePaint.measureText(addLine);

            }
        }

        if (lineWidth > 0.0f) {
            lineList.add(addLine);
        }

        return lineList;
    }

    protected ArrayList<String> getSplitLinesFromWord(String wordString, Paint linePaint, int maxWidth) {

        ArrayList<String> returnArray = new ArrayList<>();

        String partString;
        float partWidth = 0.0f;
        int iterator = 1;
        int lastPartIndex = 0;

        while (iterator <= wordString.length()) {

            partString = wordString.substring(lastPartIndex, iterator);
            partWidth = linePaint.measureText(partString);

            if (partWidth >= maxWidth && (iterator - 1) - lastPartIndex > 0) {

                partString = wordString.substring(lastPartIndex, iterator - 1);
                returnArray.add(partString);

                lastPartIndex = iterator - 1;
                partWidth = 0.0f;
            }
            else {
                iterator++;
            }
        }

        if (partWidth > 0.0f) {
            partString = wordString.substring(lastPartIndex, wordString.length());
            returnArray.add(partString);
            partWidth = 0.0f;
        }

        return returnArray;
    }

    protected void splitText(int maxWidth) {

        synchronized (stringLinesLock) {
            stringLines = textString.split("\n");

            if (maxWidth < 0) {
                drawLinesY = new float[stringLines.length];
                return;
            }

            ArrayList<String> newStringLines = new ArrayList<>();

            for (int i = 0; i < stringLines.length; i++) {
                newStringLines.addAll(
                        getMeasuredText(stringLines[i], textPaint, maxWidth)
                );
            }

            stringLines = newStringLines.toArray(new String[newStringLines.size()]);

            drawLinesY = new float[stringLines.length];
        }
    }

    protected void calcBounds() {

        bounds = new RectF();

        Rect boundsInt = new Rect();

        lineHeight = 0.0f;

        if (fixedLineHeight > 0) lineHeight = fixedLineHeight;

        synchronized (stringLinesLock) {
            for (int i = 0; i < stringLines.length; i++) {

                textPaint.getTextBounds(stringLines[i], 0, stringLines[i].length(), boundsInt);

                float mt = textPaint.measureText(stringLines[i]);

                RectF calcBounds = new RectF(boundsInt);

                switch (textAlignInt) {
                    case ALIGN_LEFT:
                        calcBounds.left = 0;
                        calcBounds.right = calcBounds.left + mt;
                        break;
                    case ALIGN_RIGHT:
                        calcBounds.left = -mt;
                        calcBounds.right = calcBounds.left + mt;
                        break;
                    case ALIGN_CENTER:
                        calcBounds.left = (float) Math.floor((-mt / 2.0f) + 0.5d);
                        calcBounds.right = calcBounds.left + mt;
                        break;
                }

                if (
                        stringLines.length == 1 ||
                        (fixedLineHeight <= 0.0f && calcBounds.height() > lineHeight)
                        ) {
                    lineHeight = calcBounds.height();
                }

                if (calcBounds.left < bounds.left) {
                    bounds.left = calcBounds.left;
                }

                if (calcBounds.top < bounds.top) {
                    bounds.top = calcBounds.top;
                }

                if (calcBounds.right > bounds.right) {
                    bounds.right = calcBounds.right;
                }
            }

            bounds.bottom = bounds.top + (lineHeight * stringLines.length);
        }
    }

    protected boolean drawBitmapCache(Canvas canvas) {
        if (useBitmapCache) {

            if (!updatedBitmapCache) {
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                bitmapCache = Bitmap.createBitmap(
                        Math.round(bounds.right - bounds.left),
                        Math.round(bounds.bottom - bounds.top),
                        conf
                        );
                Canvas bitmapCanvas = new Canvas(bitmapCache);

                bitmapCanvas.translate(-bounds.left, -bounds.top);

                drawTextOnCanvas(bitmapCanvas, true);

                updatedBitmapCache = true;
            }

            canvas.drawBitmap(bitmapCache, posX, posY, null);
        }
        return useBitmapCache;
    }

    protected float textDrawX, textDrawY, lineTextDrawY;
    protected boolean textDrawCheckHeight;

    protected void drawTextOnCanvas(Canvas canvas, boolean drawToBitmap) {

        int iter = 0;

        for (String iterString: stringLines) {

            if (drawToBitmap) {

                lineTextDrawY = lineHeight * iter;

                if (textDrawCheckHeight && lineTextDrawY + lineHeight > relativeMaxHeight) break;

                canvas.drawText(
                        iterString,
                        0,
                        lineTextDrawY,
                        textPaint
                        );
            }
            else {

                if (textDrawCheckHeight && drawLinesY[iter] > relativeMaxHeight) break;

                canvas.drawText(
                        iterString,
                        textDrawX,
                        drawLinesY[iter],
                        textPaint
                        );
            }

            iter++;
        }
    }

    public int getLineCountTotal() {
        return drawLinesY.length;
    }

    public int getLineCount() {
        if (textDrawCheckHeight) {
            return Math.min(//returns whichever is LEAST, total lines or total lines that fit.
                    (int) (maxHeight / lineHeight),
                    drawLinesY.length
            );
        }
        else {
            return drawLinesY.length;
        }
    }

    protected float currDrawLine;

    /**
     * Used in SleekViewText class.
     * @param canvas
     * @param info
     */
    public void drawRaw(Canvas canvas, SleekCanvasInfo info) {

        if (stringLines == null) {
            return;
        }

        int iter = 0;

        currDrawLine = posY;

        for (String iterString: stringLines) {

            currDrawLine += fixedLineHeight;

            if (
                    textDrawCheckHeight &&
                    currDrawLine > relativeMaxHeight
                    ) {
                break;
            }

            canvas.drawText(
                    iterString,
                    textDrawX,
                    drawLinesY[iter],
                    textPaint
            );

            iter++;
        }
    }

    @Override
    public void onSleekDraw(Canvas canvas, SleekCanvasInfo canvasInfo) {
        if (!drawBitmapCache(canvas)) {
            drawTextOnCanvas(canvas, false);
        }
    }

    @Override
    public void onSleekCanvasResize(SleekCanvasInfo canvasInfo) {

        if (mHasSlkLay) {
            applyLayout(canvasInfo);
        }

        if (canvasResize != null) {
            canvasResize.onResize(this, canvasInfo);
        }
    }

    protected boolean explicitMaxSizeMode = true;

    /**
     * When this is TRUE, w and h are ignored in calls to setSleekPosAndSize().
     * When this is FALSE, w and h are used as maxWidth and maxHeight when calling
     * setSleekPosAndSize().
     * @param theExplicitMaxSizeMode
     */
    public void setExplicitUpdateMaxSize(boolean theExplicitMaxSizeMode) {
        explicitMaxSizeMode = theExplicitMaxSizeMode;
    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {
        posX = x;
        posY = y;

        if (explicitMaxSizeMode) {//old way
            loadRelativeMaxHeight();
        }
        else {//new way
            setMaxSize(w, h);
        }
    }

    protected boolean mHasSlkLay = false;
    protected SleekLayout mSleekLayout;

    public void applyLayout(SleekCanvasInfo info) {
        getLayout().apply(this, info);
    }

    public SleekLayout getLayout() {
        if (!mHasSlkLay) {//create on demand
            mHasSlkLay = true;
            mSleekLayout = SleekLayout.create();
        }
        return mSleekLayout;
    }

    @Override
    public float getSleekX() {
        return posX;
    }

    @Override
    public float getSleekY() {
        return posY;
    }

    @Override
    public int getSleekW() {
        return (int) bounds.width();
    }

    @Override
    public int getSleekH() {
        if (!textDrawCheckHeight) return (int) bounds.height();
        if (bounds.height() > maxHeight) return maxHeight;
        return (int) bounds.height();
    }

    @Override
    public boolean isSleekFixedPosition() {
        return false;
    }

    @Override
    public boolean isSleekTouchable() {
        return false;
    }

    @Override
    public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo canvasInfo) {
        return false;
    }

    @Override
    public int getSleekPriority() {
        return 0;
    }

    @Override
    public boolean isSleekReadyToDraw() {
        if (!sleekLoadable) return true;
        return sleekLoaded;
    }

    @Override
    public boolean isSleekLoaded() {
        return sleekLoaded;
    }

    @Override
    public boolean isSleekLoadable() {
        return sleekLoadable;
    }

    @Override
    public void onSleekLoad(SleekCanvasInfo canvasInfo) {
        sleekLoaded = true;
    }

    @Override
    public void onSleekUnload() {
        sleekLoaded = false;
    }

    @Override
    public void onSleekParentAdd(SleekCanvas sCanvas, SleekParent sViewCollection) {

    }

    @Override
    public void onSleekParentRemove(SleekCanvas sCanvas, SleekParent sViewCollection) {

    }

    @Override
    public SleekCanvas getSleekCanvas() {
        return null;
    }

    @Override
    public SleekParent getSleekParent() {
        return null;
    }

    public void setCanvasResize(ISleekResize canvasResize) {
        this.canvasResize = canvasResize;
    }

}
