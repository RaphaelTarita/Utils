package com.tara.util.mirror;

public class SimpleMirrorable<T> implements Mirrorable<SimpleMirrorable<T>> {
    private T value;

    private SimpleMirrorable() {
        value = null;
    }

    public SimpleMirrorable(T value) {
        this.value = value;
    }

    @Override
    public Mirrorable<SimpleMirrorable<T>> mirrorWrap() {
        return new SimpleMirrorable<>(Mirrors.mirror(value));
    }

    @Override
    public SimpleMirrorable<T> get() {
        return this;
    }
}
