package com.alexgwyn.simpledb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class SimpleDb<T extends Table> extends SQLiteOpenHelper {
    private TableFactory<T> mTableFactory;

    public SimpleDb(Context context, String name, int version, TableFactory<T> factory) {
        super(context, name, null, version);
        mTableFactory = factory;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        TableBuilder[] tableBuilders = onCreate();
        for (TableBuilder tableBuilder : tableBuilders) {
            sqLiteDatabase.execSQL(tableBuilder.toSqlString());
        }
    }

    public abstract TableBuilder[] onCreate();

    public T getTable(String name) {
        return mTableFactory.buildTable(name, getWritableDatabase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
