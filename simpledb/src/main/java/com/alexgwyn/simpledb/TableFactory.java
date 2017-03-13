package com.alexgwyn.simpledb;

import android.database.sqlite.SQLiteDatabase;

public abstract class TableFactory<T extends Table> {
    public abstract T buildTable(String name, SQLiteDatabase database);
}
