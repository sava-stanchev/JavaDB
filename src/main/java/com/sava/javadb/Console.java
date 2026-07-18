package com.sava.javadb;

import java.io.IOException;
import java.util.Scanner;

public class Console {
    private final Database db;
    private final Scanner sc;
    private final Parser parser;

    public Console(Database db) {
        this.db = db;
        sc = new Scanner(System.in);
        this.parser = new Parser();
    }

    public void start() {
        System.out.println("Welcome to JavaDB!");
        System.out.println("Type EXIT to quit.");

        while (true) {
            System.out.print("> ");
            String input = sc.nextLine().trim();
            if (input.isEmpty())
                continue;
            String[] parts = input.split("\\s+");
            String cmd = parts[0].toUpperCase();

            switch (cmd) {
                case "EXIT":
                    System.out.println("Goodbye!");
                    return;
                case "HELP":
                    showHelp();
                    break;
                case "SAVE":
                    saveDb();
                    break;
                case "LOAD":
                    loadDb();
                    break;
                case "CHECKPOINT":
                    checkpointDb();
                    break;
                default:
                    try {
                        Command command = parser.parse(input);

                        if (command instanceof PutCommand put) {
                            handlePut(put);
                        } else if (command instanceof GetCommand get) {
                            handleGet(get);
                        } else if (command instanceof DeleteCommand delete) {
                            handleDelete(delete);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
            }
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("PUT <key> <value> - Store a value");
        System.out.println("GET <key> - Retrieve a value");
        System.out.println("DELETE <key> - Delete a key");
        System.out.println("SAVE - Save database to disk");
        System.out.println("LOAD - Load database from disk");
        System.out.println("CHECKPOINT - Save snapshot and clear WAL");
        System.out.println("HELP - Show available commands");
        System.out.println("EXIT - Exit JavaDB");
    }

    private void handlePut(PutCommand put) {
        try {
            db.put(put.getKey(), put.getValue());
            System.out.println("OK");
        } catch (IOException e) {
            System.out.println("PUT failed: " + e.getMessage());
        }
    }

    private void handleGet(GetCommand get) {
        String val = db.get(get.getKey());
        System.out.println(val != null ? val : "Key not found.");
    }

    private void handleDelete(DeleteCommand delete) {
        try {
            boolean isDeleted = db.delete(delete.getKey());
            System.out.println(isDeleted ? "OK" : "Key not found.");
        } catch (IOException e) {
            System.out.println("DELETE failed: " + e.getMessage());
        }
    }

    private void saveDb() {
        try {
            db.save();
            System.out.println("OK");
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private void loadDb() {
        try {
            db.load();
            System.out.println("OK");
        } catch (IOException e) {
            System.out.println("Load failed: " + e.getMessage());
        }
    }

    private void checkpointDb() {
        try {
            db.checkpoint();
            System.out.println("OK");
        } catch (IOException e) {
            System.out.println("Checkpoint failed: " + e.getMessage());
        }
    }
}
