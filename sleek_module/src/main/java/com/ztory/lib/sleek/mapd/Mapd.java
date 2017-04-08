package com.ztory.lib.sleek.mapd;

import java.util.HashMap;
import java.util.Map;

/**
 * Functions that simplify creating, modifying and validating Map instances.
 * Created by jonruna on 2017-03-15.
 */
public class Mapd {

    public static <T> T get(Map<String, ?> map, String key, Class<T> clazz) {

        Object value = map.get(key);

        if (value == null) {
            return null;
        }

        if (clazz.isAssignableFrom(value.getClass())) {
            return (T) value;
        }

        return null;
    }

    public static <T> void validateMap(
            Map<T, MapdRule<?>> validationRules,
            Map<T, ?> dataMap
    ) throws MapdValidationException {
        Object dataItem;
        for (T iterValidationKey : validationRules.keySet()) {

            MapdRule<?> validationRule = validationRules.get(iterValidationKey);

            dataItem = dataMap.get(iterValidationKey);

            if (dataItem == null) {
                if (validationRule.required) {
                    throw new MapdValidationException(
                            "Required key \"" + iterValidationKey + "\" is null!"
                    );
                }
            }
            else {
                if (!validationRule.clazz.isAssignableFrom(dataItem.getClass())) {
                    throw new MapdValidationException(
                            "Validation rule class ["
                            + validationRule.clazz
                            + "] is not assignable from dataItem.getClass() "
                            + dataItem.getClass()
                            + "] for key \"" + iterValidationKey + "\" !"
                    );
                }
            }
        }
    }

    @SafeVarargs
    public static <T> Map<T, MapdRule<?>> createValidationRules(
            MapdKeyValue<T, MapdRule<?>>... entries
    ) {
        Map<T, MapdRule<?>> map = new HashMap<>(entries.length);
        for (MapdKeyValue<T, MapdRule<?>> iterEntry : entries) {
            map.put(iterEntry.getKey(), iterEntry.getValue());
        }
        return map;
    }

    @SafeVarargs
    public static <T, D> Map<T, D> createMap(MapdKeyValue<T, D>... entries) {
        Map<T, D> map = new HashMap<>(entries.length);
        for (MapdKeyValue<T, D> iterEntry : entries) {
            map.put(iterEntry.getKey(), iterEntry.getValue());
        }
        return map;
    }

    @SafeVarargs
    public static <T, D> Map<T, D> appendMap(Map<T, D> map, MapdKeyValue<T, D>... entries) {
        for (MapdKeyValue<T, D> iterEntry : entries) {
            map.put(iterEntry.getKey(), iterEntry.getValue());
        }
        return map;
    }

    public static <K, V> MapdKeyValue<K, V> keyValue(final K key, final V value) {
        return new MapdKeyValueImpl<>(key, value);
    }

}
