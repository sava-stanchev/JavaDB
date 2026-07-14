package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Database db;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        db = new Database();
    }

    @Test
    void shouldStoreAndRetrieveVal() {
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
    void shouldDeleteExistingKey() {
        db.put("name", "Sava");
        boolean isDeleted = db.delete("name");
        assertTrue(isDeleted);
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingKey() {
        boolean isDeleted = db.delete("age");
        assertFalse(isDeleted);
    }

    @Test
    void shouldRemoveValAfterDelete() {
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
        Database db2 = new Database();
        db2.load(file);
        assertEquals("Sava", db2.get("name"));
        assertEquals("Sofia", db2.get("city"));
    }

    @Test
    void shouldClearExistingDataBeforeLoad() throws IOException {
        db.put("name", "Sava");
        Path file = tempDir.resolve("database.db");
        db.save(file);
        Database db2 = new Database();
        db2.put("oldKey", "oldVal");
        db2.load(file);
        assertNull(db2.get("oldKey"));
        assertEquals("Sava", db2.get("name"));
    }
}
