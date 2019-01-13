package com.alexkgwyn.simpledb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleTable implements Table<ContentValues> {
    private String mName;
    private SQLiteDatabase mDatabase;
    private LinkedHashMap<String, TableBuilder.ColumnInfo> mColumns;

    public SimpleTable(String name, SQLiteDatabase database, LinkedHashMap<String, TableBuilder.ColumnInfo> columns) {
        mName = name;
        mDatabase = database;
        mColumns = columns;
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
                        (), null, null, query.hasOrder() ? query.getOrder() : null, query.hasLimit() ? query.getLimit() :
                null);
        return cursorToContentValuesAndClose(cursor);
    }

    @Override
    public final ContentValues getFirst(Query query) {
        Cursor cursor = mDatabase.query(mName, null, query.getWhereClause(), query
                .getSelectionClause
                        (), null, null, query.hasOrder() ? query.getOrder() : null, "1");
        ArrayList<ContentValues> values = cursorToContentValuesAndClose(cursor);
        return values.isEmpty() ? null : values.get(0);
    }

    @Override
    public long insert(ContentValues values) {
        return mDatabase.insert(mName, null, removeAutoValues(values));
    }

    @Override
    public long insert(ContentValues values, ConflictMode mode) {
        return mDatabase.insertWithOnConflict(mName, null, removeAutoValues(values), mode.conflictMode);
    }

    @Override
    public long update(ContentValues values, Query query) {
        return mDatabase.update(mName, values, query.getWhereClause(), query.getSelectionClause());
    }

    @Override
    public long update(ContentValues values) {
        Query query = new Query();
        boolean valid = false;
        for (Map.Entry<String, TableBuilder.ColumnInfo> entry : mColumns.entrySet()) {
            if (entry.getValue().hasProperty(TableBuilder.Property.PRIMARY_KEY)) {
                query.addSelection(entry.getKey(), Selection.Operator.EQUAL, values.get(entry.getKey()));
                valid = values.containsKey(entry.getKey());
            }
        }
        if (!valid) {
            throw new IllegalStateException("Primary key must be defined for simple update: "+values.toString());
        }
        return mDatabase.update(mName, values, query.getWhereClause(), query.getSelectionClause());
    }

    @Override
    public long updateValues(ContentValues values, Query query) {
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

    @Override
    public HashMap<String, TableBuilder.ColumnInfo> getColumns() {
        return mColumns;
    }

    @Override
    public TableBuilder.ColumnInfo getColumnInfo(String name) {
        return mColumns.get(name);
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

    private ContentValues removeAutoValues(ContentValues values) {
        ArrayList<String> removal = new ArrayList<>();
        for (Map.Entry<String, TableBuilder.ColumnInfo> entry : mColumns.entrySet()) {
            if (entry.getValue().hasProperty(TableBuilder.Property.AUTO_INCREMENT)) {
                removal.add(entry.getKey());
            }
        }
        for (String s : removal) {
            values.remove(s);
        }
        return values;
    }

}