package com.ztory.lib.sleek.base.scroller.xy;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.SleekCanvasScroller;
import com.ztory.lib.sleek.contract.ISleekAnimView;
import com.ztory.lib.sleek.util.UtilPx;

/**
 * SleekScrollerXY scrolls the SleekCanvas on the X- and Y-axis.
 * SleekScrollerXY can be configured to only scroll X-axis or only scroll Y-axis.
 * SleekScrollerXY does not support SleekCanvasScroller-padding or SleekCanvasScroller-margin.
 * SleekScrollerXY does not scale the Canvas of its parent SleekCanvas.
 * Created by jonruna on 16/09/16.
 */
public class SleekScrollerXY implements SleekCanvasScroller {

  protected boolean mAutoLoading = true, mShouldLoadWhileScrolling = true;

  protected boolean mShouldScrollX, mShouldScrollY;

  protected long mFlingTouchDurationThreshold = 400;
  protected float mMoveDistanceThreshold;
  protected int mMoveDistanceThresholdDefaultDIPvalue = 20;
  //protected float mFlingVelocityThreshold;

  protected int mOverscrollX, mOverscrollY;

  protected SleekCanvas mSleekCanvas;

  protected EdgeEffectContract mEdgeEffectTop;
  protected EdgeEffectContract mEdgeEffectBottom;
  protected EdgeEffectContract mEdgeEffectLeft;
  protected EdgeEffectContract mEdgeEffectRight;

  protected boolean mEdgeEffectTopActive;
  protected boolean mEdgeEffectBottomActive;
  protected boolean mEdgeEffectLeftActive;
  protected boolean mEdgeEffectRightActive;

  protected float mPosLeft, mPosTop;

  protected boolean mTouchHasMoved = false, mTouchEventActive = false, mTouchEventHandled = false;

  protected long mTouchStartTS;

  protected float
      mTouchStartX,
      mTouchStartY,
      mTouchStartPosLeft,
      mTouchStartPosTop,
      mTouchMoveStartX,
      mTouchMoveStartY,
      mTouchMoveTravelX,
      mTouchMoveTravelY,
      mTouchCurrX,
      mTouchCurrY;

  protected float mRightScrollEdge, mBottomScrollEdge;

  protected int mCanvasWidth, mCanvasHeight;

  protected Rect mContentRect = new Rect();

  protected OverScroller mScroller;

  protected VelocityTracker mVelocityTracker;
  protected ViewConfiguration mViewConfiguration;

  public SleekScrollerXY() {
    this(true, true);
  }

  public SleekScrollerXY(boolean shouldScrollX, boolean shouldScrollY) {
    mShouldScrollX = shouldScrollX;
    mShouldScrollY = shouldScrollY;
  }

  @Override
  public void setSleekCanvas(SleekCanvas sleekCanvas) {

    mSleekCanvas = sleekCanvas;

    if (Build.VERSION.SDK_INT >= 14) {
      mEdgeEffectTop = new EdgeEffectWithContract(sleekCanvas.getContext());
      mEdgeEffectBottom = new EdgeEffectWithContract(sleekCanvas.getContext());
      mEdgeEffectLeft = new EdgeEffectWithContract(sleekCanvas.getContext());
      mEdgeEffectRight = new EdgeEffectWithContract(sleekCanvas.getContext());
    } else {
      mEdgeEffectTop = new EdgeEffectEmptyContract();
      mEdgeEffectBottom = new EdgeEffectEmptyContract();
      mEdgeEffectLeft = new EdgeEffectEmptyContract();
      mEdgeEffectRight = new EdgeEffectEmptyContract();
    }

    mScroller = new OverScroller(sleekCanvas.getContext());
    mViewConfiguration = ViewConfiguration.get(sleekCanvas.getContext());

//    mFlingVelocityThreshold = UtilPx.getPixels(sleekCanvas.getContext(), 80);

    //TODO mViewConfiguration.getScaledOverscrollDistance() returns 0 (zero) on Samsung S7
    mOverscrollX = UtilPx.getPixels(sleekCanvas.getContext(), 28);
    mOverscrollY = UtilPx.getPixels(sleekCanvas.getContext(), 28);
//    mOverscrollX = mViewConfiguration.getScaledOverscrollDistance();
//    mOverscrollY = mViewConfiguration.getScaledOverscrollDistance();

    mMoveDistanceThreshold = UtilPx.getPixels(
        sleekCanvas.getContext(),
        mMoveDistanceThresholdDefaultDIPvalue
    );
  }

