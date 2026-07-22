package com.sava.javadb;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<Column> cols;
    private final List<Row> rows;

    public Table(List<Column> cols) {
        this.cols = cols;
        rows = new ArrayList<>();
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public List<Column> columns() {
        return cols;
    }

    public List<Row> rows() {
        return rows;
    }

    public int size() {
        return rows.size();
    }

    public boolean hasColumn(String name) {
        for (Column col : cols) {
            if (col.getName().equals(name))
                return true;
        }

        return false;
    }
}
