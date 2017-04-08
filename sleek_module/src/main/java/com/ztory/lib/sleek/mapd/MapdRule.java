package com.ztory.lib.sleek.mapd;

/**
 * Created by jonruna on 2017-03-15.
 */
public class MapdRule<T> {

    public final boolean required;
    public final Class<T> clazz;

    public MapdRule(boolean required, Class<T> clazz) {
        this.required = required;
        this.clazz = clazz;
    }

}
