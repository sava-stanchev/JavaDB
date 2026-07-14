package com.sava.javadb;

public class Main {
    public static void main(String[] args) {
        WriteAheadLog wal = new WriteAheadLog();
        Database db = new Database(wal);
        Console console = new Console(db);
        console.start();
    }
}
