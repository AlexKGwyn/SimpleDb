package com.alexgwyn.simpledb;

import java.util.Map;

public abstract class ViewBuilder<T extends View> extends QueryableBuilder<T> {

    public ViewBuilder(String name) {
        super(name);
    }

    public String toSqlString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE VIEW ").append(getName()).append(" (");
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