  @Override
  public void setPosLeft(float posLeft) {
    mPosLeft = (float) Math.floor(posLeft + 0.5f);
  }

  @Override
  public void setPosTop(float posTop) {
    mPosTop = (float) Math.floor(posTop + 0.5f);
  }

  protected void executeFling(float velocityX, float velocityY, SleekCanvasInfo info) {

    releaseEdgeEffects();
    mScroller.forceFinished(true);
    mScroller.fling(
        -Math.round(mPosLeft),//startX,
        -Math.round(mPosTop),//startY,
        -Math.round(velocityX),//velocityX
        -Math.round(velocityY),//velocityY
        0,//minX
        Math.round(mRightScrollEdge - mCanvasWidth),//maxX
        0,//minY
        Math.round(mBottomScrollEdge - mCanvasHeight),//maxY
        mOverscrollX,//overX
        mOverscrollY//overY
    );

    info.invalidate();
  }

  protected void setAnimEdgeEffectsAndFling() {
    mSleekCanvas.setScrollerAnimView(new ISleekAnimView() {

      private boolean shouldInvalidate = false;
      private int posTopAutoLoadCount = 0, posLeftAutoLoadCount = 0;

      @Override
      public void animTickStart(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

        boolean scrollerFlinging = false;

        if (!mTouchEventActive) {

          mScroller.computeScrollOffset();

          scrollerFlinging = !mScroller.isFinished();

          if (scrollerFlinging) {

            if (mShouldScrollX) {
              setPosLeft(-mScroller.getCurrX());
            }

            if (mShouldScrollY) {
              setPosTop(-mScroller.getCurrY());
            }

            constrainPosXY();

            if (mAutoLoading && mShouldLoadWhileScrolling) {

              if (mShouldScrollX && mShouldScrollY) {

                float deltaPosTop = Math.abs(mTouchStartPosTop - mPosTop);
                float deltaPosLeft = Math.abs(mTouchStartPosLeft - mPosLeft);

                boolean shouldLoadViews = false;

                if (deltaPosLeft > mSleekCanvas.getWidthLoadPadding()) {
                  int loadPaddingTravelCount =
                      (int) (deltaPosLeft / (float) mSleekCanvas.getWidthLoadPadding());
                  if (loadPaddingTravelCount > posLeftAutoLoadCount) {
                    posLeftAutoLoadCount = loadPaddingTravelCount;
                    shouldLoadViews = true;
                  }
                }

                if (deltaPosTop > mSleekCanvas.getHeightLoadPadding()) {
                  int loadPaddingTravelCount =
                      (int) (deltaPosTop / (float) mSleekCanvas.getHeightLoadPadding());
                  if (loadPaddingTravelCount > posTopAutoLoadCount) {
                    posTopAutoLoadCount = loadPaddingTravelCount;
                    shouldLoadViews = true;
                  }
                }

                if (shouldLoadViews) {
                  loadNonFixedSleekInstances();
                }

              } else if (mShouldScrollX) {
                float deltaPosLeft = Math.abs(mTouchStartPosLeft - mPosLeft);
                if (deltaPosLeft > mSleekCanvas.getWidthLoadPadding()) {
                  int loadPaddingTravelCount =
                      (int) (deltaPosLeft / (float) mSleekCanvas.getWidthLoadPadding());
                  if (loadPaddingTravelCount > posLeftAutoLoadCount) {
                    posLeftAutoLoadCount = loadPaddingTravelCount;
                    loadNonFixedSleekInstances();
                  }
                }
              } else if (mShouldScrollY) {
                float deltaPosTop = Math.abs(mTouchStartPosTop - mPosTop);
                if (deltaPosTop > mSleekCanvas.getHeightLoadPadding()) {
                  int loadPaddingTravelCount =
                      (int) (deltaPosTop / (float) mSleekCanvas.getHeightLoadPadding());
                  if (loadPaddingTravelCount > posTopAutoLoadCount) {
                    posTopAutoLoadCount = loadPaddingTravelCount;
                    loadNonFixedSleekInstances();
                  }
                }
              }
            }
          }
        }

        if (!scrollerFlinging && !mTouchEventActive && isAnyEdgeEffectActive()) {
          releaseEdgeEffects();
        }

        shouldInvalidate = scrollerFlinging;
      }

      @Override
      public boolean animTickEnd(Sleek sleek, Canvas canvas, SleekCanvasInfo info) {

        if (drawEdgeEffectsUnclipped(canvas)) {
          shouldInvalidate = true;
        }

        if (shouldInvalidate) {
          info.invalidate();
        } else if (!mTouchEventActive) {
          loadNonFixedSleekInstances();
        }

        return !shouldInvalidate;
      }
    });
  }

