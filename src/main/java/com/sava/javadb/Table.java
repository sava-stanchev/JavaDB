package com.sava.javadb;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<Row> rows;

    public Table() {
        rows = new ArrayList<>();
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public List<Row> rows() {
        return rows;
    }

    public int size() {
        return rows.size();
    }
}
