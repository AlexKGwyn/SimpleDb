package com.alexgwyn.simpledb;

import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedHashMap;

public abstract class QueryableBuilder<T extends Queryable> {

    private String mName;
    private LinkedHashMap<String, ColumnInfo> mColumns = new LinkedHashMap<>();

    public QueryableBuilder(String name) {
        mName = name;
    }

    public QueryableBuilder addColumn(String name, Type type, Property... properties) {
        mColumns.put(name, new ColumnInfo(type, properties));
        return this;
    }

    public abstract String toSqlString();

    public String getName() {
        return mName;
    }

    public LinkedHashMap<String, ColumnInfo> getColumns() {
        return mColumns;
    }

    public abstract T get(SQLiteDatabase database);
}
