package com.alexgwyn.simpledb;

public enum Type {
    TEXT("TEXT"), INTEGER("INTEGER"), DOUBLE("REAL"), BOOLEAN("INTEGER"), JSON("TEXT");

    public String sql;

    Type(String sql) {
        this.sql = sql;
    }

}