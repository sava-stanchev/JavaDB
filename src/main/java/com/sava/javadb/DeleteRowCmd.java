package com.sava.javadb;

public class DeleteRowCmd extends Command {
    private final String tblName;
    private final String whereCol;
    private final String whereOp;
    private final String whereVal;

    public DeleteRowCmd(String tblName, String whereCol, String whereOp, String whereVal) {
        this.tblName = tblName;
        this.whereCol = whereCol;
        this.whereOp = whereOp;
        this.whereVal = whereVal;
    }

    public String getTableName() {
        return tblName;
    }

    public String getWhereCol() {
        return whereCol;
    }

    public String getWhereOp() {
        return whereOp;
    }

    public String getWhereVal() {
        return whereVal;
    }
}
