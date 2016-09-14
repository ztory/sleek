package com.ztory.lib.sleek.layout;

import com.ztory.lib.sleek.SleekCanvasInfo;
import com.ztory.lib.sleek.contract.ISleekAnimRun;
import com.ztory.lib.sleek.Sleek;
import com.ztory.lib.sleek.util.UtilSleekLayout;

/**
 * Layout functionality for Sleek instances.
 * This class provides easy to use rules for laying out a Sleek instance relative to either the
 * canvas that the Sleek is painted on or another Sleek reference that you want to relatively
 * position your Sleek to.
 * Created by jonruna on 09/10/14.
 */
public class SleekLayout {



    private SleekLayout() {

    }

    public static SleekLayout create() {
        return new SleekLayout();
    }

    public static final int
            UNCHANGED = 0,
            ABSOLUTE = 1,
            POS_X_WEST_OF = 2,
            POS_X_EAST_OF = 3,
            POS_X_PARENT = 4,
            POS_Y_NORTH_OF = 5,
            POS_Y_SOUTH_OF = 6,
            POS_Y_PARENT = 7,
            POS_CENTER = 8,
            SIZE_FIT_PARENT = 9,
            PERCENT = 10,
            PERCENT_CANVAS = 11,
            COMPUTE = 12,
            POS_X_PARENT_LEFT = 13,
            POS_X_PARENT_RIGHT = 14,
            POS_Y_PARENT_TOP = 15,
            POS_Y_PARENT_BOTTOM = 16,
            POS_PARENT_PERCENT = 17;

    private LayX mLayX;

    private LayY mLayY;

    private LayW mLayW;

    private LayH mLayH;

    private float calcPosX, calcPosY;
    private int calcSizeW, calcSizeH;

    private boolean
            hasPreApplyRun = false,
            hasPostApplyRun = false,
            hasLayX = false,
            hasLayY = false,
            hasLayW = false,
            hasLayH = false;

    private ISleekAnimRun preApplyRun, postApplyRun;

    public SleekLayout setPreApplyRun(ISleekAnimRun thePreApplyRun) {
        preApplyRun = thePreApplyRun;
        hasPreApplyRun = preApplyRun != null;
        return this;
    }

    public SleekLayout setPostApplyRun(ISleekAnimRun thePostApplyRun) {
        postApplyRun = thePostApplyRun;
        hasPostApplyRun = postApplyRun != null;
        return this;
    }

    public SleekLayout x(
            SL.X theEnumX,
            int theMargin,
            Sleek theParent
    ) {
        return x(theEnumX, theMargin, theParent, 0.5f, null);
    }

    public SleekLayout x(
            SL.X theEnumX,
            int theMargin,
            Sleek theParent,
            float thePercent
    ) {
        return x(theEnumX, theMargin, theParent, thePercent, null);
    }

    public SleekLayout x(
            SL.X theEnumX,
            int theMargin,
            Sleek theParent,
            float thePercent,
            IComputeFloat theComputeFloat
    ) {
        switch (theEnumX) {
            case ABSOLUTE:
                return x(new LayX(LayX.ABSOLUTE, theMargin, theParent, thePercent));
            case WEST_OF:
                return x(new LayX(LayX.WEST_OF, theMargin, theParent, thePercent));
            case EAST_OF:
                return x(new LayX(LayX.EAST_OF, theMargin, theParent, thePercent));
            case POS_CENTER:
                return x(new LayX(LayX.POS_CENTER, theMargin, theParent, thePercent));
            case PERCENT:
                return x(new LayX(LayX.PERCENT, theMargin, theParent, thePercent));
            case PERCENT_CANVAS:
                return x(new LayX(LayX.PERCENT_CANVAS, theMargin, theParent, thePercent));
            case COMPUTE:
                return x(new LayX(theComputeFloat));
            case PARENT_LEFT:
                return x(new LayX(LayX.PARENT_LEFT, theMargin, theParent, thePercent));
            case PARENT_RIGHT:
                return x(new LayX(LayX.PARENT_RIGHT, theMargin, theParent, thePercent));
            case PARENT_PERCENT:
                return x(new LayX(LayX.PARENT_PERCENT, theMargin, theParent, thePercent));
        }
        return null;
    }

