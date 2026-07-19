package com.sava.javadb;

public class CreateTableCmd extends Command {
    private final String tableName;

    public CreateTableCmd(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
