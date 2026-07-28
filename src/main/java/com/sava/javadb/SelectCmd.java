package com.sava.javadb;

import java.util.List;

public class SelectCmd extends Command {
    private final String tableName;
    private final String whereCol;
    private final String whereVal;
    private final List<String> cols;

    public SelectCmd(List<String> cols, String tableName) {
        this(cols, tableName, null, null);
    }

    public SelectCmd(List<String> cols, String tableName, String whereCol, String whereVal) {
        this.cols = cols;
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

    public List<String> getCols() {
        return cols;
    }
}
