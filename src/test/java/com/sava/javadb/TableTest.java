package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableTest {
    private Table table;

    @BeforeEach
    void setUp() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("name"));
        cols.add(new Column("city"));
        table = new Table(cols);
    }

    @Test
    void isNewTableEmpty() {
        assertEquals(0, table.size());
    }

    @Test
    void increasesSize() {
        Row row1 = new Row();
        row1.put("name", "Sava");
        Row row2 = new Row();
        row2.put("city", "Manila");

        table.addRow(row1);
        table.addRow(row2);

        assertEquals(2, table.size());
    }

    @Test
    void returnsAddedRows() {
        Row row1 = new Row();
        row1.put("name", "Sava");
        Row row2 = new Row();
        row2.put("city", "Manila");

        table.addRow(row1);
        table.addRow(row2);

        assertEquals(row1, table.rows().get(0));
        assertEquals(row2, table.rows().get(1));
    }
}
