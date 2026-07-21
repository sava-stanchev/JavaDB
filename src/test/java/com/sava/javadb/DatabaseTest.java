package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
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
        db.createTable("users");
        Table tbl = db.getTable("users");
        assertNotNull(tbl);
    }

    @Test
    void nullWhenMissingTable() {
        assertNull(db.getTable("users"));
    }

    @Test
    void insertRowSuccess() {
        db.createTable("users");
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
    void throwWhenSelectMissingTable() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> db.select("users"));
        assertEquals("Table does not exist.", e.getMessage());
    }

    @Test
    void selectReturnsRows() {
        db.createTable("users");
        Row row = new Row();
        row.put("name", "Sava");
        db.insert("users", row);
        List<Row> rows = db.select("users");

        assertEquals(1, rows.size());
        assertEquals("Sava", rows.get(0).get("name"));
    }
}
