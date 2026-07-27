package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Database db;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        WriteAheadLog wal = new WriteAheadLog(tempDir.resolve("wal.log"));
        db = new Database(wal);
    }

    @Test
    void shouldStoreAndRetrieveVal() throws IOException {
        db.put("name", "Sava");
        String val = db.get("name");
        assertEquals("Sava", val);
    }

    @Test
    void shouldReturnNullForMissingKey() {
        String val = db.get("age");
        assertNull(val);
    }

    @Test
    void shouldDeleteExistingKey() throws IOException {
        db.put("name", "Sava");
        boolean isDeleted = db.delete("name");
        assertTrue(isDeleted);
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingKey() throws IOException {
        boolean isDeleted = db.delete("age");
        assertFalse(isDeleted);
    }

    @Test
    void shouldRemoveValAfterDelete() throws IOException {
        db.put("name", "Sava");
        db.delete("name");
        String val = db.get("name");
        assertNull(val);
    }

    @Test
    void shouldSaveAndLoadDatabase() throws IOException {
        db.put("name", "Sava");
        db.put("city", "Sofia");
        Path file = tempDir.resolve("database.db");
        db.save(file);
        Database db2 = new Database(new WriteAheadLog(tempDir.resolve("wal.log")));
        db2.load(file);
        assertEquals("Sava", db2.get("name"));
        assertEquals("Sofia", db2.get("city"));
    }

    @Test
    void shouldClearExistingDataBeforeLoad() throws IOException {
        db.put("name", "Sava");
        Path file = tempDir.resolve("database.db");
        db.save(file);
        Database db2 = new Database(new WriteAheadLog(tempDir.resolve("wal.log")));
        db2.put("oldKey", "oldVal");
        db2.load(file);
        assertNull(db2.get("oldKey"));
        assertEquals("Sava", db2.get("name"));
    }

    @Test
    void createAndRetrieveTable() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("name", "TEXT", true, false));
        cols.add(new Column("city", "TEXT", true, false));
        db.createTable("users", cols);
        Table tbl = db.getTable("users");
        assertNotNull(tbl);
    }

    @Test
    void nullWhenMissingTable() {
        assertNull(db.getTable("users"));
    }

    @Test
    void insertRowSuccess() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("name", "TEXT", true, false));
        cols.add(new Column("city", "TEXT", true, false));
        db.createTable("users", cols);
        Row row = new Row();
        row.put("name", "Sava");
        db.insert("users", row);

        Table users = db.getTable("users");
        assertEquals(1, users.size());
        assertEquals("Sava", users.rows().get(0).get("name"));
    }

    @Test
    void insertRowFail() {
        Row row = new Row();
        row.put("name", "Sava");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> db.insert("users", row));
        assertEquals("Table does not exist.", e.getMessage());
    }

    @Test
    void unknownCol() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("name", "TEXT", true, false));
        db.createTable("users", cols);

        Row row = new Row();
        row.put("age", "32");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> db.insert("users", row));
        assertEquals("Unknown column: age", e.getMessage());
    }

    @Test
    void throwWhenSelectMissingTable() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> db.select("users", null, null));
        assertEquals("Table does not exist.", e.getMessage());
    }

    @Test
    void selectReturnsRows() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("name", "TEXT", true, false));
        cols.add(new Column("city", "TEXT", true, false));
        db.createTable("users", cols);
        Row row = new Row();
        row.put("name", "Sava");
        db.insert("users", row);
        List<Row> rows = db.select("users", null, null);

        assertEquals(1, rows.size());
        assertEquals("Sava", rows.get(0).get("name"));
    }

    @Test
    void validIntCol() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("age", "INT", true, false));
        db.createTable("users", cols);
        Row row = new Row();
        row.put("age", "25");
        db.insert("users", row);
    }

    @Test
    void invalidIntCol() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("age", "INT", true, false));
        db.createTable("users", cols);
        Row row = new Row();
        row.put("age", "hello");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> db.insert("users", row));
        assertEquals("Invalid value for column: age", e.getMessage());
    }

    @Test
    void nullable() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("id", "INT", false, false));
        cols.add(new Column("name", "TEXT", true, false));
        db.createTable("users", cols);
        Row row = new Row();
        row.put("id", "1");
        db.insert("users", row);
    }

    @Test
    void nullableFail() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("id", "INT", false, false));
        cols.add(new Column("name", "TEXT", true, false));
        db.createTable("users", cols);
        Row row = new Row();
        row.put("name", "Sava");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> db.insert("users", row));
        assertEquals("Missing value for column: id", e.getMessage());
    }

    @Test
    void pk() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("id", "INT", false, true));
        cols.add(new Column("name", "TEXT", true, false));
        db.createTable("users", cols);
        Row row1 = new Row();
        row1.put("id", "1");
        Row row2 = new Row();
        row2.put("id", "2");
        db.insert("users", row1);
        db.insert("users", row2);
    }

    @Test
    void duplicatePk() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("id", "INT", false, true));
        cols.add(new Column("name", "TEXT", true, false));
        db.createTable("users", cols);
        Row row1 = new Row();
        row1.put("id", "1");
        Row row2 = new Row();
        row2.put("id", "1");
        db.insert("users", row1);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> db.insert("users", row2));
        assertEquals("Duplicate primary key: 1", e.getMessage());
    }

    @Test
    void selectWhereFiltering() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("id", "INT", false, false));
        cols.add(new Column("name", "TEXT", true, false));
        db.createTable("users", cols);
        Row row = new Row();
        row.put("id", "1");
        db.insert("users", row);

        List<Row> rows1 = db.select("users", "id", "1");
        assertEquals(1, rows1.size());
        assertEquals("1", rows1.get(0).get("id"));

        List<Row> rows2 = db.select("users", "id", "99");
        assertTrue(rows2.isEmpty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> db.select("users","age", "25"));
        assertEquals("Unknown column: age", e.getMessage());
    }

    @Test
    void update() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("id", "INT", false, false));
        cols.add(new Column("name", "TEXT", true, false));
        cols.add(new Column("age", "INT", false, false));
        db.createTable("users", cols);
        Row row = new Row();
        row.put("id", "1");
        row.put("name", "huh");
        row.put("age", "32");
        db.insert("users", row);

        db.update("users", "name", "Sava", "id", "1");

        List<Row> rows = db.select("users", "id", "1");
        assertEquals(1, rows.size());
        assertEquals("Sava", rows.get(0).get("name"));


        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
                () -> db.update("users", "city", "Manila", "id", "1"));
        assertEquals("Unknown column: city", e1.getMessage());

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
                () -> db.update("users", "age", "whatever", "age", "32"));
        assertEquals("Invalid value for column: age", e2.getMessage());
    }

    @Test
    void delete() {
        List<Column> cols = new ArrayList<>();
        cols.add(new Column("id", "INT", false, false));
        db.createTable("users", cols);

        Row row1 = new Row();
        row1.put("id", "1");
        db.insert("users", row1);
        Row row2 = new Row();
        row2.put("id", "2");
        db.insert("users", row2);

        db.deleteRow("users", "id", "1");

        List<Row> rows = db.select("users", null, null);
        assertEquals(1, rows.size());
        assertEquals("2", rows.get(0).get("id"));


        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
                () -> db.deleteRow("users", "age", "32"));
        assertEquals("Unknown column: age", e1.getMessage());

        db.deleteRow("users", "id", "19612");
        rows = db.select("users", null, null);
        assertEquals(1, rows.size());
    }
}
