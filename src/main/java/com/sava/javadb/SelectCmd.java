package com.sava.javadb;

import java.util.List;

public class SelectCmd extends Command {
    private final String tableName;
    private final String whereCol;
    private final String whereOp;
    private final String whereVal;
    private final List<String> cols;
    private final Integer lim;

    public SelectCmd(List<String> cols, String tableName,
                     String whereCol, String whereOp, String whereVal, Integer lim) {
        this.cols = cols;
        this.tableName = tableName;
        this.whereCol = whereCol;
        this.whereVal = whereVal;
        this.whereOp = whereOp;
        this.lim = lim;
    }

    public String getTableName() {
        return tableName;
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

    public List<String> getCols() {
        return cols;
    }

    public Integer getLimit() {
        return lim;
    }
}
