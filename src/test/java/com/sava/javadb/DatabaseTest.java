package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Database db;

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
}
