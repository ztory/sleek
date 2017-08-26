package com.ztory.lib.sleek.base.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.animation.SAVfade;
import com.ztory.lib.sleek.animation.SleekAnimation;
import com.ztory.lib.sleek.base.SleekBase;
import com.ztory.lib.sleek.base.SleekParam;
import com.ztory.lib.sleek.contract.ISleekCallback;
import com.ztory.lib.sleek.contract.ISleekData;
import com.ztory.lib.sleek.contract.ISleekDrawView;
import java.util.concurrent.Executor;

/**
 * Base class for displaying a Bitmap that loads asynchronously in the Sleek-framework.
 * Created by jonruna on 12/09/16.
 */
public class SleekBaseImage extends SleekBase {

    protected volatile Bitmap mBitmap;
    protected Paint mPaint;
    protected BitmapShader mShader;
    protected boolean mUseShader = false;
    protected float mRoundedRadius = 0.0f;

    protected RectF mImgSize = new RectF();
    protected Rect mSourceRect = new Rect();

    protected volatile boolean mBitmapLoaded = false;

    protected boolean mFadeAnimOnLoad = true;

    protected boolean mBlockUnload = false;

    protected ISleekCallback<SleekBaseImage> mBitmapListener = null;

    protected Executor mBitmapExecutor = null;
    protected Runnable mBitmapFetchRunnable;

    public SleekBaseImage(int theRoundedRadius, SleekParam sleekParam) {
        this(
                theRoundedRadius,
                sleekParam.fixed,
                sleekParam.touchable,
                sleekParam.loadable,
                sleekParam.priority
        );
    }

    public SleekBaseImage(
            int theRoundedRadius,
            boolean isFixedPosition,
            boolean isTouchable,
            boolean isLoadable,
            int theTouchPrio
    ) {
        super(isFixedPosition, isTouchable, isLoadable, theTouchPrio);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setRoundedRadius(theRoundedRadius);

        setUnloadOnRemove(true);
    }

    public void setRoundedRadius(float theRoundedRadius) {
        mRoundedRadius = theRoundedRadius;
        mUseShader = mRoundedRadius > 0.0f;
    }

