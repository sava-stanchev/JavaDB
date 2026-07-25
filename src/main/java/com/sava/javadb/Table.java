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

    public Column getCol(String name) {
        for (Column col : cols) {
            if (col.getName().equals(name))
                return col;
        }

        return null;
    }

    public Column getPk() {
        for (Column col : cols) {
            if (col.isPk())
                return col;
        }

        return null;
    }
}
