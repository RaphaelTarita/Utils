package com.tara.util.container.labels;

public interface Labelled<T> {
    String label();
    T value();
    void value(T newV);

    @Override
    boolean equals(Object other);

    @Override
    int hashCode();

    @Override
    String toString();
}
