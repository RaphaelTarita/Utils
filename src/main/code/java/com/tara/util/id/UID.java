package com.tara.util.id;

import java.io.Serializable;

public interface UID<T> extends Serializable {
    boolean taken(T idGenerator);
    UID<T> newUnique(T generator);
    T getGenerator();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}
