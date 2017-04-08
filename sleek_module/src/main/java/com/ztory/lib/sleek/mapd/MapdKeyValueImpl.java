package com.ztory.lib.sleek.mapd;

/**
 * Created by jonruna on 2017-03-15.
 */
public class MapdKeyValueImpl<K, V> implements MapdKeyValue<K, V> {

    private final K key;
    private final V value;

    public MapdKeyValueImpl(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }
}
