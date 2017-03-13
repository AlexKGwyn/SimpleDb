package com.alexgwyn.simpledb;

public class Selection {

    public enum Operator {
        EQUAL("="), NOT_EQUAL("!="), LESS_THAN("<"), GREATER_THAN(">"), LESS_THAN_EQUAL("<="), GREATER_THAN_EQUAL(">=");

        String sql;

        Operator(String sql) {
            this.sql = sql;
        }
    }

    private String mColumn;
    private Operator mOperator;
    private String mValue;

    public Selection(String column, Operator operator, Object value) {
        mColumn = column;
        mOperator = operator;
        mValue = value.toString();
    }

    public String getColumn() {
        return mColumn;
    }

    public Operator getOperator() {
        return mOperator;
    }

    public String getValue() {
        return mValue;
    }
}