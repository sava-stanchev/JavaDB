package com.sava.javadb;

public class Entry<K, V> {
    private final K key;
    private V val;

    public Entry(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return val;
    }

    public void setValue(V val) {
        this.val = val;
    }
}
