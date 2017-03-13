package com.alexgwyn.simpledb;

import java.util.ArrayList;

public interface Table<T> {
    ArrayList<T> getAll();

    ArrayList<T> get(Query query);

    T getFirst(Query query);

    long insert(T values);

    long insert(T values, ConflictMode mode);

    long update(T values, Query query);

    long insertOrUpdate(T values, Query query);

    void delete(Query query);

    long replace(T values);
}
