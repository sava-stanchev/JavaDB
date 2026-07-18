package com.sava.javadb;

public class Parser {
    public Command parse(String input) {
        String[] parts = input.split("\\s+");
        String cmd = parts[0].toUpperCase();

        if (cmd.equals("PUT") && parts.length == 3) {
            return new PutCommand(parts[1], parts[2]);
        } else {
            throw new IllegalArgumentException("Invalid command");
        }
    }
}
