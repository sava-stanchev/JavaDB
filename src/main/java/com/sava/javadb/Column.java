package com.sava.javadb;

public class Column {
    private final String name;
    private final String type;
    private final boolean nullable;
    private final boolean pk;

    public Column(String name, String type, boolean nullable, boolean pk) {
        this.name = name;
        this.type = type.toUpperCase();
        this.nullable = nullable;
        this.pk = pk;
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

    public boolean isNullable() {
        return nullable;
    }

    public boolean isPk() {
        return pk;
    }
}
