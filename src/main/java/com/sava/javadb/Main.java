package com.sava.javadb;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        Console console = new Console(db);
        console.start();
    }
}
