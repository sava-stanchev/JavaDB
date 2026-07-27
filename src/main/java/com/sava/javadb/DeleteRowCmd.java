package com.sava.javadb;

public class DeleteRowCmd extends Command {
    private final String tblName;
    private final String whereCol;
    private final String whereVal;

    public DeleteRowCmd(String tblName, String whereCol, String whereVal) {
        this.tblName = tblName;
        this.whereCol = whereCol;
        this.whereVal = whereVal;
    }

    public String getTableName() {
        return tblName;
    }

    public String getWhereCol() {
        return whereCol;
    }

    public String getWhereVal() {
        return whereVal;
    }
}
