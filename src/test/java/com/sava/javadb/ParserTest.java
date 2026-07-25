package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void parseValidPut() {
        Command cmd = parser.parse("PUT name Sava");
        PutCommand put = assertInstanceOf(PutCommand.class, cmd);
        assertEquals("name", put.getKey());
        assertEquals("Sava", put.getValue());
    }

    @Test
    void rejectInvalidPut() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> parser.parse("PUT name"));
        assertEquals("Usage: PUT <key> <value>", e.getMessage());
    }

    @Test
    void parseValidGet() {
        Command cmd = parser.parse("GET name");
        GetCommand get = assertInstanceOf(GetCommand.class, cmd);
        assertEquals("name", get.getKey());
    }

    @Test
    void parseValidDelete() {
        Command cmd = parser.parse("DELETE name");
        DeleteCommand delete = assertInstanceOf(DeleteCommand.class, cmd);
        assertEquals("name", delete.getKey());
    }

    @Test
    void rejectUnknownCmd() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> parser.parse("HELLO"));
        assertEquals("Unknown command.", e.getMessage());
    }

    @Test
    void createTable() {
        Command cmd = parser.parse("CREATE TABLE users (name TEXT NOT NULL, age INT)");
        CreateTableCmd create = assertInstanceOf(CreateTableCmd.class, cmd);
        assertEquals("users", create.getTableName());
        assertEquals("name", create.getCols().get(0).getName());
        assertEquals("TEXT", create.getCols().get(0).getType());
        assertFalse(create.getCols().get(0).isNullable());
        assertTrue(create.getCols().get(1).isNullable());
    }

    @Test
    void createTableWithPk() {
        Command cmd = parser.parse("CREATE TABLE users (id INT PRIMARY KEY, name TEXT)");
        CreateTableCmd create = assertInstanceOf(CreateTableCmd.class, cmd);
        assertEquals("users", create.getTableName());
        assertTrue(create.getCols().get(0).isPk());
        assertFalse(create.getCols().get(0).isNullable());
    }

    @Test
    void invalidCreateTable() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> parser.parse("CREATE users"));
        assertEquals("Usage: CREATE TABLE <name> (<column>, ...)", e.getMessage());
    }

    @Test
    void insertRow() {
        Command cmd = parser.parse("INSERT INTO users (id, name) VALUES (1, 'Sava')");
        InsertRowCmd insert = assertInstanceOf(InsertRowCmd.class, cmd);
        assertEquals("users", insert.getTableName());

        Row row = insert.getRow();
        assertEquals("1", row.get("id"));
        assertEquals("Sava", row.get("name"));
    }

    @Test
    void insertCmdWrongUsage() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> parser.parse("INSERT users (id, name) VALUES (1, 'Sava')"));
        assertEquals("Usage: INSERT INTO <table> (<columns>) VALUES (<values>)", e.getMessage());
    }

    @Test
    void insertCmdColValMismatch() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> parser.parse("INSERT INTO users (id, name, age) VALUES (1, 'Sava')"));
        assertEquals("Number of columns and values must match.", e.getMessage());
    }

    @Test
    void validSelect() {
        Command cmd = parser.parse("SELECT * FROM users");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("users", select.getTableName());
    }

    @Test
    void invalidSelect() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> parser.parse("SELECT users"));
        assertEquals("Usage: SELECT * FROM <table>", e.getMessage());
    }
}
