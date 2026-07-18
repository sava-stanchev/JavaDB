package com.sava.javadb;

import java.util.ArrayList;
import java.util.List;

public class HashTable<K, V> {
    private final List<List<Entry<K, V>>> buckets;

    public HashTable() {
        buckets = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    private int bucketIdx(K key) {
        return Math.abs(key.hashCode()) % buckets.size();
    }

    public V get(K key) {
        int bucket = bucketIdx(key);
        List<Entry<K, V>> entries = buckets.get(bucket);

        for (Entry<K, V> e : entries) {
            if (e.getKey().equals(key))
                return e.getValue();
        }

        return null;
    }

    public void put(K key, V val) {
        int bucket = bucketIdx(key);
        List<Entry<K, V>> entries = buckets.get(bucket);

        for (Entry<K, V> e : entries) {
            if (e.getKey().equals(key)) {
                e.setValue(val);
                return;
            }
        }

        entries.add(new Entry<>(key, val));
    }

    public boolean remove(K key) {
        int bucket = bucketIdx(key);
        List<Entry<K, V>> entries = buckets.get(bucket);

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getKey().equals(key)) {
                entries.remove(i);
                return true;
            }
        }

        return false;
    }

    public List<Entry<K, V>> entries() {
        List<Entry<K, V>> allEntries = new ArrayList<>();
        for (List<Entry<K, V>> bucket : buckets) {
            allEntries.addAll(bucket);
        }

        return allEntries;
    }

    public void clear() {
        for (List<Entry<K, V>> bucket : buckets) {
            bucket.clear();
        }
    }
}
