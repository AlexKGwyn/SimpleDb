package com.alexgwyn.simpledb;

import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedHashMap;

public class ObjectTableFactory<T> extends TableFactory<GsonTable<T>> {
    private Class<T> mClass;

    public ObjectTableFactory(Class<T> aClass) {
        mClass = aClass;
    }

    @Override
    public GsonTable<T> buildTable(String name, LinkedHashMap<String, TableBuilder.ColumnInfo> columns, SQLiteDatabase
            database) {
        return new GsonTable<T>(name, database, columns, mClass);
    }
}
