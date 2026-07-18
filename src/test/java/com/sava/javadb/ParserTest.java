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

}
