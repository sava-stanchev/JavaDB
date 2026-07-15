package com.sava.javadb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

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
}
