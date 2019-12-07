package com.tara.util.id;

import java.io.Serializable;
import java.util.HashSet;
import java.util.function.Function;

public interface UID extends Serializable {
    HashSet<UID> instanceRegistry = new HashSet<>();

    static boolean check(UID id) {
        return !instanceRegistry.contains(id);
    }

    static void register(UID id) {
        if (!instanceRegistry.add(id)) {
            throw new NotUniqueException(id);
        }
    }

    default boolean check() {
        return check(this);
    }

    default void register() {
        register(this);
    }

    Function<String, UID> stringConverter();

    @Override
    String toString();

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);
}
