package com.sava.javadb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        assertNull(select.getWhereCol());
        assertNull(select.getWhereVal());
    }

    @Test
    void selectWithWhere() {
        Command cmd = parser.parse("SELECT * FROM users WHERE id = 1");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("users", select.getTableName());
        assertEquals("id", select.getWhereCol());
        assertEquals("1", select.getWhereVal());
    }

    @Test
    void selectAll() {
        Command cmd = parser.parse("SELECT * FROM users");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("users", select.getTableName());
        assertEquals(List.of("*"), select.getCols());
        assertNull(select.getWhereCol());
    }

    @Test
    void selectCol() {
        Command cmd = parser.parse("SELECT id FROM users");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("users", select.getTableName());
        assertEquals(List.of("id"), select.getCols());
        assertNull(select.getWhereCol());
    }

    @Test
    void selectCols() {
        Command cmd = parser.parse("SELECT id, name FROM users");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("users", select.getTableName());
        assertEquals(2, select.getCols().size());
        assertEquals("id", select.getCols().get(0));
        assertEquals("name", select.getCols().get(1));
    }

    @Test
    void selectColsWhere() {
        Command cmd = parser.parse("SELECT id, name FROM users WHERE id = 1");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("users", select.getTableName());
        assertEquals(List.of("id", "name"), select.getCols());
        assertEquals("id", select.getWhereCol());
        assertEquals("1", select.getWhereVal());
    }

    @Test
    void selectGt() {
        Command cmd = parser.parse("SELECT * FROM users WHERE age > 18");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("age", select.getWhereCol());
        assertEquals(">", select.getWhereOp());
        assertEquals("18", select.getWhereVal());
    }

    @Test
    void selectGe() {
        Command cmd = parser.parse("SELECT * FROM users WHERE age >= 18");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("age", select.getWhereCol());
        assertEquals(">=", select.getWhereOp());
        assertEquals("18", select.getWhereVal());
    }

    @Test
    void selectLt() {
        Command cmd = parser.parse("SELECT * FROM users WHERE age < 65");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("age", select.getWhereCol());
        assertEquals("<", select.getWhereOp());
        assertEquals("65", select.getWhereVal());
    }

    @Test
    void selectLe() {
        Command cmd = parser.parse("SELECT * FROM users WHERE age <= 65");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals("age", select.getWhereCol());
        assertEquals("<=", select.getWhereOp());
        assertEquals("65", select.getWhereVal());
    }

    @Test
    void selectLim() {
        Command cmd = parser.parse("SELECT * FROM users LIMIT 5");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals(Integer.valueOf(5), select.getLimit());
    }

    @Test
    void selectWhereLim() {
        Command cmd = parser.parse("SELECT * FROM users WHERE age > 18 LIMIT 2");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertEquals(Integer.valueOf(2), select.getLimit());
    }

    @Test
    void selectNoLim() {
        Command cmd = parser.parse("SELECT * FROM users");
        SelectCmd select = assertInstanceOf(SelectCmd.class, cmd);
        assertNull(select.getLimit());
    }

    @Test
    void invalidSelect() {
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> parser.parse("SELECT users"));
        assertEquals("Usage: SELECT <columns> FROM <table> [WHERE <column> <op> <value>] [LIMIT <count>]", e1.getMessage());

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
                () -> parser.parse("SELECT * FROM users LIMIT"));
        assertEquals("Usage: SELECT <columns> FROM <table> [WHERE <column> <op> <value>] [LIMIT <count>]", e2.getMessage());

        IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class,
                () -> parser.parse("SELECT * FROM users WHERE id"));
        assertEquals("Usage: SELECT <columns> FROM <table> [WHERE <column> <op> <value>] [LIMIT <count>]", e3.getMessage());
    }

    @Test
    void validUpdate() {
        Command cmd = parser.parse("UPDATE users SET name = 'Sava' WHERE id = 1");
        UpdateCmd update = assertInstanceOf(UpdateCmd.class, cmd);
        assertEquals("users", update.getTableName());
        assertEquals("name", update.getSetCol());
        assertEquals("Sava", update.getSetVal());
        assertEquals("id", update.getWhereCol());
        assertEquals("1", update.getWhereVal());
    }

    @Test
    void invalidUpdate() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> parser.parse("UPDATE users name = 'Sava'"));
        assertEquals("Usage: UPDATE <table> SET <column> = <value> WHERE <column> = <value>", e.getMessage());
    }

    @Test
    void validDelete() {
        Command cmd = parser.parse("DELETE FROM users WHERE id = 1");
        DeleteRowCmd delete = assertInstanceOf(DeleteRowCmd.class, cmd);
        assertEquals("users", delete.getTableName());
        assertEquals("id", delete.getWhereCol());
        assertEquals("1", delete.getWhereVal());
    }

    @Test
    void invalidDelete() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> parser.parse("DELETE users"));
        assertEquals("Usage: DELETE FROM <table> WHERE <column> = <value>", e.getMessage());
    }
}
