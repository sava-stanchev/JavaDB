package com.sava.javadb;

import java.util.ArrayList;
import java.util.List;

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
            case "CREATE":
                if (parts.length < 4 || !parts[1].equalsIgnoreCase("TABLE"))
                    throw new IllegalArgumentException("Usage: CREATE TABLE <name> (<column>, ...)");

                String rest = input.substring("CREATE TABLE".length()).trim();
                int open = rest.indexOf('(');
                int close = rest.indexOf(')');
                String tblName = rest.substring(0, open).trim();
                String colText = rest.substring(open + 1, close);
                String[] names = colText.split(",");
                List<Column> cols = new ArrayList<>();

                for (String name : names) {
                    String[] pieces = name.trim().split("\\s+");
                    cols.add(new Column(pieces[0], pieces[1]));
                }

                return new CreateTableCmd(tblName, cols);
            case "INSERT":
                if (parts.length < 3)
                    throw new IllegalArgumentException("Usage: INSERT <table> <column=value>...");

                String tableName = parts[1];
                Row row = new Row();

                for (int i = 2; i < parts.length; i++) {
                    String[] pair = parts[i].split("=");
                    if (pair.length != 2 || pair[0].isBlank() || pair[1].isBlank())
                        throw new IllegalArgumentException("Columns must be in the form column=value");
                    row.put(pair[0], pair[1]);
                }

                return new InsertRowCmd(tableName, row);
            case "SELECT":
                if (parts.length != 4 || !parts[1].equals("*") || !parts[2].equalsIgnoreCase("FROM"))
                    throw new IllegalArgumentException("Usage: SELECT * FROM <table>");
                return new SelectCmd(parts[3]);
            default:
                throw new IllegalArgumentException("Unknown command.");
        }
    }
}
