package com.sava.javadb;

public class SelectCmd extends Command {
    private final String tableName;

    public SelectCmd(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
