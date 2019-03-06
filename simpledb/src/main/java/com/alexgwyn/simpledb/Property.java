package com.alexgwyn.simpledb;

public enum Property {
    PRIMARY_KEY("PRIMARY KEY"), AUTO_INCREMENT("AUTOINCREMENT");

    public String sql;

    Property(String sql) {
        this.sql = sql;
    }
}