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
        Command cmd = parser.parse("CREATE TABLE users (name TEXT, city TEXT)");
        CreateTableCmd create = assertInstanceOf(CreateTableCmd.class, cmd);
        assertEquals("users", create.getTableName());
        assertEquals("name", create.getColumns().get(0).getName());
        assertEquals("TEXT", create.getColumns().get(0).getType());
    }

    @Test
    void invalidCreateTable() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> parser.parse("CREATE users"));
        assertEquals("Usage: CREATE TABLE <name> (<column>, ...)", e.getMessage());
    }

    @Test
    void insertRow() {
        Command cmd = parser.parse("INSERT users name=Sava city=Sofia");
        InsertRowCmd insert = assertInstanceOf(InsertRowCmd.class, cmd);
        assertEquals("users", insert.getTableName());

        Row row = insert.getRow();
        assertEquals("Sava", row.get("name"));
        assertEquals("Sofia", row.get("city"));
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