  @Override
  public float getPosLeft() {
    return mPosLeft;
  }

  @Override
  public float getPosTop() {
    return mPosTop;
  }

  @Override
  public void onSleekCanvasSizeChanged(SleekCanvasInfo info) {
    mCanvasWidth = info.width;
    mCanvasHeight = info.height;

    mContentRect.set(
        getPaddingLeft(),
        getPaddingTop(),
        mCanvasWidth - getPaddingRight(),
        mCanvasHeight - getPaddingBottom()
    );

    constrainPosXY();
  }

  @Override
  public boolean isAutoLoading() {
    return mAutoLoading;
  }

  protected void loadNonFixedSleekInstances() {
    if (mAutoLoading) {
      mSleekCanvas.loadAndUnloadSleekLists(false);
    }
  }

  protected void movePosXY(float eventX, float eventY) {
    mTouchCurrX = eventX;
    mTouchCurrY = eventY;

    mTouchMoveTravelX = mShouldScrollX ? mTouchCurrX - mTouchMoveStartX : 0;
    mTouchMoveTravelY = mShouldScrollY ? mTouchCurrY - mTouchMoveStartY : 0;

    setPosLeft(mTouchStartPosLeft + mTouchMoveTravelX);
    setPosTop(mTouchStartPosTop + mTouchMoveTravelY);
  }

  @Override
  public void checkScrollBounds() {
    constrainPosXY();
    releaseEdgeEffects();
  }

  protected void constrainPosXY() {

    if (mShouldScrollX) {
      if (mRightScrollEdge <= mCanvasWidth) {
        setPosLeft(0);
        if (Math.abs(mTouchMoveTravelX) > mMoveDistanceThreshold) {
          if (mTouchMoveTravelX > 0) {
            mEdgeEffectLeft.onPull(
                Math.abs(mTouchMoveTravelX) / mCanvasWidth,
                1 - (mTouchCurrY / mCanvasHeight)
            );
            mEdgeEffectLeftActive = true;
          } else {
            mEdgeEffectRight.onPull(
                Math.abs(mTouchMoveTravelX) / mCanvasWidth,
                mTouchCurrY / mCanvasHeight
            );
            mEdgeEffectRightActive = true;
          }
        }
      } else if (mPosLeft > 0) {
        mEdgeEffectLeft.onPull(
            mPosLeft / mCanvasWidth,
            1 - (mTouchCurrY / mCanvasHeight)
        );
        mEdgeEffectLeftActive = true;

        setPosLeft(0);
      } else if (mPosLeft < -mRightScrollEdge + mCanvasWidth) {
        mEdgeEffectRight.onPull(
            (Math.abs(mPosLeft) + mCanvasWidth - mRightScrollEdge) / mCanvasWidth,
            mTouchCurrY / mCanvasHeight
        );
        mEdgeEffectRightActive = true;

        setPosLeft(-mRightScrollEdge + mCanvasWidth);
      }
    }

    if (mShouldScrollY) {
      if (mBottomScrollEdge <= mCanvasHeight) {
        setPosTop(0);
        if (Math.abs(mTouchMoveTravelY) > mMoveDistanceThreshold) {
          if (mTouchMoveTravelY > 0) {
            mEdgeEffectTop.onPull(
                Math.abs(mTouchMoveTravelY) / mCanvasHeight,
                mTouchCurrX / mCanvasWidth
            );
            mEdgeEffectTopActive = true;
          } else {
            mEdgeEffectBottom.onPull(
                Math.abs(mTouchMoveTravelY) / mCanvasHeight,
                1 - (mTouchCurrX / mCanvasWidth)
            );
            mEdgeEffectBottomActive = true;
          }
        }
      } else if (mPosTop > 0) {
        mEdgeEffectTop.onPull(
            mPosTop / mCanvasHeight,
            mTouchCurrX / mCanvasWidth
        );
        mEdgeEffectTopActive = true;

        setPosTop(0);
      } else if (mPosTop < -mBottomScrollEdge + mCanvasHeight) {
        mEdgeEffectBottom.onPull(
            (Math.abs(mPosTop) + mCanvasHeight - mBottomScrollEdge) / mCanvasHeight,
            1 - (mTouchCurrX / mCanvasWidth)
        );
        mEdgeEffectBottomActive = true;

        setPosTop(-mBottomScrollEdge + mCanvasHeight);
      }
    }
  }

