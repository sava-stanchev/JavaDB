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
            if (input.equalsIgnoreCase("exit"))
                break;
            if (input.equalsIgnoreCase("help")) {
                showHelp();
                continue;
            }

            System.out.println("You entered: " + input);
        }

        System.out.println("Goodbye!");
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("HELP - Show available commands");
        System.out.println("EXIT - Exit JavaDB");
    }
}
