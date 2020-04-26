package com.tara.util.persistence.entity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class VirtualParameterizedType implements ParameterizedType {
    private final Type raw;

    private final Type owner;
    private final Type[] typeArgs;

    public VirtualParameterizedType(Class<?> raw, Type owner, Type[] typeArgs) {
        this.raw = raw;
        this.owner = owner;
        this.typeArgs = typeArgs == null ? new Type[0] : typeArgs;
    }

    public VirtualParameterizedType(Class<?> raw, Type[] typeArgs) {
        this.raw = raw;
        this.owner = raw.getDeclaringClass();
        this.typeArgs = typeArgs;
    }

    public VirtualParameterizedType(Type raw, Type owner) {
        this.raw = raw;
        this.owner = owner;
        this.typeArgs = new Type[0];
    }

    public VirtualParameterizedType(Type raw) {
        this.raw = raw;
        this.owner = raw instanceof Class<?>
            ? ((Class<?>) raw).getDeclaringClass()
            : null;
        this.typeArgs = new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        if (raw instanceof ParameterizedType) {
            return ((ParameterizedType) raw).getActualTypeArguments();
        } else {
            return typeArgs;
        }
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return owner;
    }
}
