package com.alexkgwyn.simpledb;

import android.database.sqlite.SQLiteDatabase;

public enum ConflictMode {
    ABORT(SQLiteDatabase.CONFLICT_ABORT), REPLACE(SQLiteDatabase.CONFLICT_REPLACE), IGNORE(SQLiteDatabase
            .CONFLICT_IGNORE), NONE(SQLiteDatabase.CONFLICT_NONE);
    int conflictMode;

    ConflictMode(int conflictMode) {
        this.conflictMode = conflictMode;
    }
}