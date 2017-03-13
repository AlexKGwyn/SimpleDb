package com.alexgwyn.simpledb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SimpleTable implements Table<ContentValues> {
    private String mName;
    private SQLiteDatabase mDatabase;

    public SimpleTable(String name, SQLiteDatabase database) {
        mName = name;
        mDatabase = database;
    }

    @Override
    public final ArrayList<ContentValues> getAll() {
        Cursor cursor = mDatabase.query(mName, null, null, null, null, null, null);
        return cursorToContentValuesAndClose(cursor);
    }

    @Override
    public final ArrayList<ContentValues> get(Query query) {
        Cursor cursor = mDatabase.query(mName, null, query.getWhereClause(), query
                .getSelectionClause
                        (), null, null, query.hasOrder() ? query.getOrderColumn() + " " + query.getOrder() : null, query.hasLimit() ? query.getLimit() :
                null);
        return cursorToContentValuesAndClose(cursor);
    }

    @Override
    public final ContentValues getFirst(Query query) {
        Cursor cursor = mDatabase.query(mName, null, query.getWhereClause(), query
                .getSelectionClause
                        (), null, null, query.hasOrder() ? query.getOrderColumn() + " " + query.getOrder() : null, query.hasLimit() ? query.getLimit() :
                null);
        ArrayList<ContentValues> values = cursorToContentValuesAndClose(cursor);
        return values.isEmpty() ? null : values.get(0);
    }

    @Override
    public long insert(ContentValues values) {
        return mDatabase.insert(mName, null, values);
    }

    @Override
    public long insert(ContentValues values, ConflictMode mode) {
        return mDatabase.insertWithOnConflict(mName, null, values, mode.conflictMode);
    }

    @Override
    public long update(ContentValues values, Query query) {
        return mDatabase.update(mName, values, query.getWhereClause(), query.getSelectionClause());
    }

    @Override
    public long insertOrUpdate(ContentValues values, Query query) {
        if (get(query).isEmpty()) {
            return insert(values);
        } else {
            return update(values, query);
        }
    }

    @Override
    public void delete(Query query) {
        mDatabase.delete(mName, query.getWhereClause(), query.getSelectionClause());
    }

    @Override
    public long replace(ContentValues values) {
        return mDatabase.replace(mName, null, values);
    }

    private ArrayList<ContentValues> cursorToContentValuesAndClose(Cursor cursor) {
        ArrayList<ContentValues> values = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ContentValues contentValues = new ContentValues(cursor.getColumnCount());
                DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
                values.add(contentValues);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return values;
    }

}