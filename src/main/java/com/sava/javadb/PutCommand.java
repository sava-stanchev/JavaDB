package com.sava.javadb;

public class PutCommand extends Command {
    private final String key;
    private final String val;

    public PutCommand(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return val;
    }
}