    public SleekLayout y(
            SL.Y theEnumY,
            int theMargin,
            Sleek theParent
    ) {
        return y(theEnumY, theMargin, theParent, 0.5f, null);
    }

    public SleekLayout y(
            SL.Y theEnumY,
            int theMargin,
            Sleek theParent,
            float thePercent
    ) {
        return y(theEnumY, theMargin, theParent, thePercent, null);
    }

    public SleekLayout y(
            SL.Y theEnumY,
            int theMargin,
            Sleek theParent,
            float thePercent,
            IComputeFloat theComputeFloat
    ) {
        switch (theEnumY) {
            case ABSOLUTE:
                return y(new LayY(LayY.ABSOLUTE, theMargin, theParent, thePercent));
            case NORTH_OF:
                return y(new LayY(LayY.NORTH_OF, theMargin, theParent, thePercent));
            case SOUTH_OF:
                return y(new LayY(LayY.SOUTH_OF, theMargin, theParent, thePercent));
            case POS_CENTER:
                return y(new LayY(LayY.POS_CENTER, theMargin, theParent, thePercent));
            case PERCENT:
                return y(new LayY(LayY.PERCENT, theMargin, theParent, thePercent));
            case PERCENT_CANVAS:
                return y(new LayY(LayY.PERCENT_CANVAS, theMargin, theParent, thePercent));
            case COMPUTE:
                return y(new LayY(theComputeFloat));
            case PARENT_TOP:
                return y(new LayY(LayY.PARENT_TOP, theMargin, theParent, thePercent));
            case PARENT_BOTTOM:
                return y(new LayY(LayY.PARENT_BOTTOM, theMargin, theParent, thePercent));
            case PARENT_PERCENT:
                return y(new LayY(LayY.PARENT_PERCENT, theMargin, theParent, thePercent));
        }
        return null;
    }

    public SleekLayout w(
            SL.W theEnumW,
            int theMargin,
            Sleek theParent
    ) {
        return w(theEnumW, theMargin, theParent, 0.5f, null);
    }

    public SleekLayout w(
            SL.W theEnumW,
            int theMargin,
            Sleek theParent,
            float thePercent
    ) {
        return w(theEnumW, theMargin, theParent, thePercent, null);
    }

    public SleekLayout w(
            SL.W theEnumW,
            int theMargin,
            Sleek theParent,
            float thePercent,
            IComputeInt theComputeInt
    ) {
        switch (theEnumW) {
            case ABSOLUTE:
                return w(new LayW(LayW.ABSOLUTE, theMargin, theParent, thePercent));
            case MATCH_PARENT:
                return w(new LayW(LayW.MATCH_PARENT, theMargin, theParent, thePercent));
            case PERCENT:
                return w(new LayW(LayW.PERCENT, theMargin, theParent, thePercent));
            case PERCENT_CANVAS:
                return w(new LayW(LayW.PERCENT_CANVAS, theMargin, theParent, thePercent));
            case COMPUTE:
                return w(new LayW(theComputeInt));
        }
        return null;
    }

    public SleekLayout h(
            SL.H theEnumH,
            int theMargin,
            Sleek theParent
    ) {
        return h(theEnumH, theMargin, theParent, 0.5f, null);
    }

    public SleekLayout h(
            SL.H theEnumH,
            int theMargin,
            Sleek theParent,
            float thePercent
    ) {
        return h(theEnumH, theMargin, theParent, thePercent, null);
    }
    
    public SleekLayout h(
            SL.H theEnumH,
            int theMargin,
            Sleek theParent,
            float thePercent,
            IComputeInt theComputeInt
    ) {
        switch (theEnumH) {
            case ABSOLUTE:
                return h(new LayH(LayH.ABSOLUTE, theMargin, theParent, thePercent));
            case MATCH_PARENT:
                return h(new LayH(LayH.MATCH_PARENT, theMargin, theParent, thePercent));
            case PERCENT:
                return h(new LayH(LayH.PERCENT, theMargin, theParent, thePercent));
            case PERCENT_CANVAS:
                return h(new LayH(LayH.PERCENT_CANVAS, theMargin, theParent, thePercent));
            case COMPUTE:
                return h(new LayH(theComputeInt));
        }
        return null;
    }