  @Override
  public boolean onSleekTouchEvent(MotionEvent event, SleekCanvasInfo info) {

    mTouchEventHandled = false;

    switch (event.getActionMasked()) {

      case MotionEvent.ACTION_DOWN:

        if (!mScroller.isFinished()) {
          loadNonFixedSleekInstances();
        }

        //mSleekCanvas.setScrollerAnimView(ISleekAnimView.NO_ANIMATION);
        releaseEdgeEffects();
        mScroller.forceFinished(true);

        mTouchEventActive = true;
        mTouchHasMoved = false;
        mTouchCurrX = event.getX();
        mTouchCurrY = event.getY();
        mTouchStartX = mTouchCurrX;
        mTouchStartY = mTouchCurrY;
        mTouchMoveStartX = mTouchCurrX;
        mTouchMoveStartY = mTouchCurrY;
        mTouchStartPosLeft = mPosLeft;
        mTouchStartPosTop = mPosTop;
        mTouchStartTS = System.currentTimeMillis();

        mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);

        break;

      case MotionEvent.ACTION_UP:

        if (mTouchHasMoved) {

          // Block fling if already at max or min position or if scrolling is disabled
          boolean blockFlingX = true;
          boolean blockFlingY = true;

          if (mShouldScrollX) {
            blockFlingX = mPosLeft >= 0
                || mPosLeft <= -mRightScrollEdge + mCanvasWidth;
          }

          if (mShouldScrollY) {
            blockFlingY = mPosTop >= 0
                || mPosTop <= -mBottomScrollEdge + mCanvasHeight;
          }

          boolean didFling = false;

          if (!blockFlingX || !blockFlingY) {
            long touchDuration = System.currentTimeMillis() - mTouchStartTS;
            if (touchDuration < mFlingTouchDurationThreshold) {

              mVelocityTracker.computeCurrentVelocity(
                  1000,
                  mViewConfiguration.getScaledMaximumFlingVelocity()
              );

              float velocityX = blockFlingX ? 0 : mVelocityTracker.getXVelocity();
              float velocityY = blockFlingY ? 0 : mVelocityTracker.getYVelocity();
              float minFlingVelocity = mViewConfiguration.getScaledMinimumFlingVelocity();

              mVelocityTracker.recycle();
              mVelocityTracker = null;

              //Log.d("SleekScrollerXY", "SleekScrollerXY | velocityY: " + velocityY);

              if (
                  Math.abs(velocityX) > minFlingVelocity
                      || Math.abs(velocityY) > minFlingVelocity
                  ) {
                executeFling(velocityX, velocityY, info);
                setAnimEdgeEffectsAndFling();
                didFling = true;
              }
            }
          }

          if (!didFling) {
            if (isAnyEdgeEffectActive()) {
              releaseEdgeEffects();
            } else {
              loadNonFixedSleekInstances();
            }
          }

          mTouchEventHandled = true;
        }

        mTouchEventActive = false;

        break;

      case MotionEvent.ACTION_CANCEL:

        mTouchEventHandled = false;
        mTouchEventActive = false;

        break;

      case MotionEvent.ACTION_MOVE:

        mVelocityTracker.addMovement(event);

        if (
            mTouchHasMoved
                || Math.abs(mTouchStartX - event.getX()) > mMoveDistanceThreshold
                || Math.abs(mTouchStartY - event.getY()) > mMoveDistanceThreshold
            ) {
          if (!mTouchHasMoved) {
            mTouchMoveStartX = event.getX();
            mTouchMoveStartY = event.getY();
          }
          mTouchHasMoved = true;
          movePosXY(event.getX(), event.getY());
          constrainPosXY();
          setAnimEdgeEffectsAndFling();
          mTouchEventHandled = true;
        }

        break;

      case MotionEvent.ACTION_POINTER_DOWN:

        break;

      case MotionEvent.ACTION_POINTER_UP:

        break;
    }

