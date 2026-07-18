package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {
    private HashTable<String, String> table;

    @BeforeEach
    void setUp() {
        table = new HashTable<>();
    }

    @Test
    void storeAndRetrieveVal() {
        table.put("name", "Sava");
        assertEquals("Sava", table.get("name"));
    }

    @Test
    void nullWhenMissingKey() {
        assertNull(table.get("age"));
    }

    @Test
    void updateExistingKey() {
        table.put("city", "Sofia");
        table.put("city", "Manila");
        assertEquals("Manila", table.get("city"));
    }

    @Test
    void removeExistingKey() {
        table.put("name", "Sava");
        assertTrue(table.remove("name"));
    }

    @Test
    void removeReturnsFalseIfMissingKey() {
        assertFalse(table.remove("x"));
    }

    @Test
    void removeValAfterDelete() {
        table.put("temp", "123");
        table.remove("temp");
        assertNull(table.get("temp"));
    }

    @Test
    void multipleEntries() {
        table.put("a", "1");
        table.put("b", "2");
        table.put("c", "3");

        assertEquals("1", table.get("a"));
        assertEquals("2", table.get("b"));
        assertEquals("3", table.get("c"));
    }

    @Test
    void handlesCollisions() {
        // same hash for all keys
        HashTable<BadKey, String> table = new HashTable<>();
        table.put(new BadKey("A"), "one");
        table.put(new BadKey("B"), "two");
        table.put(new BadKey("C"), "three");

        assertEquals("one", table.get(new BadKey("A")));
        assertEquals("two", table.get(new BadKey("B")));
        assertEquals("three", table.get(new BadKey("C")));
    }

    // forces collisions - always same hash but keys can still differ
    private static class BadKey {
        private final String key;

        BadKey(String key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            return 1; // same bucket every time
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BadKey other))
                return false;

            return key.equals(other.key);
        }
    }
}
