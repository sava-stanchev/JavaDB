package com.sava.javadb;

import java.nio.file.Path;

public class WriteAheadLog {
    private final Path path;

    public WriteAheadLog(Path path) {
        this.path = path;
    }
}
