package com.sava.javadb;

public class Parser {
    public Command parse(String input) {
        String[] parts = input.split("\\s+");
        String cmd = parts[0].toUpperCase();

        switch (cmd) {
            case "PUT":
                if (parts.length != 3)
                    throw new IllegalArgumentException("Usage: PUT <key> <value>");
                return new PutCommand(parts[1], parts[2]);
            case "GET":
                if (parts.length != 2)
                    throw new IllegalArgumentException("Usage: GET <key>");
                return new GetCommand(parts[1]);
            case "DELETE":
                if (parts.length != 2)
                    throw new IllegalArgumentException("Usage: DELETE <key>");
                return new DeleteCommand(parts[1]);
            default:
                throw new IllegalArgumentException("Unknown command.");
        }
    }
}
