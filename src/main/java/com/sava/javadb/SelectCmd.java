package com.sava.javadb;

public class SelectCmd extends Command {
    private final String tableName;
    private final String whereCol;
    private final String whereVal;

    public SelectCmd(String tableName) {
        this(tableName, null, null);
    }

    public SelectCmd(String tableName, String whereCol, String whereVal) {
        this.tableName = tableName;
        this.whereCol = whereCol;
        this.whereVal = whereVal;
    }

    public String getTableName() {
        return tableName;
    }

    public String getWhereCol() {
        return whereCol;
    }

    public String getWhereVal() {
        return whereVal;
    }
}
