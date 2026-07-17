package com.sava.javadb;

import java.util.ArrayList;
import java.util.List;

public class HashTable<K, V> {
    private final List<Entry<K, V>> entries;

    public HashTable() {
        entries = new ArrayList<>();
    }

    public V get(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key))
                return entry.getValue();
        }

        return null;
    }

    public void put(K key, V val) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                entry.setValue(val);
                return;
            }
        }

        entries.add(new Entry<>(key, val));
    }

    public boolean remove(K key) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getKey().equals(key)) {
                entries.remove(i);
                return true;
            }
        }

        return false;
    }
}
