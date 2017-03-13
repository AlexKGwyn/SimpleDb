package com.alexgwyn.simpledb;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Map;

public class JsonTable implements Table<JsonObject> {

    private SimpleTable mSimpleTable;

    public JsonTable(String name, SQLiteDatabase database) {
        mSimpleTable = new SimpleTable(name, database);
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
        return values;
    }

    private JsonObject toJsonObject(ContentValues values) {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, Object> entry : values.valueSet()) {
            JsonPrimitive primitive = null;
            Object object = entry.getValue();
            if (object instanceof Number) {
                primitive = new JsonPrimitive((Number) object);
            } else if (object instanceof Boolean) {
                primitive = new JsonPrimitive((Boolean) object);
            } else if (object instanceof String) {
                primitive = new JsonPrimitive((String) object);
            } else if (object instanceof Character) {
                primitive = new JsonPrimitive((Character) object);
            }
            if (primitive != null) {
                jsonObject.add(entry.getKey(), primitive);
            }
        }
        return jsonObject;
    }
}