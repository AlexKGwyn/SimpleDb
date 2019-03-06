package com.alexgwyn.simpledb;

import java.util.ArrayList;

public interface Queryable<T> {
    ArrayList<T> getAll();

    ArrayList<T> get(Query query);

    T getFirst(Query query);
}
