package com.sava.javadb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class WriteAheadLog {
    private final Path path;

    public WriteAheadLog(Path path) {
        this.path = path;
    }

    public void append(String entry) throws IOException {
        Files.writeString(
                path,
                entry + System.lineSeparator(),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
        );
    }

    public List<String> readEntries() throws IOException {
        if (!Files.exists(path))
            return new ArrayList<>();

        return Files.readAllLines(path);
    }

    public void clear() throws IOException {
        Files.deleteIfExists(path);
    }
}
