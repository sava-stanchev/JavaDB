package com.sava.javadb;

import java.util.Scanner;

public class Console {
    private final Database db;
    private final Scanner sc;

    public Console(Database db) {
        this.db = db;
        sc = new Scanner(System.in);
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
                    if (parts.length == 3) {
                        db.put(parts[1], parts[2]);
                        System.out.println("OK");
                    } else {
                        System.out.println("Usage: PUT <key> <value>");
                    }

                    break;
                case "GET":
                    if (parts.length == 2) {
                        String val = db.get(parts[1]);
                        System.out.println(val != null ? val : "Key not found.");
                    } else {
                        System.out.println("Usage: GET <key>");
                    }

                    break;
                case "DELETE":
                    if (parts.length == 2) {
                        boolean isDeleted = db.delete(parts[1]);
                        System.out.println(isDeleted ? "OK" : "Key not found.");
                    } else {
                        System.out.println("Usage: DELETE <key>");
                    }

                    break;
                case "HELP":
                    showHelp();
                    continue;
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
        System.out.println("HELP - Show available commands");
        System.out.println("EXIT - Exit JavaDB");
    }
}
