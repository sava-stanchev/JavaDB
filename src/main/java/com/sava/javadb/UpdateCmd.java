package com.sava.javadb;

public class UpdateCmd extends Command {
    private final String tblName;
    private final String setCol;
    private final String setVal;
    private final String whereCol;
    private final String whereVal;

    public UpdateCmd(String tblName, String setCol, String setVal, String whereCol, String whereVal) {
        this.tblName = tblName;
        this.setCol = setCol;
        this.setVal = setVal;
        this.whereCol = whereCol;
        this.whereVal = whereVal;
    }

    public String getTableName() {
        return tblName;
    }

    public String getSetCol() {
        return setCol;
    }

    public String getSetVal() {
        return setVal;
    }

    public String getWhereCol() {
        return whereCol;
    }

    public String getWhereVal() {
        return whereVal;
    }
}
