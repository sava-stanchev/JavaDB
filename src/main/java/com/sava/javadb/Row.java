package com.sava.javadb;

public class Row {
    private final HashTable<String, String> vals;

    public Row() {
        vals = new HashTable<>();
    }

    public void put(String col, String val) {
        vals.put(col, val);
    }

    public String get(String col) {
        return vals.get(col);
    }

    public boolean remove(String col) {
        return vals.remove(col);
    }
}
