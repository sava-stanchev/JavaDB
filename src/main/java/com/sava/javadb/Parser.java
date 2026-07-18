package com.sava.javadb;

public class Parser {
    public Command parse(String input) {
        String[] parts = input.split("\\s+");
        String cmd = parts[0].toUpperCase();

        if (!cmd.equals("PUT"))
            throw new IllegalArgumentException("Unknown command.");
        if (parts.length != 3)
            throw new IllegalArgumentException("Usage: PUT <key> <value>");

        return new PutCommand(parts[1], parts[2]);
    }
}
