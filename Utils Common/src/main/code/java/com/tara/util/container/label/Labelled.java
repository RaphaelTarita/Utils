package com.tara.util.container.label;

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
