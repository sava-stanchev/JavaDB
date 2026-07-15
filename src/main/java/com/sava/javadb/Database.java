package com.sava.javadb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private final WriteAheadLog wal;
    private final Map<String, String> data;

    public Database(WriteAheadLog wal) {
        this.wal = wal;
        data = new HashMap<>();
    }

    public void put(String key, String val) throws IOException {
        wal.append("PUT " + key + " " + val);
        data.put(key, val);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean delete(String key) throws IOException {
        if (!data.containsKey(key))
            return false;

        wal.append("DELETE " + key);
        data.remove(key);
        return true;
    }

    public void save(Path path) throws IOException {
        List<String> lines = new ArrayList<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            lines.add(entry.getKey() + "=" + entry.getValue());
        }

        Files.write(path, lines);
    }

    public void load(Path path) throws IOException {
        data.clear();
        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            String[] parts = line.split("=");
            data.put(parts[0], parts[1]);
        }
    }
}
