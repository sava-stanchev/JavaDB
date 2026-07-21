package com.sava.javadb;

import java.util.List;

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

    @Override
    public String toString() {
        List<Entry<String, String>> entries = vals.entries();
        int n = entries.size();
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int i = 0; i < n; i++) {
            sb.append(entries.get(i).getKey()).append("=").append(entries.get(i).getValue());
            if (i + 1 != n)
                sb.append(", ");
        }

        sb.append("}");
        return sb.toString();
    }
}
