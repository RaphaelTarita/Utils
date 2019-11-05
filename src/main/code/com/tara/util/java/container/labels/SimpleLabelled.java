package com.tara.util.java.container.labels;

import java.util.Objects;

public class SimpleLabelled<T> implements Labelled<T> {
    private T value;
    private String label;

    public SimpleLabelled(T value, String label) {
        this.value = value;
        this.label = label;
    }

    public SimpleLabelled(T value) {
        this(value, "");
    }

    public SimpleLabelled(String label) {
        this(null, label);
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public void value(T newV) {
        value = newV;
    }

    public void label(String newL) {
        label = newL;
    }

    public void labelSelf() {
        label = valOrNull();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Labelled)
                && ((Labelled) obj).value().equals(value)
                && ((Labelled) obj).label().equals(label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, label);
    }

    @Override
    public String toString() {
        return !label.isBlank()
                ? label
                : "<blank label>"
                        + ": "
                        + valOrNull();
    }

    private static <T> String valOrNull(T value) {
        return value != null
                ? value.toString()
                : "<null>";
    }

    private String valOrNull() {
        return valOrNull(value);
    }
}
