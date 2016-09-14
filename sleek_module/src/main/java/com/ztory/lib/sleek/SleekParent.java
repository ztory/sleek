package com.ztory.lib.sleek;

import java.util.List;

/**
 * Sleek instances that have child Sleek instances within them can implement this interface to
 * expose their parent-functionality to the world.
 * Created by jonruna on 09/10/14.
 */
public interface SleekParent extends Sleek {

    void addSleek(Sleek addView);
    void removeSleek(Sleek removeView);
    List<Sleek> getSleekChildren();

}