    /**
     * Call this in order to configure this SleekBaseImage instance to fetch its Bitmap data
     * in a specific way when this Sleek is loaded.
     * Must supply both a Executor instance and a ISleekData<Bitmap> instance in order to activate
     * fetcher functionality.
     * @param theHandler optional Handler instance that setBitmap() will be posted to if supplied
     * @param theBitmapExecutor Executor that the Bitmap will be fetched with
     * @param theBitmapFetcher interface that will produce the Bitmap instance
     */
    public void setBitmapFetcher(
            final Handler theHandler,
            Executor theBitmapExecutor,
            final ISleekData<Bitmap> theBitmapFetcher
    ) {
        if (theBitmapExecutor != null && theBitmapFetcher != null) {
            mBitmapExecutor = theBitmapExecutor;
            mBitmapFetchRunnable = new Runnable() {
                @Override
                public void run() {
                    final Bitmap fetchedBitmap = theBitmapFetcher.getData(SleekBaseImage.this);
                    if (theHandler != null) {
                        theHandler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        setBitmap(fetchedBitmap);
                                    }
                                }
                        );
                    }
                    else {
                        setBitmap(fetchedBitmap);
                    }
                }
            };
        }
        else {
            mBitmapExecutor = null;
            mBitmapFetchRunnable = null;
        }
    }

    /**
     * Call this to set a listener that will fire whenever the setBitmap() method is called.
     * @param theBitmapListener
     */
    public void setBitmapListener(ISleekCallback<SleekBaseImage> theBitmapListener) {
        mBitmapListener = theBitmapListener;
    }

    public void setBlockUnload(boolean willBlockUnload) {
        mBlockUnload = willBlockUnload;
    }

    public void setFadeAnimOnLoad(boolean theFadeAnimOnLoad) {
        mFadeAnimOnLoad = theFadeAnimOnLoad;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint newPaint) {
        mPaint = newPaint;
    }

    public boolean isBitmapLoaded() {
        return mBitmapLoaded;
    }

    public void setBitmap(Bitmap newBitmap) {

        synchronized (SleekBaseImage.this) {
            mBitmapLoaded = false;

            if (loaded) {
                mBitmap = newBitmap;
            }
            else {
                mBitmap = null;
            }

            if (mBitmap != null) {
                mSourceRect.set(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
                setShaderMatrix();
                mBitmapLoaded = true;
            }
            else {
                // NEED to instantiate new Paint so that old Paint-instance gets released,
                // otherwise BitmapShader will cause memory-leak!!!!
                mPaint.setShader(null);
                mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mShader = null;
            }

            if (mBitmapLoaded) {
                if (mFadeAnimOnLoad) {
                    mPaint.setAlpha(0);
                    setSleekAnimView(
                        new SAVfade(
                            0,
                            255,
                            SleekAnimation.ANIM_DURATION_SHORT,
                            mPaint,
                            ISleekDrawView.NO_DRAW
                        )
                    );
                } else {
                    mPaint.setAlpha(255);
                }
            }
        }

        if (mBitmapListener != null) {
            mBitmapListener.sleekCallback(this);
        }

        invalidateSafe();
    }

    @Override
    public void drawView(Sleek view, Canvas canvas, SleekCanvasInfo info) {

        synchronized (SleekBaseImage.this) {
            if (mBitmapLoaded) {

                if (mBitmap.isRecycled()) {
                    onSleekUnload();
                    return;
                }

                if (mUseShader) {
                    if (sleekX == 0 && sleekY == 0) {
                        canvas.drawRoundRect(
                            mImgSize,
                            mRoundedRadius,
                            mRoundedRadius,
                            mPaint
                        );
                    }
                    else {
                        canvas.save();
                        canvas.translate(sleekX, sleekY);
                        canvas.drawRoundRect(
                            mImgSize,
                            mRoundedRadius,
                            mRoundedRadius,
                            mPaint
                        );
                        canvas.restore();
                    }
                }
                else {
                    canvas.drawBitmap(mBitmap, mSourceRect, mImgSize, mPaint);
                }
            }
        }
    }

    @Override
    public void setSleekBounds(float x, float y, int w, int h) {
        super.setSleekBounds(x, y, w, h);

        if (mUseShader) {
            mImgSize.set(
                    0,
                    0,
                    sleekW,
                    sleekH
            );
        }
        else {
            mImgSize.set(
                    sleekX,
                    sleekY,
                    sleekX + sleekW,
                    sleekY + sleekH
            );
        }

        if (mBitmapLoaded && mUseShader) {
            setShaderMatrix();// Update shader matrix
        }
    }

    @Override
    public void onSleekLoad(SleekCanvasInfo info) {

        if (loaded) {
            return;
        }

        super.onSleekLoad(info);

        if (mBitmapExecutor != null && mBitmapFetchRunnable != null) {
            mBitmapExecutor.execute(mBitmapFetchRunnable);
        }
    }

    @Override
    public void onSleekUnload() {

        if (mBlockUnload) {
            return;
        }

        super.onSleekUnload();

        setBitmap(null);
    }

    public void setShaderMatrix() {
        synchronized (SleekBaseImage.this) {
            if (mUseShader && mBitmap != null && !mBitmap.isRecycled()) {

                if (mShader == null) {
                    mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    mPaint.setShader(mShader);
                }

                Matrix shaderMatrix = new Matrix();
                shaderMatrix.setRectToRect(new RectF(mSourceRect), mImgSize, Matrix.ScaleToFit.FILL);
                mShader.setLocalMatrix(shaderMatrix);
            }
        }
    }

    public Rect getSourceRect() {
        return mSourceRect;
    }

}
