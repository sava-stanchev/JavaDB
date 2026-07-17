package com.sava.javadb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static final Path DB_FILE = Path.of("database.db");
    private final WriteAheadLog wal;
    private final HashTable<String, String> data;

    public Database(WriteAheadLog wal) {
        this.wal = wal;
        data = new HashTable<>();
    }

    public void put(String key, String val) throws IOException {
        wal.append("PUT " + key + " " + val);
        applyPut(key, val);
    }

    private void applyPut(String key, String val) {
        data.put(key, val);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean delete(String key) throws IOException {
        if (data.get(key) == null)
            return false;

        wal.append("DELETE " + key);
        return applyDelete(key);
    }

    private boolean applyDelete(String key) {
        return data.remove(key);
    }

    public void save() throws IOException {
        save(DB_FILE);
    }

    public void save(Path path) throws IOException {
        List<String> lines = new ArrayList<>();

        for (Entry<String, String> e : data.entries()) {
            lines.add(e.getKey() + "=" + e.getValue());
        }

        Files.write(path, lines);
    }

    public void load() throws IOException {
        load(DB_FILE);
    }

    public void load(Path path) throws IOException {
        data.clear();
        if (!Files.exists(path))
            return;
        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            String[] parts = line.split("=");
            data.put(parts[0], parts[1]);
        }
    }

    public void applyWalEntry(String entry) {
        String[] parts = entry.split(" ");
        String cmd = parts[0].toUpperCase();

        switch (cmd) {
            case "PUT":
                applyPut(parts[1], parts[2]);
                break;
            case "DELETE":
                applyDelete(parts[1]);
                break;
            default:
                throw new IllegalArgumentException("Invalid WAL entry: " + entry);
        }
    }

    public void checkpoint() throws IOException {
        save(DB_FILE);
        wal.clear();
    }
}