    public SleekLayout x(LayX theLayX) {
        mLayX = theLayX;
        hasLayX = mLayX != null && mLayX.mode != UNCHANGED;
        return this;
    }

    public SleekLayout y(LayY theLayY) {
        mLayY = theLayY;
        hasLayY = mLayY != null && mLayY.mode != UNCHANGED;
        return this;
    }

    public SleekLayout w(LayW theLayW) {
        mLayW = theLayW;
        hasLayW = mLayW != null && mLayW.mode != UNCHANGED;
        return this;
    }

    public SleekLayout h(LayH theLayH) {
        mLayH = theLayH;
        hasLayH = mLayH != null && mLayH.mode != UNCHANGED;
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #w(LayW)} instead. */
    @Deprecated
    public SleekLayout setLaySizeW(IComputeInt compute) {
        w(new LayW(compute));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #w(LayW)} instead. */
    @Deprecated
    public SleekLayout setLaySizeW(int mode, int margin, Sleek parent) {
        w(new LayW(mode, margin, parent));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #w(LayW)} instead. */
    @Deprecated
    public SleekLayout setLaySizeW(int mode, int margin, Sleek parent, float percent) {
        w(new LayW(mode, margin, parent, percent));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #h(LayH)} instead. */
    @Deprecated
    public SleekLayout setLaySizeH(IComputeInt compute) {
        h(new LayH(compute));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #h(LayH)} instead. */
    @Deprecated
    public SleekLayout setLaySizeH(int mode, int margin, Sleek parent) {
        h(new LayH(mode, margin, parent));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #h(LayH)} instead. */
    @Deprecated
    public SleekLayout setLaySizeH(int mode, int margin, Sleek parent, float percent) {
        h(new LayH(mode, margin, parent, percent));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #x(LayX)} instead. */
    @Deprecated
    public SleekLayout setLayPosX(IComputeFloat compute) {
        x(new LayX(compute));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #x(LayX)} instead. */
    @Deprecated
    public SleekLayout setLayPosX(int mode, int margin, Sleek parent) {
        x(new LayX(mode, margin, parent));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #x(LayX)} instead. */
    @Deprecated
    public SleekLayout setLayPosX(int mode, int margin, Sleek parent, float percent) {
        x(new LayX(mode, margin, parent, percent));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #y(LayY)} instead. */
    @Deprecated
    public SleekLayout setLayPosY(IComputeFloat compute) {
        y(new LayY(compute));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #y(LayY)} instead. */
    @Deprecated
    public SleekLayout setLayPosY(int mode, int margin, Sleek parent) {
        y(new LayY(mode, margin, parent));
        return this;
    }

    /** Old style method, should not be used. Will be removed in the future.
     * @deprecated use {@link #y(LayY)} instead. */
    @Deprecated
    public SleekLayout setLayPosY(int mode, int margin, Sleek parent, float percent) {
        y(new LayY(mode, margin, parent, percent));
        return this;
    }

    public void apply(Sleek layView, SleekCanvasInfo info) {

        if (hasPreApplyRun) {
            preApplyRun.run(info);
        }

        applySize(layView, info);

        if (hasPostApplyRun) {
            postApplyRun.run(info);
        }
    }

    public void applySize(Sleek layView, SleekCanvasInfo info) {
        if (needsToCompute()) {
            computeSizeW(layView, info);
            computeSizeH(layView, info);

            applyComputedSize(layView, info);

            computePosX(layView, info);
            computePosY(layView, info);

            applyComputedValues(layView, info);
        }
    }

    private boolean needsToCompute() {
        if (
                !hasLayX &&
                !hasLayY &&
                !hasLayW &&
                !hasLayH
                ) {
            return false;
        }
        return true;
    }

    private void applyComputedSize(Sleek layView, SleekCanvasInfo info) {
        layView.setSleekBounds(
                layView.getSleekX(),
                layView.getSleekY(),
                calcSizeW,
                calcSizeH
        );

        //Update with actual size
        calcSizeW = layView.getSleekW();
        calcSizeH = layView.getSleekH();
    }

    private void applyComputedValues(Sleek layView, SleekCanvasInfo info) {
        layView.setSleekBounds(
                calcPosX,
                calcPosY,
                calcSizeW,
                calcSizeH
        );
    }

    public int computeSizeW(Sleek layView, SleekCanvasInfo info) {

        if (mLayW == null) {
            calcSizeW = layView.getSleekW();
            return calcSizeW;
        }
        else if (mLayW.mode == ABSOLUTE) {
            calcSizeW = mLayW.margin;
            return calcSizeW;
        }
        else if (
                mLayW.mode == UNCHANGED ||
                (mLayW.mode != COMPUTE && mLayW.mode != PERCENT_CANVAS && mLayW.parent == null)
                ) {
            calcSizeW = layView.getSleekW();
            return calcSizeW;
        }

        switch (mLayW.mode) {
            case SIZE_FIT_PARENT:
                calcSizeW = mLayW.parent.getSleekW() - mLayW.margin;
                break;
            case PERCENT:
                calcSizeW = Math.round((mLayW.parent.getSleekW() - mLayW.margin) * mLayW.percent);
                break;
            case PERCENT_CANVAS:
                calcSizeW = Math.round((info.width - mLayW.margin) * mLayW.percent);
                break;
            case COMPUTE:
                calcSizeW = mLayW.computeInt.compute(info);
                break;
        }

        return calcSizeW;
    }

    public int computeSizeH(Sleek layView, SleekCanvasInfo info) {

        if (mLayH == null) {
            calcSizeH = layView.getSleekH();
            return calcSizeH;
        }
        else if (mLayH.mode == ABSOLUTE) {
            calcSizeH = mLayH.margin;
            return calcSizeH;
        }
        else if (
                mLayH.mode == UNCHANGED ||
                (mLayH.mode != COMPUTE && mLayH.mode != PERCENT_CANVAS && mLayH.parent == null)
                ) {
            calcSizeH = layView.getSleekH();
            return calcSizeH;
        }

        switch (mLayH.mode) {
            case SIZE_FIT_PARENT:
                calcSizeH = mLayH.parent.getSleekH() - mLayH.margin;
                break;
            case PERCENT:
                calcSizeH = Math.round((mLayH.parent.getSleekH() - mLayH.margin) * mLayH.percent);
                break;
            case PERCENT_CANVAS:
                calcSizeH = Math.round((info.height - mLayH.margin) * mLayH.percent);
                break;
            case COMPUTE:
                calcSizeH = mLayH.computeInt.compute(info);
                break;
        }

        return calcSizeH;
    }

    private float computePosX(Sleek layView, SleekCanvasInfo info) {

        if (mLayX == null) {
            calcPosX = layView.getSleekX();
            return calcPosX;
        }
        else if (mLayX.mode == ABSOLUTE) {
            calcPosX = mLayX.margin;
            return calcPosX;
        }
        else if (
                mLayX.mode == UNCHANGED ||
                (mLayX.mode != COMPUTE && mLayX.mode != PERCENT_CANVAS && mLayX.mode != POS_CENTER && mLayX.parent == null)
                ) {
            calcPosX = layView.getSleekX();
            return calcPosX;
        }

        switch (mLayX.mode) {
            case POS_X_PARENT_LEFT:
                calcPosX = mLayX.parent.getSleekX() + mLayX.margin;
                break;
            case POS_X_PARENT_RIGHT:
                calcPosX = mLayX.parent.getSleekX() + mLayX.parent.getSleekW() - calcSizeW - mLayX.margin;
                break;
            case POS_X_WEST_OF:
                calcPosX = mLayX.parent.getSleekX() - calcSizeW - mLayX.margin;
                break;
            case POS_X_EAST_OF:
                calcPosX = mLayX.parent.getSleekX() + mLayX.parent.getSleekW() + mLayX.margin;
                break;
            case POS_X_PARENT:
                calcPosX = mLayX.parent.getSleekX() + mLayX.margin;
                break;
            case POS_CENTER:
                if (mLayX.parent != null) {
                    calcPosX = UtilSleekLayout.getAlignCenterHoriz(
                            calcSizeW,
                            mLayX.parent.getSleekX(),
                            mLayX.parent.getSleekW()
                    );
                }
                else {
                    calcPosX = UtilSleekLayout.getAlignCenterHoriz(
                            calcSizeW,
                            0,
                            info.width
                    );
                }
                break;
            case PERCENT:
                calcPosX = Math.round(mLayX.parent.getSleekX() + (mLayX.parent.getSleekW() * mLayX.percent)) + mLayX.margin;
                break;
            case POS_PARENT_PERCENT:
                calcPosX = Math.round(mLayX.parent.getSleekX() + (mLayX.parent.getSleekW() * mLayX.percent)) + mLayX.margin;
                calcPosX -= calcSizeW / 2.0f;
                break;
            case PERCENT_CANVAS:
                calcPosX = Math.round(info.width * mLayX.percent) + mLayX.margin;
                break;
            case COMPUTE:
                calcPosX = mLayX.computeFloat.compute(info);
                break;
        }

        return calcPosX;
    }

    public float computePosY(Sleek layView, SleekCanvasInfo info) {

        if (mLayY == null) {
            calcPosY = layView.getSleekY();
            return calcPosY;
        }
        else if (mLayY.mode == ABSOLUTE) {
            calcPosY = mLayY.margin;
            return calcPosY;
        }
        else if (
                mLayY.mode == UNCHANGED ||
                (mLayY.mode != COMPUTE && mLayY.mode != PERCENT_CANVAS && mLayY.mode != POS_CENTER && mLayY.parent == null)
                ) {
            calcPosY = layView.getSleekY();
            return calcPosY;
        }

        switch (mLayY.mode) {
            case POS_Y_PARENT_TOP:
                calcPosY = mLayY.parent.getSleekY() + mLayY.margin;
                break;
            case POS_Y_PARENT_BOTTOM:
                calcPosY = mLayY.parent.getSleekY() + mLayY.parent.getSleekH() - calcSizeH - mLayY.margin;
                break;
            case POS_Y_NORTH_OF:
                calcPosY = mLayY.parent.getSleekY() - calcSizeH - mLayY.margin;
                break;
            case POS_Y_SOUTH_OF:
                calcPosY = mLayY.parent.getSleekY() + mLayY.parent.getSleekH() + mLayY.margin;
                break;
            case POS_Y_PARENT:
                calcPosY = mLayY.parent.getSleekY() + mLayY.margin;
                break;
            case POS_CENTER:
                if (mLayY.parent != null) {
                    calcPosY = UtilSleekLayout.getAlignCenterVert(
                            calcSizeH,
                            mLayY.parent.getSleekY(),
                            mLayY.parent.getSleekH()
                    );
                }
                else {
                    calcPosY = UtilSleekLayout.getAlignCenterVert(
                            calcSizeH,
                            0,
                            info.height
                    );
                }
                break;
            case PERCENT:
                calcPosY = Math.round(mLayY.parent.getSleekY() + (mLayY.parent.getSleekH() * mLayY.percent)) + mLayY.margin;
                break;
            case POS_PARENT_PERCENT:
                calcPosY = Math.round(mLayY.parent.getSleekY() + (mLayY.parent.getSleekH() * mLayY.percent)) + mLayY.margin;
                calcPosY -= calcSizeH / 2.0f;
                break;
            case PERCENT_CANVAS:
                calcPosY = Math.round(info.height * mLayY.percent) + mLayY.margin;
                break;
            case COMPUTE:
                calcPosY = mLayY.computeFloat.compute(info);
                break;
        }

        return calcPosY;
    }

}
