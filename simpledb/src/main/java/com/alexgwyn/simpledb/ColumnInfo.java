package com.alexgwyn.simpledb;

import java.util.Arrays;
import java.util.List;

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