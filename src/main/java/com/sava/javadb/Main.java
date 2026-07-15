package com.sava.javadb;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        WriteAheadLog wal = new WriteAheadLog(Path.of("wal.log"));
        Database db = new Database(wal);

        for (String entry : wal.readEntries()) {
            db.applyWalEntry(entry);
        }

        Console console = new Console(db);
        console.start();
    }
}
