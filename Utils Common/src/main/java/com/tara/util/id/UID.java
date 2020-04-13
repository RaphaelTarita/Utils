package com.tara.util.id;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface UID extends Serializable {
    private static void load(Class<?> clazz) {
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Class not found: " + clazz.getName(), ex);
        }
    }

    Map<UID, UID> instanceRegistry = new HashMap<>();
    Map<Class<? extends UID>, Function<String, ? extends UID>> builderRegistry = new HashMap<>();

    static boolean check(UID id) {
        return id != null
            && !instanceRegistry.containsKey(id)
            && builderRegistry.containsKey(id.getClass());
    }

    static void registerUID(UID id) {
        UID dupCandidate;
        if ((dupCandidate = instanceRegistry.putIfAbsent(id, id)) != null) {
            throw new NotUniqueException(dupCandidate);
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

    @SuppressWarnings("unchecked")
    static <U extends UID> Function<String, U> getUIDMapper(Class<U> clazz) {
        if (!builderRegistry.containsKey(clazz)) {
            load(clazz);
        }
        return (Function<String, U>) builderRegistry.get(clazz);
    }

    static String mapUID(UID id) {
        return id != null
            ? id.mapUID()
            : "";
    }

    static <U extends UID> U mapString(Class<U> uidType, String marshalled) {
        return getUIDMapper(uidType).apply(marshalled);
    }

    @SuppressWarnings("unchecked")
    static <U extends UID> U getCanonical(Class<U> uidType, String marshalled) {
        try {
            return mapString(uidType, marshalled);
        } catch (NotUniqueException ex) {
            UID candidate = ex.getDuplicate();
            if (uidType.isInstance(candidate)) {
                return (U) candidate;
            } else {
                return null;
            }
        }
    }

    String mapUID();

    @Override
    String toString();

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);
}
