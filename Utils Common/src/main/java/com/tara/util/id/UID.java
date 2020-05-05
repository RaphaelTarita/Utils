package com.tara.util.id;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface UID extends Serializable {
    private static void load(Class<?> clazz) {
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Class not found: " + clazz.getName(), ex);
        }
    }

    Set<UID> instanceRegistry = new HashSet<>();
    Map<Class<? extends UID>, Function<String, ? extends UID>> builderRegistry = new HashMap<>();

    static boolean check(UID id) {
        return id != null
            && !instanceRegistry.contains(id)
            && builderRegistry.containsKey(id.getClass());
    }

    static void registerUID(UID id) {
        if (!instanceRegistry.add(id)) {
            throw new NotUniqueException(id);
        }
    }

    static <U extends UID> void registerBuilder(Class<U> clazz, Function<String, U> stringMapper) {
        builderRegistry.put(clazz, stringMapper);
    }

    default boolean check() {
        return check(this);
    }

    default void registerUID() {
        registerUID(this);
    }

    static Function<String, ? extends UID> getUIDMapper(Class<? extends UID> clazz) {
        if (!builderRegistry.containsKey(clazz)) {
            load(clazz);
        }
        return builderRegistry.get(clazz);
    }

    static String mapUID(UID id) {
        return id != null
            ? id.mapUID()
            : "";
    }

    String mapUID();

    @Override
    String toString();

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);
}
