package com.alexkgwyn.simpledb;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonTable implements Table<JsonObject> {

    private SimpleTable mSimpleTable;
    private JsonParser mJsonParser = new JsonParser();
    public JsonTable(String name, SQLiteDatabase database, LinkedHashMap<String, TableBuilder.ColumnInfo> columns) {
        mSimpleTable = new SimpleTable(name, database, columns);
    }

    public ArrayList<JsonObject> getAll() {
        ArrayList<JsonObject> ret = new ArrayList<>();
        ArrayList<ContentValues> values = mSimpleTable.getAll();
        for (ContentValues value : values) {
            ret.add(toJsonObject(value));
        }
        return ret;
    }

    public ArrayList<JsonObject> get(Query query) {
        ArrayList<JsonObject> ret = new ArrayList<>();
        ArrayList<ContentValues> values = mSimpleTable.get(query);
        for (ContentValues value : values) {
            ret.add(toJsonObject(value));
        }
        return ret;
    }

    public JsonObject getFirst(Query query) {
        ContentValues values = mSimpleTable.getFirst(query);
        if(values != null) {
            return toJsonObject(values);
        }
        return null;
    }

    public long insert(JsonObject object) {
        return mSimpleTable.insert(fromJsonObject(object));
    }

    public long insert(JsonObject object, ConflictMode mode) {
        return mSimpleTable.insert(fromJsonObject(object), mode);
    }

    public long update(JsonObject object, Query query) {
        return mSimpleTable.update(fromJsonObject(object), query);
    }

    public long update(JsonObject object) {
        return mSimpleTable.update(fromJsonObject(object));
    }

    @Override
    public long updateValues(ContentValues values, Query query) {
        return mSimpleTable.updateValues(values, query);
    }

    public long insertOrUpdate(JsonObject object, Query query) {
        return mSimpleTable.insertOrUpdate(fromJsonObject(object), query);
    }

    @Override
    public void delete(Query query) {
        mSimpleTable.delete(query);
    }

    public long replace(JsonObject object) {
        return mSimpleTable.replace(fromJsonObject(object));
    }

    @Override
    public HashMap<String, TableBuilder.ColumnInfo> getColumns() {
        return mSimpleTable.getColumns();
    }

    @Override
    public TableBuilder.ColumnInfo getColumnInfo(String name) {
        return mSimpleTable.getColumnInfo(name);
    }

    private ContentValues fromJsonObject(JsonObject object) {
        ContentValues values = new ContentValues();
        for (Map.Entry<String, JsonElement> entry : object.entrySet())
            if (entry.getValue() instanceof JsonPrimitive) {
                JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
                if (primitive.isBoolean()) {
                    values.put(entry.getKey(), primitive.getAsBoolean());
                } else if (primitive.isNumber()) {
                    Number number = primitive.getAsNumber();
                    if (number.doubleValue() == number.longValue()) {
                        values.put(entry.getKey(), entry.getValue().getAsLong());
                    } else {
                        values.put(entry.getKey(), entry.getValue().getAsDouble());
                    }
                } else if (primitive.isString()) {
                    values.put(entry.getKey(), primitive.getAsString());
                }
            }
            else {
                values.put(entry.getKey(), entry.getValue().toString());
            }
        return values;
    }

    private JsonObject toJsonObject(ContentValues values) {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, Object> entry : values.valueSet()) {
            JsonElement element = null;
            Object object = entry.getValue();
            if (object instanceof Number) {
                element = new JsonPrimitive((Number) object);
            } else if (object instanceof Boolean) {
                element = new JsonPrimitive((Boolean) object);
            } else if (object instanceof String) {
                if (getColumnInfo(entry.getKey()).getType() == TableBuilder.Type.JSON) {
                    element = mJsonParser.parse((String) object);
                } else {
                    element = new JsonPrimitive((String) object);
                }
            } else if (object instanceof Character) {
                element = new JsonPrimitive((Character) object);
            }
            if (element != null) {
                jsonObject.add(entry.getKey(), element);
            }
        }
        return jsonObject;
    }
}