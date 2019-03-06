package com.alexgwyn.simpledb;

import java.util.Map;

public abstract class TableBuilder<T extends Table> extends QueryableBuilder<T> {

    public TableBuilder(String name) {
        super(name);
    }

    public String toSqlString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(getName()).append(" (");
        boolean first = true;
        for (Map.Entry<String, ColumnInfo> entry : getColumns().entrySet()) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(entry.getKey()).append(" ").append(entry.getValue().toSqlString());
        }
        builder.append(");");
        return builder.toString();
    }

}
