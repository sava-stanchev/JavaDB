package com.sava.javadb;

public class DeleteCommand extends Command {
    private final String key;

    public DeleteCommand(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
