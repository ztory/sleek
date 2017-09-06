package com.ztory.lib.sleek.mapd;

/**
 * Created by jonruna on 2017-03-15.
 */
public class MapdRule<T> {

    public static MapdRule required(Class<?> clazz) {
        return new MapdRule<>(true, clazz);
    }

    public static MapdRule optional(Class<?> clazz) {
        return new MapdRule<>(false, clazz);
    }

    public final boolean required;
    public final Class<T> clazz;

    public MapdRule(boolean required, Class<T> clazz) {
        this.required = required;
        this.clazz = clazz;
    }

}
