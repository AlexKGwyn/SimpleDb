package com.alexkgwyn.simpledb;

import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class TableBuilder<T extends Table> {

    public enum Type {
        TEXT("TEXT"), INTEGER("INTEGER"), DOUBLE("REAL"), BOOLEAN("INTEGER"), JSON("TEXT");

        public String sql;

        Type(String sql) {
            this.sql = sql;
        }

    }

    public enum Property {
        PRIMARY_KEY("PRIMARY KEY"), AUTO_INCREMENT("AUTOINCREMENT");

        public String sql;

        Property(String sql) {
            this.sql = sql;
        }
    }

    private String mName;
    private LinkedHashMap<String, ColumnInfo> mColumns = new LinkedHashMap<>();

    public TableBuilder(String name) {
        mName = name;
    }

    public TableBuilder addColumn(String name, Type type, Property... properties) {
        mColumns.put(name, new ColumnInfo(type, properties));
        return this;
    }

    public String toSqlString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(mName).append(" (");
        boolean first = true;
        for (Map.Entry<String, ColumnInfo> entry : mColumns.entrySet()) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(entry.getKey()).append(" ").append(entry.getValue().toSqlString());
        }
        builder.append(");");
        return builder.toString();
    }

    public class ColumnInfo {
        private Type mType;
        private List<Property> mProperties;

        ColumnInfo(Type type, Property... properties) {
            mType = type;
            mProperties = Arrays.asList(properties);
        }

        public String toSqlString() {
            StringBuilder builder = new StringBuilder();
            builder.append(mType.sql);
            for (int i = 0; i < mProperties.size(); i++) {
                Property property = mProperties.get(i);
                builder.append(" ").append(property.sql);
            }
            return builder.toString();
        }

        public Type getType() {
            return mType;
        }

        public List<Property> getProperties() {
            return mProperties;
        }

        public boolean hasProperty(Property property) {
            return mProperties.contains(property);
        }
    }

    public String getName() {
        return mName;
    }

    public LinkedHashMap<String, ColumnInfo> getColumns() {
        return mColumns;
    }

    public abstract T getTable(SQLiteDatabase database);
}
