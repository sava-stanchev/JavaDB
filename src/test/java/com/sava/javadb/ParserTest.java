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
        assertThrows(IllegalArgumentException.class, () -> parser.parse("HELLO"));
    }

    @Test
    void rejectInvalidPut() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("PUT name"));
    }
}
