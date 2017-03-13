package com.alexgwyn.simpledb;

import java.util.ArrayList;

public class Query {

    public static final String BOOLEAN_TRUE = "1";
    public static final String BOOLEAN_FALSE = "0";

    public enum Order {
        ASCENDING("ASC"), DESCENDING("DESC");

        String sql;

        Order(String sql) {
            this.sql = sql;
        }
    }

    private ArrayList<Selection> mSelections = new ArrayList<>();
    private Order mOrder;
    private Integer mLimit;

    public Query addSelection(Selection selection) {
        mSelections.add(selection);
        return this;
    }

    public Query addSelection(String column, Selection.Operator operator, String value) {
        addSelection(new Selection(column, operator, value));
        return this;
    }

    public Query addSelection(String column, Selection.Operator operator, boolean value) {
        addSelection(new Selection(column, operator, value ? BOOLEAN_TRUE : BOOLEAN_FALSE));
        return this;
    }


    public Query addSelection(String column, Selection.Operator operator, Object value) {
        addSelection(new Selection(column, operator, value.toString()));
        return this;
    }

    public Query setOrder(Order order) {
        mOrder = order;
        return this;
    }

    public Query setLimit(int limit) {
        mLimit = limit;
        return this;
    }

    public boolean hasOrder() {
        return mOrder != null;
    }

    public String getOrder() {
        return mOrder.sql;
    }

    public boolean hasLimit() {
        return mLimit != null;
    }

    public String getLimit() {
        return String.valueOf(mLimit);
    }

    public String getWhereClause() {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < mSelections.size(); i++) {
            if (!first) {
                builder.append(" AND ");
            }
            first = false;
            Selection selection = mSelections.get(i);
            builder.append(selection.getColumn()).append(" ").append(selection.getOperator().sql).append(" ?");
        }
        return builder.toString();
    }

    public String[] getSelectionClause() {
        String[] selections = new String[mSelections.size()];
        for (int i = 0; i < mSelections.size(); i++) {
            Selection selection = mSelections.get(i);
            selections[i] = selection.getValue();
        }
        return selections;
    }
}