    return mTouchEventHandled;
  }

  @Override
  public void setBottomScrollEdge(float bottomScrollEdge) {
    mBottomScrollEdge = bottomScrollEdge;
  }

  @Override
  public void setRightScrollEdge(float rightScrollEdge) {
    mRightScrollEdge = rightScrollEdge;
  }

  protected boolean drawEdgeEffectsUnclipped(Canvas canvas) {
    // The methods below rotate and translate the canvas as needed before drawing the glow,
    // since EdgeEffectCompat always draws a top-glow at 0,0.

    boolean needsInvalidate = false;

    if (!mEdgeEffectTop.isFinished()) {
      final int restoreCount = canvas.save();
      canvas.translate(mContentRect.left, mContentRect.top);
      mEdgeEffectTop.setSize(mContentRect.width(), mContentRect.height());
      if (mEdgeEffectTop.draw(canvas)) {
        needsInvalidate = true;
      }
      canvas.restoreToCount(restoreCount);
    }

    if (!mEdgeEffectBottom.isFinished()) {
      final int restoreCount = canvas.save();
      canvas.translate(2 * mContentRect.left - mContentRect.right, mContentRect.bottom);
      canvas.rotate(180, mContentRect.width(), 0);
      mEdgeEffectBottom.setSize(mContentRect.width(), mContentRect.height());
      if (mEdgeEffectBottom.draw(canvas)) {
        needsInvalidate = true;
      }
      canvas.restoreToCount(restoreCount);
    }

    if (!mEdgeEffectLeft.isFinished()) {
      final int restoreCount = canvas.save();
      canvas.translate(mContentRect.left, mContentRect.bottom);
      canvas.rotate(-90, 0, 0);
      mEdgeEffectLeft.setSize(mContentRect.height(), mContentRect.width());
      if (mEdgeEffectLeft.draw(canvas)) {
        needsInvalidate = true;
      }
      canvas.restoreToCount(restoreCount);
    }

    if (!mEdgeEffectRight.isFinished()) {
      final int restoreCount = canvas.save();
      canvas.translate(mContentRect.right, mContentRect.top);
      canvas.rotate(90, 0, 0);
      mEdgeEffectRight.setSize(mContentRect.height(), mContentRect.width());
      if (mEdgeEffectRight.draw(canvas)) {
        needsInvalidate = true;
      }
      canvas.restoreToCount(restoreCount);
    }

    return needsInvalidate;
  }

  protected boolean isAnyEdgeEffectActive() {
    return mEdgeEffectLeftActive
        || mEdgeEffectTopActive
        || mEdgeEffectRightActive
        || mEdgeEffectBottomActive;
  }

  protected void releaseEdgeEffects() {
    mEdgeEffectLeftActive
        = mEdgeEffectTopActive
        = mEdgeEffectRightActive
        = mEdgeEffectBottomActive
        = false;
    mEdgeEffectLeft.onRelease();
    mEdgeEffectTop.onRelease();
    mEdgeEffectRight.onRelease();
    mEdgeEffectBottom.onRelease();
  }

  public void setCustomEdgeEffects(
      EdgeEffectContract eeTop,
      EdgeEffectContract eeBottom,
      EdgeEffectContract eeLeft,
      EdgeEffectContract eeRight
  ) {
    if (eeTop != null) {
      mEdgeEffectTop = eeTop;
    }

    if (eeBottom != null) {
      mEdgeEffectBottom = eeBottom;
    }

    if (eeLeft != null) {
      mEdgeEffectLeft = eeLeft;
    }

    if (eeRight != null) {
      mEdgeEffectRight = eeRight;
    }
  }

  @Override
  public void setPaddingTop(int paddingTop) {

  }

  @Override
  public void setPaddingBottom(int paddingBottom) {

  }

  @Override
  public void setPaddingLeft(int paddingLeft) {

  }

  @Override
  public void setPaddingRight(int paddingRight) {

  }

  @Override
  public int getPaddingTop() {
    return 0;
  }

  @Override
  public int getPaddingBottom() {
    return 0;
  }

  @Override
  public int getPaddingLeft() {
    return 0;
  }

  @Override
  public int getPaddingRight() {
    return 0;
  }

  @Override
  public void setMarginTop(int marginTop) {

  }

  @Override
  public void setMarginBottom(int marginBottom) {

  }

  @Override
  public void setMarginLeft(int marginLeft) {

  }

  @Override
  public void setMarginRight(int marginRight) {

  }

  @Override
  public int getMarginTop() {
    return 0;
  }

  @Override
  public int getMarginBottom() {
    return 0;
  }

  @Override
  public int getMarginLeft() {
    return 0;
  }

  @Override
  public int getMarginRight() {
    return 0;
  }

  @Override
  public boolean isScaled() {
    return false;
  }

  @Override
  public float getScaleX() {
    return 0;
  }

  @Override
  public float getScaleY() {
    return 0;
  }

  @Override
  public float getScalePivotX() {
    return 0;
  }

  @Override
  public float getScalePivotY() {
    return 0;
  }

}
