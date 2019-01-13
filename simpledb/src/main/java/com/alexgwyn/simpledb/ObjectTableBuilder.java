package com.alexkgwyn.simpledb;

import android.database.sqlite.SQLiteDatabase;

public class ObjectTableBuilder<T> extends TableBuilder<GsonTable<T>> {
    private Class<T> mClass;

    public ObjectTableBuilder(String name, Class<T> objectClass) {
        super(name);
        mClass = objectClass;
    }

    public GsonTable<T> getTable(SQLiteDatabase database) {
        return new GsonTable(getName(), database, getColumns(), mClass);
    }
}
