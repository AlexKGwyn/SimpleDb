package com.alexgwyn.simpledb;

import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedHashMap;

public abstract class TableFactory<T extends Table> {
    public abstract T buildTable(String name, LinkedHashMap<String, TableBuilder.ColumnInfo> columnInfoLinkedHashMap,
                                 SQLiteDatabase database);

}
