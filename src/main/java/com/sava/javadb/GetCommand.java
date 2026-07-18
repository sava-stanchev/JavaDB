package com.sava.javadb;

public class GetCommand extends Command {
    private final String key;

    public GetCommand(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
