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

                    boolean nullable = true;
                    if (pieces.length == 4 &&
                            pieces[2].equalsIgnoreCase("NOT") &&
                            pieces[3].equalsIgnoreCase("NULL")) {
                        nullable = false;
                    }

                    boolean pk = false;
                    if (pieces.length == 4 &&
                            pieces[2].equalsIgnoreCase("PRIMARY") &&
                            pieces[3].equalsIgnoreCase("KEY")) {
                        pk = true;
                        nullable = false;
                    }

                    cols.add(new Column(pieces[0], pieces[1], nullable, pk));
                }

                return new CreateTableCmd(tblName, cols);
            case "INSERT":
                return parseInsert(input);
            case "SELECT":
                if (parts.length == 4 && parts[1].equals("*") && parts[2].equalsIgnoreCase("FROM"))
                    return new SelectCmd(parts[3]);
                if (parts.length == 8 && parts[1].equals("*") &&
                        parts[2].equalsIgnoreCase("FROM") &&
                        parts[4].equalsIgnoreCase("WHERE") && parts[6].equals("="))
                    return new SelectCmd(parts[3], parts[5], parts[7]);

                throw new IllegalArgumentException("Usage: SELECT * FROM <table> [WHERE <column> = <value>]");
            default:
                throw new IllegalArgumentException("Unknown command.");
        }
    }

    private InsertRowCmd parseInsert(String input) {
        String prefix = "INSERT INTO ";
        if (!input.toUpperCase().startsWith(prefix))
            throw new IllegalArgumentException("Usage: INSERT INTO <table> (<columns>) VALUES (<values>)");

        String rest = input.substring(prefix.length()).trim();
        int valsIdx = rest.toUpperCase().indexOf("VALUES");
        if (valsIdx == - 1)
            throw new IllegalArgumentException("Usage: INSERT INTO <table> (<columns>) VALUES (<values>)");

        String beforeVals = rest.substring(0, valsIdx).trim();
        String afterVals = rest.substring(valsIdx + "VALUES".length()).trim();

        int open = beforeVals.indexOf('(');
        if (open == -1)
            throw new IllegalArgumentException("Usage: INSERT INTO <table> (<columns>) VALUES (<values>)");
        String tblName = beforeVals.substring(0, open).trim();
        String colText = beforeVals.substring(open + 1, beforeVals.lastIndexOf(')'));
        if (!afterVals.startsWith("(") || !afterVals.endsWith(")"))
            throw new IllegalArgumentException("Usage: INSERT INTO <table> (<columns>) VALUES (<values>)");
        String valText = afterVals.substring(1, afterVals.length() - 1);

        String[] cols = colText.split(",");
        String[] vals = valText.split(",");
        if (cols.length != vals.length)
            throw new IllegalArgumentException("Number of columns and values must match.");

        Row row = new Row();
        for (int i = 0; i < cols.length; i++) {
            String col = cols[i].trim();
            String val = vals[i].trim();

            if (val.startsWith("'") && val.endsWith("'"))
                val = val.substring(1, val.length() - 1);

            row.put(col, val);
        }

        return new InsertRowCmd(tblName, row);
    }
}
