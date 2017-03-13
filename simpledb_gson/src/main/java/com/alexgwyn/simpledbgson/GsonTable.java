package com.alexgwyn.simpledbgson;

import android.database.sqlite.SQLiteDatabase;

import com.alexgwyn.simpledb.ConflictMode;
import com.alexgwyn.simpledb.Query;
import com.alexgwyn.simpledb.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GsonTable<T> implements Table<T> {

    private JsonTable mJsonTable;
    private Gson mGson;
    private Class<T> mClass;

    public static Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Boolean.TYPE, new BooleanDeserializer());
        return builder.create();
    }

    private static class BooleanDeserializer implements JsonDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String value = json.getAsString();
            try {
                int val = Integer.valueOf(value);
                return val != 0;
            } catch (NumberFormatException ignored) {

            }
            return Boolean.parseBoolean(value);
        }
    }

    public GsonTable(String name, SQLiteDatabase database, Class<T> aClass) {
        this(name, database, aClass, buildGson());
    }

    public GsonTable(String name, SQLiteDatabase database, Class<T> aClass, Gson gson) {
        mJsonTable = new JsonTable(name, database);
        mClass = aClass;
        mGson = gson;
    }

    public ArrayList<T> getAll() {
        ArrayList<T> ret = new ArrayList<>();
        for (JsonObject jsonObject : mJsonTable.getAll()) {
            ret.add(mGson.fromJson(jsonObject, mClass));
        }
        return ret;
    }

    public ArrayList<T> get(Query query) {
        ArrayList<T> ret = new ArrayList<>();
        for (JsonObject jsonObject : mJsonTable.get(query)) {
            ret.add(mGson.fromJson(jsonObject, mClass));
        }
        return ret;
    }

    public T getFirst(Query query) {
        JsonObject object = mJsonTable.getFirst(query);
        if (object != null) {
            return mGson.fromJson(object, mClass);
        }
        return null;
    }

    public long insert(T object) {
        JsonElement element = mGson.toJsonTree(object);
        return mJsonTable.insert(element.getAsJsonObject());
    }

    public long insert(T object, ConflictMode mode) {
        JsonElement element = mGson.toJsonTree(object);
        return mJsonTable.insert(element.getAsJsonObject(), mode);
    }

    public long update(T object, Query query) {
        JsonElement element = mGson.toJsonTree(object);
        return mJsonTable.update(element.getAsJsonObject(), query);
    }

    public long insertOrUpdate(T object, Query query) {
        JsonElement element = mGson.toJsonTree(object);
        return mJsonTable.insertOrUpdate(element.getAsJsonObject(), query);
    }

    @Override
    public void delete(Query query) {
        mJsonTable.delete(query);
    }

    public long replace(T object) {
        JsonElement element = mGson.toJsonTree(object);
        return mJsonTable.replace(element.getAsJsonObject());
    }
}
