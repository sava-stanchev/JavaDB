package com.sava.javadb;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<String, String> data;

    public Database() {
        data = new HashMap<>();
    }

    public void put(String key, String val) {
        data.put(key, val);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean delete(String key) {
        return data.remove(key) != null;
    }
}
