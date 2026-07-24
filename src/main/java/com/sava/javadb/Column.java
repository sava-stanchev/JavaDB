package com.sava.javadb;

public class Column {
    private final String name;
    private final String type;

    public Column(String name, String type) {
        this.name = name;
        this.type = type.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isValid(String val) {
        switch (type) {
            case "TEXT":
                return true;
            case "INT":
                try {
                    Integer.parseInt(val);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                throw new IllegalArgumentException("Unknown column type: " + type);
        }
    }
}
