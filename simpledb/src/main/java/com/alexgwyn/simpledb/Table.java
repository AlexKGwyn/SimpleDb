package com.alexkgwyn.simpledb;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.HashMap;

public interface Table<T> {
    ArrayList<T> getAll();

    ArrayList<T> get(Query query);

    T getFirst(Query query);

    long insert(T values);

    long insert(T values, ConflictMode mode);

    long update(T values, Query query);

    long update(T values);

    long updateValues(ContentValues values, Query query);

    long insertOrUpdate(T values, Query query);

    void delete(Query query);

    long replace(T values);

    HashMap<String, TableBuilder.ColumnInfo> getColumns();

    TableBuilder.ColumnInfo getColumnInfo(String name);
}
