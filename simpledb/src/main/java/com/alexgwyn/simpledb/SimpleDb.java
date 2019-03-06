package com.alexgwyn.simpledb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public abstract class SimpleDb<T extends Table> extends SQLiteOpenHelper {
    private HashMap<String, QueryableBuilder<T>> mQueryableMap = new HashMap<>();

    public SimpleDb(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        TableBuilder[] tableBuilders = getTableBuilders();
        for (TableBuilder tableBuilder : tableBuilders) {
            mQueryableMap.put(tableBuilder.getName(), tableBuilder);
            sqLiteDatabase.execSQL(tableBuilder.toSqlString());
        }
    }

    public abstract TableBuilder<T>[] getTableBuilders();

    public T getTable(String name) {
        return getTable(name, getWritableDatabase());
    }

    protected T getTable(String name, SQLiteDatabase database) {
        if (mQueryableMap.isEmpty()) {
            TableBuilder[] tableBuilders = getTableBuilders();
            for (TableBuilder tableBuilder : tableBuilders) {
                mQueryableMap.put(tableBuilder.getName(), tableBuilder);
            }
        }
        return mQueryableMap.get(name).get(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
