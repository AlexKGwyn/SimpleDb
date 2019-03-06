package com.alexgwyn.simpledb;

import android.content.ContentValues;

import java.util.HashMap;

public interface Table<T> extends Queryable<T> {
    long insert(T values);

    long insert(T values, ConflictMode mode);

    long update(T values, Query query);

    long update(T values);

    long updateValues(ContentValues values, Query query);

    long insertOrUpdate(T values, Query query);

    void delete(Query query);

    long replace(T values);

    HashMap<String, ColumnInfo> getColumns();

    ColumnInfo getColumnInfo(String name);
}
