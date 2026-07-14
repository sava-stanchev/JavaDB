package com.sava.javadb;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        WriteAheadLog wal = new WriteAheadLog(Path.of("wal.log"));
        Database db = new Database(wal);
        Console console = new Console(db);
        console.start();
    }
}
