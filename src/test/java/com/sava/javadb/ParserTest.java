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
    void rejectUnknownCmd() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> parser.parse("HELLO"));
        assertEquals("Unknown command.", e.getMessage());
    }

    @Test
    void rejectInvalidPut() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> parser.parse("PUT name"));
        assertEquals("Usage: PUT <key> <value>", e.getMessage());
    }
}
