package com.ztory.lib.sleek.base;

/**
 * Created by jonruna on 25/11/16.
 */
public class SleekParam {

    public static final SleekParam
            DEFAULT = new SleekParam(
                    SleekBase.FIXED_POSITION_FALSE,
                    SleekBase.TOUCHABLE_FALSE,
                    SleekBase.LOADABLE_TRUE,
                    SleekBase.TOUCH_PRIO_DEFAULT
            ),
            DEFAULT_TOUCHABLE = new SleekParam(
                    SleekBase.FIXED_POSITION_FALSE,
                    SleekBase.TOUCHABLE_TRUE,
                    SleekBase.LOADABLE_TRUE,
                    SleekBase.TOUCH_PRIO_DEFAULT
            ),
            FIXED_DEFAULT = new SleekParam(
                    SleekBase.FIXED_POSITION_TRUE,
                    SleekBase.TOUCHABLE_FALSE,
                    SleekBase.LOADABLE_TRUE,
                    SleekBase.TOUCH_PRIO_DEFAULT
            ),
            FIXED_DEFAULT_TOUCHABLE = new SleekParam(
                    SleekBase.FIXED_POSITION_TRUE,
                    SleekBase.TOUCHABLE_TRUE,
                    SleekBase.LOADABLE_TRUE,
                    SleekBase.TOUCH_PRIO_DEFAULT
            );

    public final boolean fixed, touchable, loadable;
    public final int priority;

    public SleekParam(boolean isFixed, boolean isTouchable, boolean isLoadable, int thePriority) {
        fixed = isFixed;
        touchable = isTouchable;
        loadable = isLoadable;
        priority = thePriority;
    }

    public SleekParam newPriority(int thePriority) {
        return new SleekParam(
                fixed,
                touchable,
                loadable,
                thePriority
        );
    }

    public SleekParam newLoadable(boolean isLoadable) {
        return new SleekParam(
                fixed,
                touchable,
                isLoadable,
                priority
        );
    }

}
