package com.sava.javadb;

import java.util.List;

public class CreateTableCmd extends Command {
    private final String tableName;
    private final List<Column> cols;

    public CreateTableCmd(String tableName, List<Column> cols) {
        this.tableName = tableName;
        this.cols = cols;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumns() {
        return cols;
    }
}
