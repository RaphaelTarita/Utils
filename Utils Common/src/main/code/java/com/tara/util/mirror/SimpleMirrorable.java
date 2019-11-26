package com.tara.util.mirror;

import java.util.Objects;

public class SimpleMirrorable<T> implements Mirrorable<SimpleMirrorable<T>> {
    private T value;

    private SimpleMirrorable() {
        value = null;
    }

    public SimpleMirrorable(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public SimpleMirrorable<T> mirror() {
        return new SimpleMirrorable<>(Mirrors.mirror(value));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this)
                || (obj instanceof SimpleMirrorable)
                && Objects.equals(((SimpleMirrorable<?>) obj).value, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "m{ " + value.toString() + " }";
    }
}
