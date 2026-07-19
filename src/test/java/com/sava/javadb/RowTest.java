package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RowTest {
    private Row row;

    @BeforeEach
    void setUp() {
        row = new Row();
    }

    @Test
    void storeAndRetrieveCol() {
        row.put("name", "Sava");
        assertEquals("Sava", row.get("name"));
    }

    @Test
    void nullWhenMissingCol() {
        assertNull(row.get("age"));
    }

    @Test
    void updateExistingCol() {
        row.put("city", "Sofia");
        row.put("city", "Manila");
        assertEquals("Manila", row.get("city"));
    }

    @Test
    void removeExistingCol() {
        row.put("name", "Sava");
        assertTrue(row.remove("name"));
    }

    @Test
    void removeReturnsFalseIfMissingCol() {
        assertFalse(row.remove("x"));
    }

    @Test
    void removeValAfterDelete() {
        row.put("temp", "123");
        row.remove("temp");
        assertNull(row.get("temp"));
    }
}
