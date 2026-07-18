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
                case "PUT":
                    handlePut(input);
                    break;
                case "GET":
                    handleGet(input);
                    break;
                case "DELETE":
                    handleDelete(parts);
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
                case "HELP":
                    showHelp();
                    break;
                case "EXIT":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Unknown command.");
                    System.out.println("Type HELP to see available commands.");
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

    private void handlePut(String input) {
        try {
            Command cmd = parser.parse(input);

            if (cmd instanceof PutCommand put) {
                db.put(put.getKey(), put.getValue());
                System.out.println("OK");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("PUT failed: " + e.getMessage());
        }
    }

    private void handleGet(String input) {
        try {
            Command cmd = parser.parse(input);

            if (cmd instanceof GetCommand get) {
                String val = db.get(get.getKey());
                System.out.println(val != null ? val : "Key not found.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDelete(String[] parts) {
        if (parts.length == 2) {
            try {
                boolean isDeleted = db.delete(parts[1]);
                System.out.println(isDeleted ? "OK" : "Key not found.");
            } catch (IOException e) {
                System.out.println("DELETE failed: " + e.getMessage());
            }
        } else {
            System.out.println("Usage: DELETE <key>");
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
