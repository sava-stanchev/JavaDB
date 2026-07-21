package com.sava.javadb;

public class InsertRowCmd extends Command {
    private final String tableName;
    private final Row row;

    public InsertRowCmd(String tableName, Row row) {
        this.tableName = tableName;
        this.row = row;
    }

    public String getTableName() {
        return tableName;
    }

    public Row getRow() {
        return row;
    }
}
